package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SnapshotArray;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.utils.Util;

public class GameBlockArea extends Stack {
    private static final String CELL_ATTR = "uiCell";
    private static final String CELL_ATTR_SEPARATOR = "_";
    private static final Color ACTIVE_BACKGROUND_COLOR = new Color(0.7f, 0.7f, 0.7f, 1);
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0.3f, 0.3f, 0.3f, 1);

    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;

    private Skin skin;

    private boolean isSpeedDown;
    private boolean isActive;

    private int boardNumber;
    private YokelGameBoard board;

    private Table boarder;
    private Table grid;
    private GamePiece gamePiece;
    private Table bgNumber;

    private YokelObjectFactory factory;
    private ObjectMap<String, GameBlock> uiBlocks;
    private Array<Actor> actors;
    private boolean isPreview;

    public GameBlockArea(YokelObjectFactory factory) {
        super();
        reset(factory);
    }

    private void reset(YokelObjectFactory factory){
        //initializeSize();
        initializeBoard(factory);
        initializeBackground();
        initializeGrid();
    }

    private void initializeSize(){
        if(isPreview){
            setSize(48, 128);
        } else {
            setSize(96, 256);
        }
        invalidate();
    }

    private void initializeBoard(YokelObjectFactory factory){
        if (factory == null){
            throw new GdxRuntimeException("YokelObjectFactory cannot be null.");
        }

        this.factory = factory;
        this.skin = factory.getUserInterfaceService().getSkin();
        this.uiBlocks = new ObjectMap<>();
        this.actors = new Array<>();
        this.boardNumber = 0;
        this.grid = new Table(skin);
        this.bgNumber = new Table(skin);
    }

    public void setDebug (boolean enabled) {
        super.setDebug(enabled);
        Util.setDebug(enabled, boarder, grid, bgNumber);
    }
    
    private void initializeGrid(){
        for(int r = YokelGameBoard.MAX_ROWS - 1; r >= 0; r--){
            for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                GameBlock uiBlock = factory.getGameBlock(YokelBlock.CLEAR_BLOCK, isPreview);

                uiBlocks.put(getCellAttrName(r,c), uiBlock);
                if(c + 1 == YokelGameBoard.MAX_COLS){
                    grid.add(uiBlock).row();
                } else {
                    grid.add(uiBlock);
                }
            }
        }
        add(grid);
    }

    private void initializeBackground(){
        boarder = new Table();
        boarder.setSkin(skin);
        boarder.setWidth(getWidth());
        boarder.setHeight(getHeight());
        boarder.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        this.setColor(DEFAULT_BACKGROUND_COLOR);
        //add(boarder);

        bgNumber = new Table(skin);
        bgNumber.align(Align.top);
        bgNumber.setBackground(GameClock.NO_DIGIT_NME);
        //bgNumber.setScale(2);
        add(bgNumber);
    }

    private String getCellAttrName(int r, int c){
        return CELL_ATTR + CELL_ATTR_SEPARATOR + r + CELL_ATTR_SEPARATOR + c;
    }

    public int getBoardNumber(){
        return this.boardNumber;
    }

    public void setBoardNumber(int number){
        bgNumber.setBackground(number + GameClock.DIGIT_NME);
        this.boardNumber = number;
    }

    private void setBlock(int block, int r, int c){
        GameBlock uiCell = uiBlocks.get(getCellAttrName(r, c));

        if(uiCell != null){
            GameBlock blockUi = factory.getGameBlock(block, isPreview);
            if(blockUi != null){
                uiCell.setImage(blockUi.getImage());
            }
            //factory.freeObject(blockUi);
        }
    }

    public boolean isPreview(){
        return isPreview;
    }

    public void setGamePiece(GamePiece gamePiece){
        System.out.println("SetGamePiece");
        //printParentCoords();
        if(this.gamePiece == null && gamePiece != null){
            this.gamePiece = gamePiece;
            this.gamePiece.setDebug(true);
            this.gamePiece.setPosition(grid.getX() + (2 * gamePiece.getWidth()), grid.getY() + (16 * gamePiece.getWidth()));

            //System.out.println("gamePiece.. " + gamePiece);
            //Stage stage = getStage();
            //gamePiece.setX(getParent().getX());
            //gamePiece.setY(getParent().getY());

            //stage.addActor(gamePiece);
            //System.out.println(getMinWidth());
            //System.out.println(getMaxWidth());

            actors.add(this.gamePiece);
            //add(this.gamePiece);
        }
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        boarder.setPosition(x, y);
        grid.setPosition(x, y);
        gamePiece.setPosition(x, y);
        bgNumber.setPosition(x, y);
    }

    public void printParentCoords(){
        if(hasParent()){
            System.out.println("(" + getParent().getX() + "," + getParent().getY() + ")");
        }
    }


    public void setPreview(boolean isPreview){
        this.isPreview = isPreview;
        SnapshotArray<Actor> cells = grid.getChildren();
        grid.clearChildren();
        invalidate();
        reset(getFactory());
        //update();
        for(Actor cell : cells){
            grid.add(cell);
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);

        //Move game piece down
        moveGamePiece(delta);
        if(this.gamePiece != null){
            System.out.println("(" + this.gamePiece.getX() + "," + this.gamePiece.getY() + ")");
        }
    }

    /*
    @Override
    public void draw(Batch batch, float alpha){
        //if(!isActive) return;

        super.draw(batch, alpha);
        //drawGamePiece(batch, alpha);
        drawSprites(batch, alpha);
    }*/

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

    private void drawSprites(Batch batch, float alpha){
        if(Util.isArrayEmpty(actors)){
            for(Actor actor : actors){
               if(actor != null){
                   actor.draw(batch, alpha);
               }
            }
        }
    }

    private void drawGamePiece(Batch batch, float alpha){
        if(gamePiece != null){
            gamePiece.setX(getX());
            gamePiece.setY(getY());
            gamePiece.draw(batch, alpha);
        }
    }

    private void actOnBlocks(int r, int c, float delta){
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

    private void moveGamePiece(float delta) {
        if(this.gamePiece != null){
            attemptGamePieceMoveDown(delta);
        }
    }

    private void attemptGamePieceMoveDown(float delta) {
        if(gamePiece != null){

            if(gamePiece.getY() > 0){
                gamePiece.setY(gamePiece.getY() - 1);
                System.out.println("Grid:" + grid.getPadX() + "," + grid.getY() + ")");
            } else {
                //actors.removeValue(gamePiece, false);
                //gamePiece = null;
            }
        }
    }

    public Skin getSkin() {
        return this.skin;
    }

    public YokelObjectFactory getFactory(){
        return factory;
    }
}