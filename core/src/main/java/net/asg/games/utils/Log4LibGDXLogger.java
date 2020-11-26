package net.asg.games.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerFactory;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.log.formatter.TextFormatter;
import com.github.czyzby.kiwi.log.impl.DebugLogger;
import com.github.czyzby.kiwi.log.impl.DefaultLogger;

import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a toggleable per class logger.
 *
 * @author Blakbro2k
 * @see DebugLogger */
public class Log4LibGDXLogger extends DefaultLogger {
    private final TextFormatter formatter;
    private final String TOKEN_PATTERN_STR = "\\{[0-9]*}";
    private final Pattern tokenPattern = Pattern.compile(TOKEN_PATTERN_STR);

    public Log4LibGDXLogger(final LoggerService service, final Class<?> forClass) {
        super(service, forClass);
        formatter = service.getFormatter();
        setError();
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
        System.out.println(Gdx.app.getLogLevel());

        if (isDebugOn()) {
            prepareLogMessage(message, arguments);
            logDebug(getDebugTag(), formatter.format(message, arguments));
        }
    }

     @Override
    public void debug(final Throwable exception, final String message, final Object... arguments) {

         if (isDebugOn()) {
            prepareLogMessage(message, arguments);
            logDebug(getDebugTag(), formatter.format(message, arguments), exception);
        }
    }

    @Override
    public void info(final String message, final Object... arguments) {
        if (isInfoOn()) {
            prepareLogMessage(message, arguments);
            logInfo(getInfoTag(), formatter.format(message, arguments));
        }
    }

    @Override
    public void info(final Throwable exception, final String message, final Object... arguments) {
        if (isInfoOn()) {
            prepareLogMessage(message, arguments);
            logInfo(getInfoTag(), formatter.format(message, arguments), exception);
        }
    }

    @Override
    public void error(final String message, final Object... arguments) {
        if (isErrorOn()) {
            prepareLogMessage(message, arguments);
            logError(getErrorTag(), formatter.format(message, arguments));
        }
    }

    @Override
    public void error(final Throwable exception, final String message, final Object... arguments) {
        if (isErrorOn()) {
            prepareLogMessage(message, arguments);
            logError(getErrorTag(), formatter.format(message, arguments), exception);
        }
    }

    private int countArgumentsInString(String str){
        int count = 0;
        if(!YokelUtilities.isEmpty(str)){
            Matcher matcher = tokenPattern.matcher(str);
            while (matcher.find())
                count++;
        }
        return count;
    }

    /**
     * Prepares replacement tokens with correct arguments.
     * @param message
     * @param arguments
     */
    private void prepareLogMessage(final String message, final Object... arguments){
        System.out.println("arguments: " + Arrays.toString(arguments));
        System.out.println("arguments count: " + countArgumentsInString(message));
        Matcher matcher = tokenPattern.matcher(message);
        //matcher.rep
        System.out.println("arguments match 1: " + matcher.find(0));

        ;

    }

    public static class Log4LibGDXLoggerFactory implements LoggerFactory {
        @Override
        public Logger newLogger(final LoggerService service, final Class<?> forClass) {
            return new Log4LibGDXLogger(service, forClass);
        }
    }
}
