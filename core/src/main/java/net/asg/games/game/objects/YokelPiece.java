package net.asg.games.game.objects;

public class YokelPiece extends AbstractYokelObject {
    private int index;
    private int[] cells;
    public int row;
    public int column;

    //Empty Contructor required for Json.Serializable
    public YokelPiece(){}

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

    public int getBlock1(){
        return cells[2];
    }

    public int getBlock2(){
        return cells[1];
    }

    public int getBlock3(){
        return cells[0];
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    @Override
    public void dispose() {}

    public void setPosition(int r, int c) {
        if(r < 0) throw new RuntimeException("Row value cannot be less than zero!");
        if(c < 0) throw new RuntimeException("Column value cannot be less than zero!");
        this.row = r;
        this.column = c;
    }
}