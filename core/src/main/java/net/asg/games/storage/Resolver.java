package net.asg.games.storage;

public interface Resolver {
    <T> T getObjectByName(Class<T> clazz, String name);
    <T> T getObjectById(Class<T> clazz, String id);
    <T> T getObject(Class<T> clazz, String name);
}
