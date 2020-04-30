package net.asg.games.storage;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;

import net.asg.games.game.objects.AbstractYokelObject;
import net.asg.games.game.objects.YokelObject;
import net.asg.games.utils.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MemoryStorage implements Storage {
    private ObjectMap<String, Object> _store;
    private Queue<Object> _transactions;

    MemoryStorage(){
        _store = new ObjectMap<>();
        _transactions = new Queue<>();
    }

    @Override
    public void dispose() {
        _transactions.clear();
        _store.clear();
    }

    @Override
    public <T extends YokelObject> T getObjectByName(Class<T> clazz, String name) {
        return clazz.cast(_store.get(name));
    }

    @Override
    public <T extends YokelObject> T getObjectById(Class<T> clazz, String id) {
        return clazz.cast(_store.get(id));
    }

    @Override
    public void saveObject(Object object) {
        _transactions.addFirst(object);
    }

    @Override
    public void commitTransactions() {
        try {
            for(Object o : _transactions){
                String key;
                if(o.getClass().isAssignableFrom(YokelObject.class)){
                    key = getKey(o);
                } else {
                    key = o.toString();
                }
                _store.put(key, o);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void rollTransactions() {
        _transactions.clear();
    }

    public String getKey(Object o) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String var = getNameOrIdFromInstance(o, true);
        if(var == null || "".equalsIgnoreCase(var)){
            var = getNameOrIdFromInstance(o, false);
        }
        return var;
    }

    private String getNameOrIdFromInstance(Object o, boolean getName) {
        Object var = null;
        try {
                Class<?> c = o.getClass().getSuperclass();

                String methodName = getName ? "getName" : "getId";
                System.out.println(c.getTypeName().equals(AbstractYokelObject.class.getTypeName()) + "+" + c.getTypeName().equals(Object.class.getTypeName()));
                Method m = c.getDeclaredMethod(methodName);
                var = m.invoke(o);

        } catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }
        return Util.otos(var);
    }

    private Class<?> getAbstractYokelObjectClassFromSuper(Object o){
        boolean done = false;

        if(o != null){
            Class<?> var = o.getClass().getSuperclass();
            while(done){
                if(var.getTypeName().equals(AbstractYokelObject.class.getTypeName()) || var.getTypeName().equals(Object.class.getTypeName())){
                    done = true;
                }
                var = var.getSuperclass();
            }
        }
        return null;
    }
}