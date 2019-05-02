package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

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
    private final Skin skin;

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

    public GameBlockArea(YokelObjectFactory factory) {
        if (factory == null){
            throw new GdxRuntimeException("YokelObjectFactory cannot be null.");
        }
        Skin skin = factory.getUserInterfaceService().getSkin();
        if (skin == null){
            throw new GdxRuntimeException("skin cannot be null.");
        }
        this.skin = skin;

        initializeBoard(factory);
        initializeBackground();
        initializeGrid();
    }

    private void initializeBoard(YokelObjectFactory factory){
        this.factory = factory;
        this.uiBlocks = new ObjectMap<>();
        this.actors = new Array<>();
        this.boardNumber = 0;
        this.grid = new Table(skin);
        this.bgNumber = new Table(skin);
    }

    public void setDebug (boolean enabled) {
        boarder.setDebug(enabled);
        grid.setDebug(enabled);
        //gamePiece.setDebug(enabled);
        bgNumber.setDebug(enabled);
        super.setDebug(enabled);
    }


    private void initializeGrid(){
        for(int r = YokelGameBoard.MAX_ROWS - 1; r >= 0; r--){
            for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                GameBlock uiBlock = factory.getGameBlock(YokelBlock.CLEAR);

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
        boarder.setBounds(this.getX(), this.getY(), getWidth(), getHeight());
        this.setColor(DEFAULT_BACKGROUND_COLOR);
        add(boarder);

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
            GameBlock blockUi = factory.getGameBlock(block);
            if(blockUi != null){
                uiCell.setImage(blockUi.getImage());
            }
            //factory.freeObject(blockUi);
        }
    }

    public void setGamePiece(GamePiece gamePiece){
        if(this.gamePiece == null && gamePiece != null){
            System.out.println("SetGamePiece");
            //System.out.println("gamePiece.. " + gamePiece);
            //Stage stage = getStage();
            gamePiece.setX(getX());
            gamePiece.setY(getY());

            //stage.addActor(gamePiece);
            //System.out.println("stage:" + stage);
            this.gamePiece = gamePiece;
            actors.add(gamePiece);
            add(gamePiece);
            System.out.println("stage:" + Util.printBounds(gamePiece));
System.out.println(this);

        }
    }


    @Override
    public void act(float delta){
        super.act(delta);

        //Move game piece down
        moveGamePiece(delta);
    }

    @Override
    public void draw(Batch batch, float alpha){
        //if(!isActive) return;

        super.draw(batch, alpha);
        //drawGamePiece(batch, alpha);
        drawSprites(batch, alpha);
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
        if(!Util.isArrayEmpty(actors)){
            for(Actor actor : actors){
               if(actor != null){
                   //System.out.println("Drawing.. " + actor);

                   //System.out.println(Util.printBounds(actor));


                   //stage.draw();
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

            if(gamePiece.getY() < 0){
                gamePiece.setY(gamePiece.getY() - 1);
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







