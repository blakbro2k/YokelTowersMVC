package net.asg.games.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerFactory;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.log.formatter.TextFormatter;
import com.github.czyzby.kiwi.log.impl.DebugLogger;
import com.github.czyzby.kiwi.log.impl.DefaultLogger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a toggleable per class logger.
 *
 * @author Blakbro2k
 * @see DebugLogger */
public class Log4LibGDXLogger extends DefaultLogger {
    private final TextFormatter formatter;
    private final String TOKEN_PATTERN_STR = "\\{([^{}]*)}";
    private final Pattern tokenPattern = Pattern.compile(TOKEN_PATTERN_STR);
    private final String ENTER_METHOD_TAG = "Enter Method ";
    private final String EXIT_METHOD_TAG = "Exit Method ";
    private final String METHOD_OPEN_TAG = "(";
    private final String METHOD_CLOSE_TAG = ")";

    public Log4LibGDXLogger(final LoggerService service, final Class<?> forClass) {
        super(service, forClass);
        formatter = service.getFormatter();
        //System.err.println(this + ": " + ((Log4LibGDXLoggerService)service).INSTANCE.isProductionMode());

        //setError();
    }

    @Override
    public boolean isDebugOn() {
        return Gdx.app.getLogLevel() >= Application.LOG_DEBUG;
    }

    @Override
    public boolean isInfoOn() {
        return Gdx.app.getLogLevel() >= Application.LOG_INFO;
    }

    @Override
    public boolean isErrorOn() {
        return Gdx.app.getLogLevel() >= Application.LOG_ERROR;
    }

    public void setError(){
        Gdx.app.setLogLevel(Application.LOG_ERROR);
    }

    public void setDebug(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public void setInfo(){
        Gdx.app.setLogLevel(Application.LOG_INFO);
    }

    public int getLoggerLevel(){
        return Gdx.app.getLogLevel();
    }


    @Override
    public void debug(final String message, final Object... arguments) {
        if (isDebugOn()) {
            String preparedMessage = prepareLogMessage(message);
            logDebug(getDebugTag(), formatter.format(preparedMessage, arguments));
        }
    }

    @Override
    public void debug(final Throwable exception, final String message, final Object... arguments) {
        if (isDebugOn()) {
            String preparedMessage = prepareLogMessage(message);
            logDebug(getDebugTag(), formatter.format(preparedMessage, arguments), exception);
        }
    }

    @Override
    public void info(final String message, final Object... arguments) {
        if (isInfoOn()) {
            String preparedMessage = prepareLogMessage(message);
            logInfo(getInfoTag(), formatter.format(preparedMessage, arguments));
        }
    }

    @Override
    public void info(final Throwable exception, final String message, final Object... arguments) {
        if (isInfoOn()) {
            String preparedMessage = prepareLogMessage(message);
            logInfo(getInfoTag(), formatter.format(preparedMessage, arguments), exception);
        }
    }

    @Override
    public void error(final String message, final Object... arguments) {
        if (isErrorOn()) {
            String preparedMessage = prepareLogMessage(message);
            logError(getErrorTag(), formatter.format(preparedMessage, arguments));
        }
    }

    @Override
    public void error(final Throwable exception, final String message, final Object... arguments) {
        if (isErrorOn()) {
            String preparedMessage = prepareLogMessage(message);
            logError(getErrorTag(), formatter.format(preparedMessage, arguments), exception);
        }
    }

    public void enter(final String methodName){
        if (isDebugOn()) {
            logDebug(getDebugTag(), ENTER_METHOD_TAG + methodName + METHOD_OPEN_TAG + METHOD_CLOSE_TAG);
        }
    }

    public void enter(final String methodName, ObjectMap<String, Object> arguments){
        if (isDebugOn()) {
            if(arguments != null){
                StringBuilder sb = new StringBuilder();

                for(String key : arguments.keys()){
                   sb.append(key).append("=").append(arguments.get(key)).append(", ");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.deleteCharAt(sb.length() - 1);

                logDebug(getDebugTag(), ENTER_METHOD_TAG + methodName + METHOD_OPEN_TAG + sb + METHOD_CLOSE_TAG);
            } else {
                enter(methodName);
            }
        }
    }

    public void exit(final String methodName){
        if (isDebugOn()) {
            logDebug(getDebugTag(), EXIT_METHOD_TAG + methodName + METHOD_OPEN_TAG + METHOD_CLOSE_TAG);
        }
    }

    public void exit(final String methodName, Object result){
        if (isDebugOn()) {
            logDebug(getDebugTag(), EXIT_METHOD_TAG + methodName + METHOD_OPEN_TAG + METHOD_CLOSE_TAG + "=" + result);
        }
    }

    /**
     * Prepares replacement tokens with numbered arguments starting at 0.
     * This allows '{}' to be entered instead of forcing each number by the user
     * @param message
     */
    private String prepareLogMessage(final String message){
        Matcher matcher = tokenPattern.matcher(message);

        StringBuilder sb = new StringBuilder();
        String[] splitMessage = message.split(matcher.pattern().toString());
        sb.append(splitMessage[0]);

        if(splitMessage.length == 1) {
            sb.append("{").append(0).append("}");
        } else {
            for(int i = 1; i < splitMessage.length; i++){
                sb.append("{").append(i-1).append("}").append(splitMessage[i]);
            }
        }
        return sb.toString();
    }

    public static class Log4LibGDXLoggerFactory implements LoggerFactory {
        @Override
        public Logger newLogger(final LoggerService service, final Class<?> forClass) {
            return new Log4LibGDXLogger(service, forClass);
        }
    }
}
