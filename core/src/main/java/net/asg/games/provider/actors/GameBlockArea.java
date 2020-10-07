package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockMove;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.utils.UIUtil;
import net.asg.games.utils.YokelUtilities;

import java.util.Vector;

public class GameBlockArea extends Stack {
    private static final String CELL_ATTR = "uiCell";
    private static final String CELL_ATTR_SEPARATOR = "_";
    private static final String GRID_NAME = "grid";
    private static final String BOARD_NUMBER_NAME = "boardNumber";
    private static final String ALIVE_BACKGROUND = "area_alive_bg";
    private static final String DEAD_BACKGROUND = "area_dead_bg";
    private static final String PLAYER_BACKGROUND = "area_player_bg";

    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;

    private boolean isActive;
    private boolean isPreview;
    private boolean isPlayerView;

    private int boardNumber;
    private YokelGameBoard board;

    private Skin skin;

    private Table grid;
    private Table bgNumber;
    private Table bgColor;
    private Label tableNumber;

    private GameJoinWidget joinWindow;

    private ObjectMap<String, GameBlock> uiBlocks;
    private Queue<GameBlock> dropCells;
    private PieceDrawable pieceSprite;

    public GameBlockArea(Skin skin, boolean isPreview) {
        this.skin = skin;
        this.isPreview = isPreview;
        init();
        joinWindow = new GameJoinWidget(skin);
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
        setBoardNumber(0);
        bgColor.setBackground(skin.getDrawable(ALIVE_BACKGROUND));
        add(bgColor);
        bgNumber.add(tableNumber);
        bgNumber.add("");
        add(bgNumber);
        initializeGrid();
    }

    private void initializeSize(){
        GameBlock clear = getClearBlock();
        float width = clear.getWidth() * YokelGameBoard.MAX_COLS;
        float height = clear.getHeight() * YokelGameBoard.MAX_PLAYABLE_ROWS;

        //this.setBounds(sLoc.x, sLoc.y, width, height);
        //grid.setBounds(sLoc.x, sLoc.y, width, height);
        //setCullingArea(new Rectangle(sLoc.x, sLoc.y, width, height));
    }

    private void initializeBoard(){
        this.uiBlocks = new ObjectMap<>();
        this.boardNumber = 0;
        this.grid = new Table(skin);
        this.bgColor = new Table(skin);
        this.bgNumber = new Table(skin);
        this.pieceSprite = new PieceDrawable();
        this.dropCells = new Queue<>();
    }

    public void setDebug (boolean enabled) {
        super.setDebug(YokelUtilities.setDebug(enabled, grid, bgNumber));
    }

    private void initializeGrid(){
        this.grid.setName(GRID_NAME);
        for(int r = YokelGameBoard.MAX_PLAYABLE_ROWS - 1; r >= 0; r--){
            for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                GameBlock uiBlock = getClearBlock();

                uiBlocks.put(getCellAttrName(r, c), uiBlock);
                if(c + 1 == YokelGameBoard.MAX_COLS){
                    grid.add(uiBlock).row();
                } else {
                    grid.add(uiBlock);
                }
            }
        }
        setAreaBounds();
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
        if(tableNumber == null){
            tableNumber = new Label("", skin);
            tableNumber.setFontScale(2);
            tableNumber.setColor(0,0,0,1);
        }
        this.bgNumber.setName(BOARD_NUMBER_NAME);
        this.boardNumber = number;
        this.tableNumber.setText(number);
        if(board != null) {
            this.board.setName(this.board.getId() + " : " + number);
        }
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
                uiblock.act(delta * YokelUtilities.otof(0.03));
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

    public boolean isActive() {
        return isActive;
    }

