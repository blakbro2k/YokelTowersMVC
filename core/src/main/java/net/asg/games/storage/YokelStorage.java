package net.asg.games.storage;

import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;

/** Interface for Yokel Object Resource Storage.
 * @author blakbro2k */
public interface YokelStorage {

    /** Puts a lounge into storage. */
    void putLounge(YokelLounge lounge) throws Exception;

    /** Releases all resources of this object. */
    YokelLounge getLounge(String nameOrId);

    /** Puts a lounge into storage. */
    void putRoom(YokelRoom room) throws Exception;

    /** Releases all resources of this object. */
    YokelRoom getRoom(String nameOrId);

    /** Puts a lounge into storage. */
    void putTable(YokelTable table) throws Exception;

    /** Releases all resources of this object. */
    YokelTable getTable(String nameOrId);

    /** Puts a lounge into storage. */
    void putSeat(YokelSeat lounge) throws Exception;

    /** Releases all resources of this object. */
    YokelSeat getSeat(String nameOrId);

    /** Puts a lounge into storage. */
    void putPlayer(YokelPlayer player) throws Exception;

    /** Releases all resources of this object. */
    YokelPlayer getPlayer(String nameOrId);

    /** Releases all resources of this object. */
    ObjectMap.Values<YokelLounge> getAllLounges();

    /** Releases all resources of this object. */
    void putGame(String id, GameManager game);

    /** Releases all resources of this object. */
    GameManager getGame(String gameId);

    /** Releases all resources of this object. */
    ObjectMap.Values<GameManager> getAllGames();
}