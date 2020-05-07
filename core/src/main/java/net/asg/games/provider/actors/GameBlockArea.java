package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SnapshotArray;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.utils.Util;

public class GameBlockArea extends Table {
    private static final String CELL_ATTR = "uiCell";
    private static final String CELL_ATTR_SEPARATOR = "_";
    private static final Color ACTIVE_BACKGROUND_COLOR = new Color(0.7f, 0.7f, 0.7f, 1);
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0.3f, 0.3f, 0.3f, 1);

    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;

    private boolean isSpeedDown;
    private boolean isActive;
    private boolean isPreview;

    private int boardNumber;
    private YokelGameBoard board;

    private Table grid;
    private Table bgNumber;

    private GamePiece gamePiece;

    private GameJoinWindow joinWindow;

    private YokelObjectFactory factory;
    private ObjectMap<String, GameBlock> uiBlocks;
    private Array<Actor> actors;

    public GameBlockArea(YokelObjectFactory factory) {
        super(factory.getUserInterfaceService().getSkin());

        reset(factory);
        joinWindow = new GameJoinWindow(getSkin());
        //joinButton.clearChildren();
        //joinDialog.button("Join").setDebug(false);
        //joinDialog.set
        //joinDialog.show(factory.getUserInterfaceService().getStage());
        add(joinWindow);
    }

    private void reset(YokelObjectFactory factory){
        //initializeSize();
        initializeBoard(factory);
        initializeBackground();
        initializeGrid();
    }

    private void initializeSize(){
        GameBlock clear = getClearBlock();
        float width = clear.getWidth() * YokelGameBoard.MAX_COLS;
        float height = clear.getHeight() * YokelGameBoard.MAX_ROWS;
        float cullHeight = clear.getHeight() * YokelGameBoard.MAX_PLAYABLE_ROWS;
        setSize(width, height);
        Rectangle cullingArea = new Rectangle();
        cullingArea.setWidth(width);
        cullingArea.setHeight(cullHeight);
        setCullingArea(cullingArea);
        invalidate();
    }

    private void initializeBoard(YokelObjectFactory factory){
        if (factory == null){
            throw new GdxRuntimeException("YokelObjectFactory cannot be null.");
        }

        this.factory = factory;
        Skin skin = factory.getUserInterfaceService().getSkin();
        setSkin(skin);
        this.uiBlocks = new ObjectMap<>();
        this.actors = new Array<>();
        this.boardNumber = 0;
        this.grid = new Table(skin);
        this.bgNumber = new Table(skin);
    }

    public void setDebug (boolean enabled) {
        super.setDebug(Util.setDebug(enabled, grid, bgNumber));
    }

    private void initializeGrid(){
        for(int r = YokelGameBoard.MAX_ROWS - 1; r >= 0; r--){
            for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                GameBlock uiBlock = getClearBlock();

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

    private GameBlock getClearBlock(){
        return factory.getGameBlock(YokelBlock.CLEAR_BLOCK, isPreview);
    }

    private void initializeBackground(){
        Skin skin = getSkin();
        this.setColor(ACTIVE_BACKGROUND_COLOR);

        bgNumber = new Table(skin);
        bgNumber.align(Align.top);
        setBackground(GameClock.NO_DIGIT_NME);
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
        setBackground(number + GameClock.DIGIT_NME);
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

    /*
    @Override
    public void setPosition(float x, float y){
        super.setPosition(x, y);
        boarder.setPosition(x, y);
        grid.setPosition(x, y);
        gamePiece.setPosition(x, y);
        bgNumber.setPosition(x, y);
    }*/

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
        //joinWindow.setPosition(getX(), getY());
        //Move game piece down
        moveGamePiece(delta);
        if(this.gamePiece != null){
            System.out.println("(" + this.gamePiece.getX() + "," + this.gamePiece.getY() + ")");
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        super.draw(batch, alpha);
        //joinWindow.setPosition(getX(), getY() / 2);
        //if(!isActive) return;

        //drawGamePiece(batch, alpha);
        //drawSprites(batch, alpha);
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

    /*public Skin getSkin() {
        return this.skin;
    }*/

    public YokelObjectFactory getFactory(){
        return factory;
    }
}