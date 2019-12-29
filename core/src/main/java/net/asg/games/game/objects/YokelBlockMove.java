package net.asg.games.game.objects;

import java.util.Vector;

public class YokelBlockMove extends AbstractYokelObject {
    public int x;
    public int y;
    private int block;

    public YokelBlockMove(int x, int y, int block){
        this.x = x;
        this.y = y;
        this.block = block;
    }

    @Override
    public void dispose() {

    }
}
