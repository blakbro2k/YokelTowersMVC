package net.asg.games.storage;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;

/** Interface for Storage resources.
 * @author blakbro2k */
public interface YokelStorage extends StorageController {
    /** Puts a lounge into storage. */
    void putLounge(YokelLounge lounge) throws Exception;

    /** Releases all resources of this object. */
    YokelLounge getLounge(String lounge);

    /** Releases all resources of this object. */
    ObjectMap.Values<YokelLounge> getAllLounges();

    /** Releases all resources of this object. */
    void putGame(String id, GameManager game);

    /** Releases all resources of this object. */
    GameManager getGame(String gameId);

    /** Releases all resources of this object. */
    ObjectMap.Values<GameManager> getAllGames();
}