package net.asg.games;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.websocket.WebSocket;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.managers.ServerManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.storage.MemoryStorage;
import net.asg.games.storage.YokelMemoryStorage;
import net.asg.games.storage.YokelStorage;
import net.asg.games.storage.YokelStorageAdapter;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.TestingUtils;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class StorageTest {
    private static MemoryStorage storage;

    @BeforeClass
    public static void setUp() {
        storage = new YokelMemoryStorage();
    }

    @AfterClass
    public static void tearDown() {
        storage.dispose();
    }

    @Test
    public void testGetKey() throws Exception {
        YokelPlayer player = new YokelPlayer();

        String expectedId = player.getId();
        String expectedName = "expectedName";
        Assert.assertEquals(expectedId, storage.getKey(player));
        player.setName(expectedName);
        Assert.assertEquals(expectedName, storage.getKey(player));

    }
}