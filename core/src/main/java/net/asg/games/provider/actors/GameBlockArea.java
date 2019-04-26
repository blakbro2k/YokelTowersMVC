package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelObjectFactory;
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
    private YokelGameBoard board;
    private Table boarder;

    private int boardNumber;
    private GamePiece gamePiece;
    private YokelObjectFactory factory;
    private ObjectMap<String, GameBlock> uiBlocks;
    private Array<GameBlock> sprites;

    public GameBlockArea(YokelObjectFactory factory) {
        if (factory == null){
            throw new GdxRuntimeException("YokelObjectFactory cannot be null.");
        }
        Skin skin = factory.getUserInterfaceService().getSkin();
        if (skin == null){
            throw new GdxRuntimeException("skin cannot be null.");
        }
        setSkin(skin);

        boarder = new Table();
        boarder.setDebug(this.getDebug());
        boarder.setSize(getWidth() + 20, getHeight() + 20);
        boarder.setBounds(this.getX(), this.getY(), getWidth() - 8, getHeight() - 8);
        this.setColor(DEFAULT_BACKGROUND_COLOR);

        //add(boarder);

        NinePatch patch = new NinePatch(getSkin().getRegion("8_digit"),3, 3, 3, 3);
        NinePatchDrawable background = new NinePatchDrawable(patch);

        initializeBoard(factory);
    }

    private void initializeBoard(YokelObjectFactory factory){
        this.factory = factory;
        uiBlocks = new ObjectMap<>();
        this.sprites = new Array<>();
        this.boardNumber = 0;
        initializeUiCells();
    }

    private void initializeUiCells(){
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
        this.setBackground(number + "_digit");
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
        boarder.draw(batch, alpha);
        super.draw(batch, alpha);
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
        if(!Util.isArrayEmpty(sprites)){
            for(GameBlock sprite : sprites){
               if(sprite != null){
                   sprite.draw(batch, alpha);
               }
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







