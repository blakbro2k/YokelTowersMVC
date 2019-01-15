package net.asg.games.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Blakbro2k on 1/2/2018.
 */

public class TestingUtils {
    private TestingUtils(){}

    public static Object invokeStaticMethod(Class<?> targetClass, String methodName, Class[] argClasses, Object[] argObjects, Object object) throws InvocationTargetException {
        try {
            Method method = targetClass.getDeclaredMethod(methodName, argClasses);
            method.setAccessible(true);
            return method.invoke(object, argObjects);
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

