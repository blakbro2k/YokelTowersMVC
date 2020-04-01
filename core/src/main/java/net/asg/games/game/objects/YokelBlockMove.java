package net.asg.games.game.objects;

public class YokelBlockMove extends AbstractYokelObject {
    public int x;
    public int y;
    public int block;

    //Empty Contructor required for Json.Serializable
    public YokelBlockMove(){}

    public YokelBlockMove(int x, int y, int block){
        this.x = x;
        this.y = y;
        this.block = block;
    }

    @Override
    public void dispose() {}
}
