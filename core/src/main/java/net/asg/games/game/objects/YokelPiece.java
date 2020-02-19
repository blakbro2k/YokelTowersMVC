package net.asg.games.game.objects;

import java.util.Arrays;

public class YokelPiece extends AbstractYokelObject {
    private int index;
    private int[] cells;
    public int row;
    public int column;

    public YokelPiece(int index, int block1, int block2, int block3){
        init();
        this.index = index;
        this.cells[0] = block3;
        this.cells[1] = block2;
        this.cells[2] = block1;
    }

    public int getValueAt(int i) {
        return cells[i];
    }

    private void init(){
        if(this.cells == null){
            this.cells = new int[3];
        }
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void dispose() {

    }

    public void setPosition(int r, int c) {
        if(r < 0) throw new RuntimeException("Row value cannot be less than zero!");
        if(c < 0) throw new RuntimeException("Column value cannot be less than zero!");
        this.row = r;
        this.column = c;
    }

    public String toString(){
        return Arrays.toString(cells);
    }
}