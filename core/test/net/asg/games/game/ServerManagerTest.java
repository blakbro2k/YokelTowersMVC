package net.asg.games.game;

import net.asg.games.server.YokelLounge;
import net.asg.games.server.YokelRoom;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerManagerTest {
    ServerManager manager;

    @Before
    public void createLounge() {
        manager = new ServerManager();
        YokelLounge socialLounge = new YokelLounge(YokelLounge.SOCIAL_GROUP);
        //YokelRoom room1 = new YokelRoom("Eiffel Tower");
    }

    @Test
    public void createLounges() throws Exception{
        throw new Exception("Test case is not initialized.");
    }

    @Test
    public void getLounges() throws Exception{
        throw new Exception("Test case is not initialized.");
    }
}