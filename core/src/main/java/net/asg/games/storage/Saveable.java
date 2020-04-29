package net.asg.games.storage;

import net.asg.games.game.objects.YokelObject;

public interface Saveable {
    /** Add Object to transaction list */
    void saveObject(YokelObject object);

    /** Commit transactions */
    void commitTransactions();

    /** Delete transactions */
    void rollTransactions();
}