    public void pushCellsToMove(Vector<YokelBlockMove> cellsToDrop) {
        for(YokelBlockMove toMove : cellsToDrop){
            if(toMove != null) {
                System.out.println("[" + toMove.x + "," + toMove.y + "] to row: " + toMove.targetRow);
                //System.err.println("uiBlock: " + uiBlocks.get(getCellAttrName(toMove.y, toMove.x)));
                GameBlock uiCell = uiBlocks.get(getCellAttrName(toMove.y, toMove.x));
                if(uiCell != null) {
                    System.err.println("HAS ACTION!!" + uiCell.hasActions());
                    System.err.println("[" + uiCell.getX() + "," + uiCell.getX() + "] to row: " + toMove.targetRow);
                    System.out.println("{" + grid.getCell(uiCell).getActorX() + "}");
                    uiCell.addAction(Actions.moveTo(uiCell.getX() / 2, (uiCell.getX() - 16) / 2, 0.8f, Interpolation.linear));
                    System.err.println("HAS ACTION!!" + uiCell.hasActions());
                }
                //dropCells.addLast(uiCell);
            }
        }
    }

    public boolean isActionFinished(){
        //System.out.println("dropCells{" + dropCells + "}");
        return dropCells.isEmpty();
    }

    /*        boolean isFinished = false;
        for(Cell cell : grid.getCells()) {
            if(cell != null){
                if(cell.hasActor()){
                    Actor actor = cell.getActor();
                    System.out.println("actor{" + actor.getName() + "}");

                    if(actor != null && actor.hasActions()) {
                        isFinished = true;
                        break;
                    }
                }
            }
        }
        return isFinished;

     */
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
                //Set the x to the position of the grid
                Vector2 pos = localToParentCoordinates(new Vector2(0, 0));

                float SIDE_BAR_OFFSET = 12;
                pos.x = pos.x / 2 + SIDE_BAR_OFFSET;
                pos.y = pos.y / 2;

                pos.x -= block.getWidth();
                if(parent != null && parent.isDownCellFree(col, row)){
                    pos.y -= ((1 - fallOffset) * block.getHeight() / 2);
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

    private void setPieceSprite(YokelGameBoard board, float fallOffset){
        if(board != null) {
            YokelPiece piece = board.fetchCurrentPiece();
            if(piece != null){
                pieceSprite.setBlocks(piece);
                pieceSprite.setParent(this);
                pieceSprite.setFallOffset(fallOffset);
                pieceSprite.setVisible(!board.isPieceSet());
            }
        }
    }

    public void updateData(YokelGameBoard gameBoard) {
        if(gameBoard != null) {
            this.board = gameBoard;
            update();
            setPieceSprite(gameBoard, board.fetchCurrentFallNumber());
            setBrokenBlocks(gameBoard.getBrokenCells());
            dropCells(gameBoard.getCellsToBeDropped());
        }
    }

    private void dropCells(Vector<YokelBlockMove> cellsToBeDropped) {
        if(cellsToBeDropped != null && cellsToBeDropped.size() > 0) {
            YokelBlockMove cell = cellsToBeDropped.remove(0);
        }
    }

    private void setBrokenBlocks(Vector<YokelBlock> brokenCells) {
        if(brokenCells != null && brokenCells.size() > 0){
            YokelBlock block = brokenCells.remove(0);
        }
    }

    private void update(){
        if(board != null){
            for(int r = 0; r < YokelGameBoard.MAX_PLAYABLE_ROWS; r++){
                for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                    setBlock(board.getBlockValueAt(c, r), r, c);
                }
            }
        }
    }

    void setActive(boolean b){
        this.isActive = b;
        if(isActive){
            if(isPlayerView){
                bgColor.setBackground(skin.getDrawable(PLAYER_BACKGROUND));
            } else {
                bgColor.setBackground(skin.getDrawable(ALIVE_BACKGROUND));
            }
        }
    }

    void setPlayerView(boolean b){
        this.isPlayerView = b;
        this.pieceSprite.setActive(!isPreview && isPlayerView);
        if(isPlayerView){
            bgColor.setBackground(skin.getDrawable(PLAYER_BACKGROUND));
        }
    }

    void killPlayer(){
        if(isActive && board.hasPlayerDied()){
            bgColor.setBackground(skin.getDrawable(DEAD_BACKGROUND));
        }
        setActive(false);
    }

    YokelGameBoard getBoard(){
        return board;
    }
}