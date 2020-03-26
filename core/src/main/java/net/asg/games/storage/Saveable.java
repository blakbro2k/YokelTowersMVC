package net.asg.games.storage;

public interface Saveable {
    /** Put the Table State */
    void saveObject(Object object);

    /** Releases all resources of this object. */
    void commitTransactions();

    /** Releases all resources of this object. */
    void rollTransactions();
}
