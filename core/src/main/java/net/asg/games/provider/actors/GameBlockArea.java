package net.asg.games.provider.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import net.asg.games.game.objects.YokelBlock;

public class GameBlockArea extends Table {
    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;
    private static final int NUMBER_WIDTH_BLOCKS = 6;
    private static final int NUMBER_HEIGHT_BLOCKS = 16;

    private boolean isSpeedDown;
    private YokelBlock[] blocks;

    private int boardNumber;
    private GamePiece currentPiece;

    public GameBlockArea(int boardNumber) {
        if (boardNumber < 1 || boardNumber > 8){
            throw new GdxRuntimeException("Board number must be 0 < x < 9");
        }
        blocks = new YokelBlock[NUMBER_WIDTH_BLOCKS * NUMBER_HEIGHT_BLOCKS];
    }

    private void initializeBoard(){

    }

    public void updateBlocks(YokelBlock[] blocks) throws GdxRuntimeException {
        if(blocks == null) {
            throw new GdxRuntimeException("Blocks to update cannot be null");
        }
        if(blocks.length != NUMBER_WIDTH_BLOCKS * NUMBER_HEIGHT_BLOCKS){
            throw new GdxRuntimeException("blocks index mismatch.");
        }
        this.blocks = blocks;
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