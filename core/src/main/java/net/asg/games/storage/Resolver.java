package net.asg.games.storage;

public interface Resolver {
    /** Gets Object given name */
    <T> T getObjectByName(Class<T> clazz, String name);

    /** Gets Object given id */
    <T> T getObjectById(Class<T> clazz, String id);
}
