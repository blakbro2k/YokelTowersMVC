package net.asg.games.game.objects;

import java.util.Arrays;

public class YokelPiece extends AbstractYokelObject {
    private int index;
    private int[] cells;

    public YokelPiece(int index){
        this(index, YokelBlock.Y_BLOCK, YokelBlock.O_BLOCK, YokelBlock.K_BLOCK);
    }

    public YokelPiece(int index, int block1, int block2, int block3){
        this.index = index;
        cells = new int[3];
        cells[0] = block1;
        cells[1] = block2;
        cells[2] = block3;
    }

    public int getValueAt(int i) {
        System.out.println("getting i=" + i);
        System.out.println(cells[0]);
        return cells[i];
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void dispose() {

    }
}
