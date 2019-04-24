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
    private boolean isActive;
    private int[][] blocks;
    private YokelGameBoard board;
    private GameBlock[][] uiBlocks;

    private int boardNumber;
    private GamePiece currentPiece;
    private YokelObjectFactory factory;

    public GameBlockArea(YokelObjectFactory factory) {
        if (factory == null){
            throw new GdxRuntimeException("YokelObjectFactory cannot be null.");
        }
        Skin skin = factory.getUserInterfaceService().getSkin();
        if (skin == null){
            throw new GdxRuntimeException("skin cannot be null.");
        }
        setSkin(skin);
        setSize(getPrefWidth(), getPrefHeight());
        initializeBoard(0, factory);
        this.add(factory.getGameBlock(YokelBlock.CLEAR));
        this.add(factory.getGameBlock(YokelBlock.ATTACK_E));
        this.add(factory.getGameBlock(YokelBlock.DEFENSE_EX));
    }

    @Override
    public float getPrefWidth() {
        GameBlock actor = null;
        if(factory != null) actor = factory.getGameBlock(YokelBlock.CLEAR);
        if (actor != null) return actor.getWidth() * YokelGameBoard.MAX_WIDTH;
        return 16f;
    }

    @Override
    public float getPrefHeight() {
        GameBlock actor = null;
        if(factory != null) actor = factory.getGameBlock(YokelBlock.CLEAR);
        if (actor != null) return actor.getHeight() * YokelGameBoard.MAX_HEIGHT;
        return 16f;
    }

    private void initializeBoard(int boardNumber, YokelObjectFactory factory){
        this.blocks = new int[YokelGameBoard.MAX_WIDTH][ YokelGameBoard.MAX_HEIGHT];
        this.uiBlocks = new GameBlock[YokelGameBoard.MAX_WIDTH][YokelGameBoard.MAX_HEIGHT];
        this.boardNumber = boardNumber;
        this.factory = factory;
    }

    public int getBoardNumber(){
        return this.boardNumber;
    }

    public void setBoardNumber(int number){
        this.boardNumber = number;
    }

    private void setBlock(int block, int row, int col){
        GameBlock blockUi = factory.getGameBlock(block);

        if(blockUi != null){
            uiBlocks[row][col] = blockUi;
        }
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
        super.draw(batch, parentAlpha);
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

    public void updateData(YokelGameBoard gameBoard) {
        if(gameBoard != null) {
            this.board = gameBoard;
            update();
        }
    }

    private void update(){
        for(int r = 0; r < YokelGameBoard.MAX_WIDTH; r++){
            for(int c = 0; c < YokelGameBoard.MAX_HEIGHT; c++){
                setBlock(blocks[r][c], r, c);
            }
        }
    }

    private void actOnBlock(int row, int col, float delta){
        if(!isActive) return;
        //update ui blocks
        GameBlock gameBlock = uiBlocks[row][col];

        if(gameBlock != null){
            gameBlock.act(delta);

            // move down one space
            // if speed down move down more spaces
            //if not broken, act
            //if broken, interpolate down
        }
    }
}







