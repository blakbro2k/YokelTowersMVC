package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelObjectFactory;

public class GameBlockArea extends Table {
    private static final String CELL_ATTR = "uiCell";
    private static final String CELL_ATTR_SEPARATOR = "_";

    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;

    private boolean isSpeedDown;
    private boolean isActive;
    private YokelGameBoard board;
    private Table blockAll;

    private int boardNumber;
    private GamePiece currentPiece;
    private YokelObjectFactory factory;
    private ObjectMap<String, GameBlock> uiBlocks;

    public GameBlockArea(YokelObjectFactory factory) {
        if (factory == null){
            throw new GdxRuntimeException("YokelObjectFactory cannot be null.");
        }
        Skin skin = factory.getUserInterfaceService().getSkin();
        if (skin == null){
            throw new GdxRuntimeException("skin cannot be null.");
        }
        initializeBoard(factory);
        setSkin(skin);
        setSize(getPrefWidth(), getPrefHeight());
    }

    private void initializeBoard(YokelObjectFactory factory){
        this.factory = factory;
        uiBlocks = new ObjectMap<>();
        initilizeUiCells();
        this.boardNumber = 0;
    }

    private void initilizeUiCells(){
        for(int r = YokelGameBoard.MAX_ROWS - 1; r >= 0; r--){
            for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                GameBlock uiBlock = factory.getGameBlock(YokelBlock.CLEAR);

                uiBlocks.put(getCellAttrName(r,c), uiBlock);
                if(c + 1 == YokelGameBoard.MAX_COLS){
                    add(uiBlock).row();
                } else {
                    add(uiBlock);
                }
            }
        }
    }

    private String getCellAttrName(int r, int c){
        return CELL_ATTR + CELL_ATTR_SEPARATOR + r + CELL_ATTR_SEPARATOR + c;
    }

    public int getBoardNumber(){
        return this.boardNumber;
    }

    public void setBoardNumber(int number){
        this.boardNumber = number;
    }

    private void setBlock(int block, int r, int c){
        GameBlock uiCell = uiBlocks.get(getCellAttrName(r, c));

        if(uiCell != null){
            //factory.freeObject(uiCell);
            GameBlock blockUi = factory.getGameBlock(block);
            if(blockUi != null){
                uiCell.setImage(blockUi.getImage());
            }
        }
    }

    @Override
    public void act(float delta){
        for(int r = 0; r < YokelGameBoard.MAX_ROWS; r++){
            for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                actOnBlock(r, c, delta);
            }
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        //if(!isActive) return;
        super.draw(batch, alpha);
    }

    public void updateData(YokelGameBoard gameBoard) {
        if(gameBoard != null) {
            this.board = gameBoard;
            update();
        }
    }

    private void update(){
        int[][] cells = board.getCells();
        for(int r = 0; r < YokelGameBoard.MAX_ROWS; r++){
            for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                setBlock(cells[r][c], r, c);
            }
        }
    }

    private void actOnBlock(int r, int c, float delta){
        //if(!isActive) return;
        //update ui blocks
        GameBlock uiBlock = uiBlocks.get(getCellAttrName(r,c));

        if(uiBlock != null){
            uiBlock.act(delta);

            // move down one space
            // if speed down move down more spaces
            //if not broken, act
            //if broken, interpolate down
        }
    }
}







