package net.asg.games.storage;

import net.asg.games.game.objects.YokelObject;

public interface Saveable<T> {
    /** Add Object to transaction list */
    void saveObject(T object);

    /** Commit transactions */
    void commitTransactions();

    /** Delete transactions */
    void rollTransactions();
}
