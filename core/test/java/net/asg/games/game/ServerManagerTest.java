package net.asg.games.game;

import net.asg.games.game.objects.YokelLounge;

import org.junit.Test;

public class ServerManagerTest {
    ServerManager manager;
    @Test
    public void createLounge() {
        manager = new ServerManager();
        YokelLounge socialLounge = new YokelLounge(YokelLounge.SOCIAL_GROUP);
        //YokelRoom room1 = new YokelRoom("Eiffel Tower");
    }
}