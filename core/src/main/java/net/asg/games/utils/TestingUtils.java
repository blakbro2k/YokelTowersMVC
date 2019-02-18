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
        boolean isPrivate;

        public TestMethod(String methodName, Class<?> parentClass, Class<?>[] methodParameterClasses, Object[] parameterValues, Object instantiatedObject, boolean isPrivate){
            setMethodName(methodName);
            setParentClass(parentClass);
            setMethodParameterClasses(methodParameterClasses);
            setParameterValues(parameterValues);
            setInstantiatedObject(instantiatedObject);
            setPrivate(isPrivate);
        }

        public TestMethod(String methodName, Class<?> parentClass, Class<?>[] methodParameterClasses, Object[] parameterValues, Object instantiatedObject){
            this(methodName,parentClass,methodParameterClasses, parameterValues,instantiatedObject, false);
        }

        void setMethodName(String methodName){
            this.methodName = methodName;
        }

        String getMethodName(){
            return this.methodName;
        }

        void setParentClass(Class<?> parentClass){
            this.parentClass = parentClass;
        }

        Class<?> getParentClass(){
            return this.parentClass;
        }

        void setMethodParameterClasses(Class<?>... methodParameterClasses){
            this.methodParameterClasses = methodParameterClasses;
        }

        Class<?>[] getMethodParameterClasses(){
            return this.methodParameterClasses;
        }

        public void setParameterValues(Object... parameterValues){
            this.parameterValues = parameterValues;
        }

        Object[] getParameterValues(){
            return this.parameterValues;
        }

        void setInstantiatedObject(Object instantiatedObject){
            this.instantiatedObject = instantiatedObject;
        }

        Object getInstantiatedObject(){
            return this.instantiatedObject;
        }

        public void setPrivate(boolean isPrivate){
            this.isPrivate = isPrivate;
        }

        boolean isPrivate(){
            return isPrivate;
        }

        public Object invoke() throws InvocationTargetException {
            return invokeMethod(this, isPrivate());
        }

        public Object returnType() throws InvocationTargetException {
            return getMethodReturnType(this);
        }

        @Override
        public String toString(){
            try {
                return printTestMethod(getParentClass(),
                        getMethodName(),
                        getMethodParameterClasses(),
                        getParameterValues(),
                        getInstantiatedObject(),
                        isPrivate());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return super.toString();
        }
    }

    public static Object invokeMethod(TestMethod testMethod, boolean isStatic) throws InvocationTargetException {
        if(testMethod != null){
            return invokeStaticMethod(testMethod.getParentClass(),
                    testMethod.getMethodName(),
                    testMethod.getMethodParameterClasses(),
                    testMethod.getParameterValues(),
                    testMethod.getInstantiatedObject(),
                    isStatic);
        }
        return null;
    }

    public static Object getMethodReturnType(TestMethod testMethod) throws InvocationTargetException {
        if(testMethod != null){
            return getMethodReturnType(testMethod.getParentClass(),
                    testMethod.getMethodName(),
                    testMethod.getMethodParameterClasses(),
                    testMethod.getParameterValues(),
                    testMethod.getInstantiatedObject());
        }
        return null;
    }

    public static Object getMethodReturnType(Class<?> targetClass, String methodName, Class[] argClasses, Object[] argObjects, Object object) throws InvocationTargetException {
        try {
            if (targetClass != null) {
                Method method = targetClass.getDeclaredMethod(methodName, argClasses);
                if (method != null) {
                    return method.getReturnType();
                }
            }
            return null;
        } catch (NoSuchMethodException e) {
            // Should happen only rarely, because most times the
            // specified method should exist. If it does happen, just let
            // the test fail so the programmer can fix the problem.
            throw new InvocationTargetException(e);
        } catch (SecurityException e) {
            // Should happen only rarely, because the setAccessible(true)
            // should be allowed in when running unit tests. If it does
            // happen, just let the test fail so the programmer can fix
            // the problem.
            throw new InvocationTargetException(e);
        } catch (IllegalArgumentException e) {
            // Should happen only rarely, because usually the right
            // number and types of arguments will be passed. If it does
            // happen, just let the test fail so the programmer can fix
            // the problem.
            throw new InvocationTargetException(e);
        }
    }

        public static Object invokeStaticMethod(Class<?> targetClass, String methodName, Class[] argClasses, Object[] argObjects, Object object, boolean doPrivate) throws InvocationTargetException {
        try {
            if(targetClass != null){
                Method method = targetClass.getDeclaredMethod(methodName, argClasses);
                if(method != null){
                    method.setAccessible(doPrivate);
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

    public static String printTestMethod(Class<?> targetClass, String methodName, Class[] argClasses, Object[] argObjects, Object object, boolean isPrivate) throws InvocationTargetException{
        return getMethodCallString(methodName, argClasses, argObjects) + "=" + buildMethodReturnString(targetClass,methodName,argClasses,argObjects,object, isPrivate);
    }

    private static String buildMethodReturnString(Class<?> targetClass, String methodName, Class[] argClasses, Object[] argObjects, Object object, boolean isPrivate) throws InvocationTargetException{
        Object returnType = getMethodReturnType(targetClass, methodName, argClasses);
        Object returnObject = invokeStaticMethod(targetClass, methodName, argClasses, argObjects, object, isPrivate);
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

    private static String getMethodCallString(String methodName, Class[] argClasses, Object[] argObjects) {
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

