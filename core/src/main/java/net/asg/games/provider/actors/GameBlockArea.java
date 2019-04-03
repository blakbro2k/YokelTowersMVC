package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelObjectFactory;

public class GameBlockArea extends Table {
    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;

    private boolean isSpeedDown;
    private int[][] blocks;
    private GameBlock[][] uiBlocks;

    private int boardNumber;
    private GamePiece currentPiece;
    private YokelObjectFactory factory;

    public GameBlockArea(int boardNumber, Skin skin, YokelObjectFactory factory) {
        if (boardNumber < 1 || boardNumber > 8){
            throw new GdxRuntimeException("Board number must be 0 < x < 9");
        }
        if (factory == null){
            throw new GdxRuntimeException("YokelObjectFactory cannot be null.");
        }
        if (skin == null){
            throw new GdxRuntimeException("skin cannot be null.");
        }
        setSkin(skin);
        initializeBoard(boardNumber, factory);
    }

    private void initializeBoard(int boardNumber, YokelObjectFactory factory){
        this.blocks = new int[YokelGameBoard.MAX_WIDTH][ YokelGameBoard.MAX_HEIGHT];
        this.uiBlocks = new GameBlock[YokelGameBoard.MAX_WIDTH][YokelGameBoard.MAX_HEIGHT];
        this.boardNumber = boardNumber;
        this.factory = factory;
    }

    private void setBlock(int block, int row, int col){

    }

    @Override
    public void act(float delta){
        for(int r = 0; r < YokelGameBoard.MAX_WIDTH; r++){
            for(int c = 0; c < YokelGameBoard.MAX_HEIGHT; c++){
                actOnBlock(r, c, delta);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        for(int r = 0; r < YokelGameBoard.MAX_WIDTH; r++){
            for(int c = 0; c < YokelGameBoard.MAX_HEIGHT; c++){
                drawBlock(r, c, batch, parentAlpha);
            }
        }
    }

    private void drawBlock(int r, int c, Batch batch, float parentAlpha) {
        GameBlock uiBlock = uiBlocks[r][c];

        if(uiBlock != null){
            uiBlock.draw(batch,parentAlpha);
        }
    }

    public void updateBlocks(int[][] blocks) {
        if(blocks != null && blocks.length == YokelGameBoard.MAX_WIDTH * YokelGameBoard.MAX_HEIGHT) {
            this.blocks = blocks;
        }
    }

    private void actOnBlock(int row, int col, float delta){
        //update ui blocks
        GameBlock gameBlock = uiBlocks[row][col];

        if(gameBlock != null){
             //=
        }
        //if not broken, act
        //if broken, interpolate down
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