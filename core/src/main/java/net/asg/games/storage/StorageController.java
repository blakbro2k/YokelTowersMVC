package net.asg.games.storage;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.objects.YokelPlayer;

public interface StorageController extends Disposable, Saveable, Resolver {
    /** Player Controls
     *  Methods control the flow of a player and client*/
    /** Must link clientID to Player ID to Player Object. */
    void putRegisteredPlayer(String clientId, YokelPlayer player) throws Exception;

    /** Get Registered Player. */
    YokelPlayer getRegisteredPlayer(String playerId);

    /** Remove Registered Player. */
    /** Should remove the from all tables, seats, games and rooms **/
    void removeRegisteredPlayer(String clientID) throws Exception;

    /** Gets all registered players. */
    ObjectMap.Values<YokelPlayer> getAllRegisteredPlayers();

    /** Check if client id is registered **/
    boolean isClientRegistered(String clientId);

    /** Check if client id is registered **/
    //boolean isPlayerRegistered(String playerId);
}
