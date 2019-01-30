package net.asg.games.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Blakbro2k on 1/2/2018.
 */

public class TestingUtils {
    private TestingUtils(){}

    public static class TestMethod{
        String methodName;
        Class<?> parentClass;
        Class<?>[] methodParameterClasses;
        Object[] parameterValues;
        Object instantiatedObject;


        public TestMethod(String methodName, Class<?> parentClass, Class<?>[] methodParameterClasses, Object[] parameterValues){
            this(methodName,parentClass,methodParameterClasses,parameterValues,null);
        }

        public TestMethod(String methodName, Class<?> parentClass, Class<?>[] methodParameterClasses, Object[] parameterValues, Object instantiatedObject){
            setMethodName(methodName);
            setParentClass(parentClass);
            setMethodParameterClasses(methodParameterClasses);
            setParameterValues(parameterValues);
            setInstantiatedObject(instantiatedObject);
        }

        public void setMethodName(String methodName){
            this.methodName = methodName;
        }

        public String getMethodName(){
            return this.methodName;
        }

        public void setParentClass(Class<?> parentClass){
            this.parentClass = parentClass;
        }

        public Class<?> getParentClass(){
            return this.parentClass;
        }

        public void setMethodParameterClasses(Class<?>[] methodParameterClasses){
            this.methodParameterClasses = methodParameterClasses;
        }

        public Class<?>[] getMethodParameterClasses(){
            return this.methodParameterClasses;
        }

        public void setParameterValues(Object[] parameterValues){
            this.parameterValues = parameterValues;
        }

        public Object[] getParameterValues(){
            return this.parameterValues;
        }

        public void setInstantiatedObject(Object instantiatedObject){
            this.instantiatedObject = instantiatedObject;
        }

        public Object getInstantiatedObject(){
            return this.instantiatedObject;
        }
    }

    public static Object invokeMethod(TestMethod method, boolean isStatic) throws InvocationTargetException {
        if(method != null){
            return invokeStaticMethod(method.getParentClass(),
                    method.getMethodName(),
                    method.getMethodParameterClasses(),
                    method.getParameterValues(),
                    method.getInstantiatedObject());
        }
        return null;
    }

    public static Object invokeStaticMethod(Class<?> targetClass, String methodName, Class[] argClasses, Object[] argObjects, Object object) throws InvocationTargetException {
        try {
            if(targetClass != null){
                Method method = targetClass.getDeclaredMethod(methodName, argClasses);
                if(method != null){
                    method.setAccessible(true);
                    return method.invoke(object, argObjects);
                }
            }
            return null;
        }
        catch (NoSuchMethodException e) {
            // Should happen only rarely, because most times the
            // specified method should exist. If it does happen, just let
            // the test fail so the programmer can fix the problem.
            throw new InvocationTargetException(e);
        }
        catch (SecurityException e) {
            // Should happen only rarely, because the setAccessible(true)
            // should be allowed in when running unit tests. If it does
            // happen, just let the test fail so the programmer can fix
            // the problem.
            throw new InvocationTargetException(e);
        }
        catch (IllegalAccessException e) {
            // Should never happen, because setting accessible flag to
            // true. If setting accessible fails, should throw a security
            // exception at that point and never get to the invoke. But
            // just in case, wrap it in a TestFailedException and let a
            // human figure it out.
            throw new InvocationTargetException(e);
        }
        catch (IllegalArgumentException e) {
            // Should happen only rarely, because usually the right
            // number and types of arguments will be passed. If it does
            // happen, just let the test fail so the programmer can fix
            // the problem.
            throw new InvocationTargetException(e);
        }
    }

    public static String printTestMethod(Class<?> targetClass, String methodName, Class[] argClasses, Object[] argObjects, Object object) throws InvocationTargetException{
        return getMethodCallString(methodName, argClasses, argObjects) + "=" + buildMethodReturnString(targetClass,methodName,argClasses,argObjects,object);
    }

    private static String buildMethodReturnString(Class<?> targetClass, String methodName, Class[] argClasses, Object[] argObjects, Object object) throws InvocationTargetException{
        Object returnType = getMethodReturnType(targetClass, methodName, argClasses);
        Object returnObject = invokeStaticMethod(targetClass, methodName, argClasses, argObjects, object);
        return "(" + returnType + ")" + (returnObject == null? "" : returnObject);
    }

    private static Object getMethodReturnType(Class<?> targetClass, String methodName, Class[] argClasses) throws InvocationTargetException{
        try{
            if(targetClass != null){
                Method method = targetClass.getDeclaredMethod(methodName, argClasses);
                if(method != null){
                    method.setAccessible(true);
                    return method.getReturnType().getSimpleName();
                }
            }
            return null;
        } catch (NoSuchMethodException e) {
            // Should happen only rarely, because most times the
            // specified method should exist. If it does happen, just let
            // the test fail so the programmer can fix the problem.
            throw new InvocationTargetException(e);
        }
    }

    private static String getMethodCallString(String methodName, Class[] argClasses, Object[] argObjects) throws InvocationTargetException{
        StringBuilder sb = new StringBuilder();
        sb.append(methodName).append("(");

        if(argClasses.length == 0){
            sb.append(")");
        }
        for(int i = 0; i < argClasses.length; i++) {
            Class<?> clazz = argClasses[i];
            if (clazz != null) {
                sb.append(clazz.getSimpleName()).append("=").append(argObjects[i]);
                if (i == argClasses.length - 1) {
                    sb.append(")");
                } else {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    public static Boolean getBoolean(Object o){
        if(o instanceof Boolean){
            return (Boolean) o;
        }
        throw new IllegalArgumentException(o + " is not an instance of Boolean");
    }

    public static Integer getInteger(Object o){
        if(o instanceof Integer){
            return (Integer) o;
        }
        throw new IllegalArgumentException(o + " is not an instance of Integer");
    }

    public static Float getFloat(Object o){
        if(o instanceof Float){
            return (Float) o;
        }
        throw new IllegalArgumentException(o + " is not an instance of Float");
    }

    public static String getString(Object o){
        if(o instanceof String){
            return (String) o;
        }
        throw new IllegalArgumentException(o + " is not an instance of String");
    }
}

