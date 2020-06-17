package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.utils.UIUtil;
import net.asg.games.utils.YokelUtilities;

public class GameBlockArea extends Stack {
    private static final String CELL_ATTR = "uiCell";
    private static final String CELL_ATTR_SEPARATOR = "_";
    private static final String GRID_NAME = "grid";
    private static final String BOARD_NUMBER_NAME = "boardNumber";

    private static final Color ACTIVE_BACKGROUND_COLOR = new Color(0.7f, 0.7f, 0.7f, 1);
    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(0.3f, 0.3f, 0.3f, 1);

    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;

    private boolean isSpeedDown;
    private boolean isActive;
    private boolean isPreview;
    private boolean isPlayerView;

    private int boardNumber;
    private YokelGameBoard board;

    private Skin skin;

    private Table grid;
    private Table bgNumber;

    private GamePiece gamePiece;
    private GameJoinWindow joinWindow;

    private ObjectMap<String, GameBlock> uiBlocks;

    private PieceDrawable pieceSprite;

    public GameBlockArea(Skin skin, boolean isPreview) {
        this.skin = skin;
        this.isPreview = isPreview;
        init();
        joinWindow = new GameJoinWindow(skin);
        //joinButton.clearChildren();
        //joinDialog.button("Join").setDebug(false);
        //joinDialog.set
        //joinDialog.show(factory.getUserInterfaceService().getStage());
        //add(joinWindow);this
        //System.out.println("Adding block " + this.getName() + "w: " + this.getWidth() + " h: " + this.getHeight());
    }

    public GameBlockArea(Skin skin) {
        this(skin, true);
    }

    private void init(){
        initializeBoard();
        initializeSize();
        initializeGrid();
    }

    private void initializeSize(){
        GameBlock clear = getClearBlock();
        float width = clear.getWidth() * YokelGameBoard.MAX_COLS;
        float height = clear.getHeight() * YokelGameBoard.MAX_PLAYABLE_ROWS;

        //validate();

        //this.setBounds(sLoc.x, sLoc.y, width, height);
        //grid.setBounds(sLoc.x, sLoc.y, width, height);
        //setCullingArea(new Rectangle(sLoc.x, sLoc.y, width, height));
    }

    private void initializeBoard(){
        this.uiBlocks = new ObjectMap<>();
        this.boardNumber = 0;
        this.grid = new Table(skin);
        this.bgNumber = new Table(skin);
        this.pieceSprite = new PieceDrawable();
    }

    public void setDebug (boolean enabled) {
        super.setDebug(YokelUtilities.setDebug(enabled, grid, bgNumber));
    }

    private void initializeGrid(){
        this.grid.setName(GRID_NAME);
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
        setAreaBounds();
        grid.setBackground(GameClock.NO_DIGIT_NME);
        grid.add(pieceSprite);
        add(grid);
    }

    private void setAreaBounds(){
        GameBlock block = getClearBlock();
        float width = block.getWidth();
        float height = block.getHeight();
        Rectangle bounds = new Rectangle(0, 0, width * YokelGameBoard.MAX_COLS, height * YokelGameBoard.MAX_PLAYABLE_ROWS);
        grid.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        grid.setCullingArea(bounds);
    }

