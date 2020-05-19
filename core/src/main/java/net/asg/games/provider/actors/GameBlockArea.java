package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SnapshotArray;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.utils.Util;

public class GameBlockArea extends Stack {
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

    private Skin skin;

    private Table grid;
    private Table bgNumber;

    private GamePiece gamePiece;
    private GameJoinWindow joinWindow;

    private ObjectMap<String, GameBlock> uiBlocks;
    private Array<Actor> actors;

    private Sprite pieceSprite;

    public GameBlockArea(Skin skin) {
        this.skin = skin;
        init();
        joinWindow = new GameJoinWindow(skin);
        //joinButton.clearChildren();
        //joinDialog.button("Join").setDebug(false);
        //joinDialog.set
        //joinDialog.show(factory.getUserInterfaceService().getStage());
        //add(joinWindow);this
        //System.out.println("Adding block " + this.getName() + "w: " + this.getWidth() + " h: " + this.getHeight());
    }

    private void init(){
        initializeBoard();
        initializeSize();
        initializeBackground();
        initializeGrid();
    }

    private void initializeSize(){
        GameBlock clear = getClearBlock();
        float width = clear.getWidth() * YokelGameBoard.MAX_COLS;
        float height = clear.getHeight() * YokelGameBoard.MAX_PLAYABLE_ROWS;

        setSize(width, height);
        this.setBounds(0, 0, width, height);
        grid.setBounds(0, 0, width, height);
        setCullingArea(new Rectangle(0, 0, width, height));
        pieceSprite = new Sprite();
    }

    private void initializeBoard(){
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
        for(int r = YokelGameBoard.MAX_PLAYABLE_ROWS - 1; r >= 0; r--){
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
        return Util.getBlock(YokelBlock.CLEAR_BLOCK, isPreview);
    }

    private void initializeBackground(){
        //Skin skin = getSkin();
        this.setColor(ACTIVE_BACKGROUND_COLOR);

        bgNumber = new Table(skin);
        bgNumber.align(Align.top);
        bgNumber.setBounds(0, 0, getWidth(), getWidth());
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
        this.bgNumber.setBackground(number + GameClock.DIGIT_NME);
        this.boardNumber = number;
    }

    private void setBlock(int block, int r, int c){
        GameBlock uiCell = uiBlocks.get(getCellAttrName(r, c));

        if(uiCell != null){
            GameBlock blockUi = Util.getBlock(block, isPreview);
            if(blockUi != null){
                uiCell.setImage(blockUi.getImage());
            }
            //factory.freeObject(blockUi);
        }
    }

    public boolean isPreview(){
        return isPreview;
    }

    public void setGamePiece(YokelPiece gamePiece){
        if(gamePiece != null){
            if(this.gamePiece == null){
                this.gamePiece = new GamePiece(skin);
            }
            this.gamePiece.setData(gamePiece.toString());
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
        //setUpBoard();
        //invalidate();
        //init(getFactory());
        update();
    }

    private void setUpBoard(){
        clearChildren();
        SnapshotArray<Actor> cells = grid.getChildren();
        grid.clearChildren();
        for(Actor cell : cells){
            grid.add(cell);
        }
        add(bgNumber);
        add(grid);
        update();
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
        if(board != null){
            int[][] cells = board.getCells();
            for(int r = 0; r < YokelGameBoard.MAX_ROWS; r++){
                for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                    setBlock(cells[r][c], r, c);
                }
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
}