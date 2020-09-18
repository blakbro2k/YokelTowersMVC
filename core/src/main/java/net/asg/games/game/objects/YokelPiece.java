package net.asg.games.game.objects;

public class YokelPiece extends AbstractYokelObject {
    private int index;
    private int[] cells;
    public int row;
    public int column;

    //Empty Constructor required for Json.Serializable
    public YokelPiece(){
        this(0,0,0,0);
    }

    public YokelPiece(int index, int block1, int block2, int block3){
        init();
        this.index = index;
        setBlock1(block1);
        setBlock2(block2);
        setBlock3(block3);
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

    public void setBlock1(int block){
        cells[2] = block;
    }

    public int getBlock2(){
        return cells[1];
    }

    public void setBlock2(int block){
        cells[1] = block;
    }

    public int getBlock3(){
        return cells[0];
    }

    public void setBlock3(int block){
        cells[0] = block;
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

    public void cycleDown() {
        int tempBlock1 = getBlock1();
        int tempBlock2 = getBlock2();
        int tempBlock3 = getBlock3();
        setBlock1(tempBlock3);
        setBlock2(tempBlock1);
        setBlock3(tempBlock2);
    }

    public void cycleUp() {
        int tempBlock1 = getBlock1();
        int tempBlock2 = getBlock2();
        int tempBlock3 = getBlock3();
        setBlock1(tempBlock2);
        setBlock2(tempBlock3);
        setBlock3(tempBlock1);
    }
}