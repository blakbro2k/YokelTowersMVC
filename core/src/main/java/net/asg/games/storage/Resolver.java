package net.asg.games.storage;

import net.asg.games.game.objects.YokelObject;

public interface Resolver {
    /** Gets Object given name */
    <T extends YokelObject> T getObjectByName(Class<T> clazz, String name);

    /** Gets Object given id */
    <T extends YokelObject> T getObjectById(Class<T> clazz, String id);
}
