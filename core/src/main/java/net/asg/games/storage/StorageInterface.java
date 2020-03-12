package net.asg.games.storage;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;

/** Interface for Storage resources.
 * @author blakbro2k */
public interface StorageInterface extends Disposable {
    /** Put the Table State */
    void putTableState(YokelTable table);

    /** Releases all resources of this object. */
    YokelTable getTableState();

    /** Register Player. */
    /** Must link clientID to Player ID to Player Object. */
    void putRegisteredPlayer(String clientId, YokelPlayer player) throws Exception;

    /** Get Registered Player. */
    YokelPlayer getRegisteredPlayer(String playerId);

    /** Remove Registered Player. */
    /** Should remove the from all tables, seats, games and rooms **/
    void removeRegisteredPlayer(String clientID) throws Exception;

    /** Releases all resources of this object. */
    ObjectMap.Values<YokelPlayer> getAllRegisteredPlayers();

    /** Puts a lounge into storage. */
    void putLounge(YokelLounge lounge) throws Exception;

    /** Releases all resources of this object. */
    YokelLounge getLounge(String lounge);

    /** Releases all resources of this object. */
    ObjectMap.Values<YokelLounge> getAllLounges();

    /** Releases all resources of this object. */
    void addGame(String id, GameManager game);

    /** Releases all resources of this object. */
    GameManager getGame(String gameId);

    /** Releases all resources of this object. */
    ObjectMap.Values<GameManager> getAllGames();

    /** Check if client id is registered **/
    boolean isClientRegistered(String clientId);
}