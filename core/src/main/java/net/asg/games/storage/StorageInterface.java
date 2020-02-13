package net.asg.games.storage;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;

public interface StorageInterface extends Disposable {
    void putTableState(YokelTable table);
    YokelTable getTableState();
    void putRegisteredPlayer(YokelPlayer player) throws Exception;
    YokelPlayer getRegisteredPlayer(String playerId);
    void putLounge(YokelLounge lounge) throws Exception;
    YokelLounge getLounge(String lounge);
    ObjectMap.Values<YokelLounge> getAllLounges();
    void addGame(String id, GameManager game);
    GameManager getGame(String gameId);
    ObjectMap.Values<GameManager> getAllGames();
}