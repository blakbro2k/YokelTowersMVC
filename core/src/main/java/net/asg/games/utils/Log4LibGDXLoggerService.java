package net.asg.games.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import net.asg.games.utils.Log4LibGDXLogger.Log4LibGDXLoggerFactory;

public class Log4LibGDXLoggerService extends LoggerService {
    public static final Log4LibGDXLoggerService INSTANCE = new Log4LibGDXLoggerService();
    private final ObjectMap<Class<?>, Boolean> active = GdxMaps.newObjectMap();
    private boolean isProductionMode = false;

    private Log4LibGDXLoggerService(){
        super();
        // Services manage their own settings, we need to override default settings to turn on all logging levels.
        Gdx.app.setLogLevel(Application.LOG_ERROR);
        clearLoggersCache();
        setFactory(new Log4LibGDXLoggerFactory());
    }

    public void setActiveLogger(Class<?> forClass, boolean isActive){
        active.put(forClass, isActive);
    }

    public void addActiveLogger(Class<?> forClass){
        System.out.println("ADD: active:" + this.getClass());
        System.out.println("active(s):" + active);
        active.put(forClass, isProductionMode);
    }

    public ObjectMap<Class<?>, Boolean> getActives(){
        return active;
    }

    public boolean isActive(Class<?> forClass){
        return active.get(forClass);
    }

    public boolean isProductionMode(){
        return isProductionMode;
    }

    public void setProductionMode(boolean isProductionMode){
        if(!isProductionMode){
            setDebugOn(true);
        }
        this.isProductionMode = isProductionMode;
    }

    public static Log4LibGDXLogger forClass(final Class<?> forClass) {
        if(INSTANCE.getLoggerForClass(forClass) instanceof Log4LibGDXLogger) {
            Log4LibGDXLoggerService.INSTANCE.addActiveLogger(forClass);
            return (Log4LibGDXLogger) INSTANCE.getLoggerForClass(forClass);
        }
        throw new RuntimeException("Logger for class: " + forClass + " must be of " + Log4LibGDXLogger.class);
    }
}
