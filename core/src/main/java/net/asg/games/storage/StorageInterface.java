package net.asg.games.storage;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;

public interface StorageInterface {
    void putTableState(YokelTable table);
    YokelTable getTableState();
    void putRegisteredPlayer(YokelPlayer player);
    YokelPlayer getRegisteredPlayer();
    void putLounge(YokelLounge lounge);
    YokelLounge getLounge();
}