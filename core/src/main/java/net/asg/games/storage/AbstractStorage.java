package net.asg.games.storage;

import net.asg.games.game.objects.AbstractYokelObject;
import net.asg.games.game.objects.YokelObject;
import net.asg.games.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractStorage implements Storage {
    private final static String STORAGE_NAME_METHOD = "getName";
    private final static String STORAGE_ID_METHOD = "getId";

    @Override
    abstract public <T extends YokelObject> T getObjectByName(@NotNull Class<T> clazz, String name);

    @Override
    abstract public <T extends YokelObject> T getObjectById(@NotNull Class<T> clazz, String id);

    @Override
    abstract public void saveObject(Object object);

    @Override
    abstract public void commitTransactions();

    @Override
    abstract public void rollTransactions();

    String getNameOrIdFromInstance(Object o, boolean getName) {
        Object var = null;
        try {
            Class<?> c = getClassFromSuper(AbstractYokelObject.class, o);
            String methodName = getName ? STORAGE_NAME_METHOD : STORAGE_ID_METHOD;
            Method m = c.getDeclaredMethod(methodName);
            var = m.invoke(o);
        } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
        return Util.otos(var);
    }

    public Class<?> getClassFromSuper(@NotNull Class<?> klass, Object o){
        if(o != null){
            Class<?> var = o.getClass().getSuperclass();
            int thresh = 0;
            do {
                if (var.getTypeName().equals(klass.getTypeName()) || var.getTypeName().equals(Object.class.getTypeName())) {
                    return var;
                }
                var = var.getSuperclass();
            } while (++thresh <= 1000);
        }
        return null;
    }
}