    private GameBlock getClearBlock(){
        return YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK, isPreview);
    }

    private String getCellAttrName(int r, int c){
        return CELL_ATTR + CELL_ATTR_SEPARATOR + r + CELL_ATTR_SEPARATOR + c;
    }

    public int getBoardNumber(){
        return this.boardNumber;
    }

    void setBoardNumber(int number){
        this.bgNumber.setName(BOARD_NUMBER_NAME);
        this.grid.setBackground(number + GameClock.DIGIT_NME);
        this.boardNumber = number;
    }

    private void setBlock(int block, int r, int c){
        GameBlock uiCell = uiBlocks.get(getCellAttrName(r, c));

        if(uiCell != null){
            uiCell.update(block, isPreview);
        }
    }

    public boolean isDownCellFree(int column, int row) {
        return row > 0 && row < YokelGameBoard.MAX_PLAYABLE_ROWS + 1 && getPieceValue(column, row - 1) == YokelBlock.CLEAR_BLOCK;
    }

    public int getPieceValue(int c, int r){
        GameBlock uiCell = uiBlocks.get(getCellAttrName(r, c));
        int ret = YokelBlock.CLEAR_BLOCK;
        if(uiCell != null) ret = UIUtil.getInstance().getFactory().getBlockNumber(uiCell.getImage().getName());
        return ret;
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

    public void setPreview(boolean isPreview){
        this.isPreview = isPreview;
        setAreaBounds();
        update();
    }

    @Override
    public void act(float delta){
        super.act(delta);
        for(GameBlock uiblock : uiBlocks.values()) {
            if(uiblock != null){
                uiblock.act(delta);
            }
        }
        //joinWindow.setPosition(getX(), getY());
    }

    @Override
    public void draw(Batch batch, float alpha){
        super.draw(batch, alpha);
        //this.drawChildren();
        //joinWindow.setPosition(getX(), getY() / 2);
        //if(!isActive) return;
        //System.err.println("s=" + super.getX());
        //drawGamePiece(batch, alpha);
        //drawSprites(batch, alpha);
    }

    private boolean validatePiece(){
        return pieceSprite != null && !isPreview && isActive && isPlayerView;
    }

    private static class PieceDrawable extends Actor {
        private GameBlock[] blocks = new GameBlock[3];
        private int row;
        private int col;
        private boolean isActive;
        private float fallOffset;
        private GameBlockArea parent;

        PieceDrawable(){
            blocks[0] = YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK);
            blocks[1] = YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK);
            blocks[2] = YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK);
        }

        void setActive(boolean b){
            isActive = b;
        }

        @Override
        public void act(float delta){
            super.act(delta);
            if(isActive) {
                for(int i = 0; i < 3; i++){
                    if(blocks[i] != null){
                        blocks[i].act(delta);
                    }
                }
            }
        }

        @Override
        public void draw(Batch batch, float alpha){
            super.draw(batch, alpha);
            if(isActive){
                computePosition();
                float x = getX();
                float y = getY();

                for(int i = 0; i < 3; i++){
                    if(this.blocks[i] != null){
                        this.blocks[i].setPosition(x, y + (i * blocks[i].getHeight() / 2));
                        this.blocks[i].draw(batch, alpha);
                    }
                }
            }
        }

        void setBlocks(YokelPiece piece){
            if(piece != null && isActive){
                updateIndex(0, piece.getBlock1());
                updateIndex(1, piece.getBlock2());
                updateIndex(2, piece.getBlock3());
                this.row = piece.row;
                this.col = piece.column;
            }
        }

        private void updateIndex(int index, int block){
            if(index > -1 && index < blocks.length && blocks[index] != null){
                blocks[index].update(block,false);
            }
        }

        private void setParent(GameBlockArea area){
            this.parent = area;
        }

        private void computePosition() {
            GameBlock block = blocks[0];

            if(block != null){
                // No transform for this group, offset each child.

                //Set the x to the position of the grid
                Vector2 pos = localToParentCoordinates(new Vector2(0, 0));

                float SIDE_BAR_OFFSET = 12;
                pos.x = pos.x / 2 + SIDE_BAR_OFFSET;
                pos.y = pos.y / 2;

                pos.x -= block.getWidth();
                if(parent != null){
                    if(parent.isDownCellFree(col, row)){
                        pos.y -= ((1 - fallOffset) * block.getHeight() / 2);
                    }
                }

                float offSetX = block.getWidth() / 2 * col;
                float offSetY = block.getHeight() / 2 * row;

                this.setPosition(pos.x + offSetX, pos.y + offSetY);
            }
        }

        private void setFallOffset(float fallOffset) {
            this.fallOffset = fallOffset;
        }
    }

    private void setPieceSprite(YokelPiece piece, float fallOffset){
        if(piece != null){
            pieceSprite.setBlocks(piece);
            pieceSprite.setParent(this);
            pieceSprite.setFallOffset(fallOffset);
        }
    }

    public void updateData(YokelGameBoard gameBoard) {
        if(gameBoard != null) {
            this.board = gameBoard;
            setPieceSprite(gameBoard.fetchCurrentPiece(), board.fetchCurrentFallnumber());
            update();
        }
    }

    private void update(){
        if(board != null){
            //System.out.println(board);
            for(int r = 0; r < YokelGameBoard.MAX_PLAYABLE_ROWS; r++){
                for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                    if(YokelBlockEval.hasPowerBlockFlag(board.getPieceValue(c, r))){
                        System.out.println("POWER!!!!");
                    }
                    //System.out.println("block!" + board.getBlockValueAt(c, r));
                    //System.out.println("POWER!!!!" + board.getPieceValue(c, r));

                    setBlock(board.getPieceValue(c, r), r, c);
                }
            }
        }
    }

    void setActive(boolean b){
        this.isActive = b;
    }

    void setPlayerView(boolean b){
        this.isPlayerView = b;
        this.pieceSprite.setActive(validatePiece());
    }
}