package net.asg.games.game.objects;

public interface Nameable {
    void setName(String name);

    String getName();

    boolean isNameSame(Object o);
}
