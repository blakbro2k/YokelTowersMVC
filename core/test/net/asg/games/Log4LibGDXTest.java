package net.asg.games;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.google.gwt.storage.client.Storage;

import net.asg.games.controller.LoungeController;
import net.asg.games.controller.UITestController;
import net.asg.games.game.managers.GameManager;
import net.asg.games.utils.Log4LibGDXLogger;
import net.asg.games.utils.Log4LibGDXLoggerService;
import net.asg.games.utils.YokelUtilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(GdxTestRunner.class)
public class Log4LibGDXTest {
    private final Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(UITestController.class);
    //Logging Utils
    private static Log4LibGDXLogger validateLogger(Logger logger){
        if(logger instanceof Log4LibGDXLogger){
            return (Log4LibGDXLogger) logger;
        }

        System.out.println("logger logger: " + logger);
        System.out.println("logger logger Class: " + logger.getClass());
        System.out.println("logger factory: " + LoggerService.INSTANCE.getFactory());

        Log4LibGDXLogger.Log4LibGDXLoggerFactory logFactory = new Log4LibGDXLogger.Log4LibGDXLoggerFactory();

        //LoggerService
        LoggerService.INSTANCE.clearLoggersCache();
        LoggerService.INSTANCE.setFactory(logFactory);
        //logFactory.newLogger()
        System.out.println("logger factory2: " + LoggerService.INSTANCE.getFactory());
        //Only turn log on error
        LoggerService.disable();
        LoggerService.error(true);

        //return logFactory.newLogger();
        throw new GdxRuntimeException("Cannot use Utils on non Log4LibGDXLogger logger.");
    }

    public static void setError(Logger logger){
        validateLogger(logger).setError();
    }

    public static void setDebug(Logger logger){
        validateLogger(logger).setDebug();
    }

    public static void setInfo(Logger logger){
        validateLogger(logger).setInfo();
    }

    public static int getLoggerLevel(Logger logger){
        return validateLogger(logger).getLoggerLevel();
    }
    @Before
    public void startUp(){
        //LoggerService
        //Log4LibGDXLogger.Log4LibGDXLoggerFactory logFactory = new Log4LibGDXLogger.Log4LibGDXLoggerFactory();

        //LoggerService.INSTANCE.setFactory(logFactory);
        //LoggerService.disable();
    }

    @Test
    public void enterTest() throws Exception {
        String methodName = "initialize";
        logger.enter(methodName);
    }

    @Test
    public void exitTest() throws Exception {
        String methodName = "initialize";
        logger.exit(methodName);
    }

    @Test
    public void setErrorTest() throws Exception {
        //Test default
        Assert.assertEquals(getLoggerLevel(logger), Application.LOG_ERROR);
        Assert.assertEquals(logger.isErrorOn(), false);

        //Test setter
        setError(logger);
        Assert.assertEquals(getLoggerLevel(logger), Application.LOG_ERROR);
        Assert.assertEquals(logger.isErrorOn(), false);
        Assert.assertEquals(logger.isInfoOn(), false);
        Assert.assertEquals(logger.isDebugOn(), false);

        LoggerService.error(true);
        Assert.assertEquals(logger.isErrorOn(), true);
        Assert.assertEquals(logger.isInfoOn(), false);
        Assert.assertEquals(logger.isDebugOn(), false);

        printTestMessages(logger);
        logger.error(this);
        logger.error("This is an error");
        logger.error("This is an tokenized {} error");
        logger.error("This is an tokenized error w/ {}", "inputs");
    }

    @Test
    public void setInfoTest() throws Exception {
        //Test default
        Assert.assertEquals(getLoggerLevel(logger), Application.LOG_ERROR);
        Assert.assertEquals(logger.isErrorOn(), false);

        //Test setter
        setInfo(logger);
        Assert.assertEquals(getLoggerLevel(logger), Application.LOG_INFO);
        Assert.assertEquals(logger.isErrorOn(), false);
        Assert.assertEquals(logger.isInfoOn(), false);
        Assert.assertEquals(logger.isDebugOn(), false);

        LoggerService.info(true);
        Assert.assertEquals(logger.isErrorOn(), false);
        Assert.assertEquals(logger.isInfoOn(), true);
        Assert.assertEquals(logger.isDebugOn(), false);

        printTestMessages(logger);
    }

    @Test
    public void setDebugTest() throws Exception {
        //Test default
        Assert.assertEquals(getLoggerLevel(logger), Application.LOG_ERROR);
        Assert.assertEquals(logger.isErrorOn(), false);

        //Test setter
        setDebug(logger);
        Assert.assertEquals(getLoggerLevel(logger), Application.LOG_DEBUG);
        Assert.assertEquals(logger.isErrorOn(), false);
        Assert.assertEquals(logger.isInfoOn(), false);
        Assert.assertEquals(logger.isDebugOn(), false);

        LoggerService.debug(true);
        Assert.assertEquals(logger.isErrorOn(), false);
        Assert.assertEquals(logger.isInfoOn(), false);
        Assert.assertEquals(logger.isDebugOn(), true);

        printTestMessages(logger);
    }

    private void printTestMessages(Logger logger){
        logger.debug("Test debug");
        logger.info("Test info");
        logger.error("Test error");
    }
}