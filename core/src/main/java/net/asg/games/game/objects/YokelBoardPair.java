package net.asg.games.game.objects;

public class YokelBoardPair {
    YokelGameBoard leftBoard;
    YokelGameBoard rightBoard;

    public YokelBoardPair(YokelGameBoard left, YokelGameBoard right){
        setLeftBoard(left);
        setRightBoard(right);
    }

    public void setLeftBoard(YokelGameBoard leftBoard) {
        this.leftBoard = leftBoard;
    }

    public void setRightBoard(YokelGameBoard rightBoard){
        this.rightBoard = rightBoard;
    }

    public YokelGameBoard getLeftBoard(){
        return leftBoard;
    }

    public YokelGameBoard getRightBoard() {
        return rightBoard;
    }
}
