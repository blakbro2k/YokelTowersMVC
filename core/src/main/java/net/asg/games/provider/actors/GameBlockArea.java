package net.asg.games.provider.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GameBlockArea extends Table {
    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;

    private boolean isSpeedDown;
    private Array<GameBlock> blocks;

    private int boardNumber;
    private GamePiece currentPiece;
    private Rectangle bounds;


    public GameBlockArea(int boardNumber) {
        if (boardNumber < 1 || boardNumber > 8){
            throw new GdxRuntimeException("Board number must be 0 < x < 9");
        }

    }

    private void initializeBoard(){

    }
/*
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < YokelGameBoard.MAX_ROWS; i++){
            for(int j = 0; j < YokelGameBoard.MAX_COLS; j++){
                ret.append("|").append(drawableBoard[i][j]);
            }
            ret.append("|\n");
        }
        return ret.toString();
    }
    */
}