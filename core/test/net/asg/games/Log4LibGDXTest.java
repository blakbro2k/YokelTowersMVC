package net.asg.games;

import com.badlogic.gdx.Application;
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
        Assert.assertEquals(YokelUtilities.getLoggerLevel(logger), Application.LOG_ERROR);
        Assert.assertEquals(logger.isErrorOn(), false);

        //Test setter
        YokelUtilities.setError(logger);
        Assert.assertEquals(YokelUtilities.getLoggerLevel(logger), Application.LOG_ERROR);
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
        Assert.assertEquals(YokelUtilities.getLoggerLevel(logger), Application.LOG_ERROR);
        Assert.assertEquals(logger.isErrorOn(), false);

        //Test setter
        YokelUtilities.setInfo(logger);
        Assert.assertEquals(YokelUtilities.getLoggerLevel(logger), Application.LOG_INFO);
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
        Assert.assertEquals(YokelUtilities.getLoggerLevel(logger), Application.LOG_ERROR);
        Assert.assertEquals(logger.isErrorOn(), false);

        //Test setter
        YokelUtilities.setDebug(logger);
        Assert.assertEquals(YokelUtilities.getLoggerLevel(logger), Application.LOG_DEBUG);
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