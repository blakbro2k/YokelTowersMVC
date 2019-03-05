package net.asg.games.provider.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GameBlockArea extends Table {

    private static final float BLOCK_DROP_SPEED = .8f;
    private static final float MAX_BLOCK_DROP_SPEED = 6f;
    private static final float FALL_BLOCK_SPEED = 250f;

    private boolean isSpeedDown;
    private Array<GameBlock> blocks;

    private int boardNumber;
    private GamePiece currentPiece;
    private Rectangle bounds;


    public GameBlockArea(int boardNumber){
        if(boardNumber < 1 || boardNumber > 8) throw new GdxRuntimeException("Board number must be 0 < x < 9");
        bounds = new Rectangle();

        setBounds();
        setDebug(true);
    }

    private void setBounds(){
        setBounds(getX(), getY(), getPrefWidth(), getPrefHeight());
        bounds.set(getX(), getY(), getPrefWidth(), getPrefHeight());
    }
/*
    private void updateDrawableBoard(){
        int col = 0;
        Queue<YokelBlock> column;
        while(col < YokelGameBoard.MAX_COLS){
            column = myBoard.getColumn(col);
            for(int row = 0; row < YokelGameBoard.MAX_ROWS; row++){
                YokelBlock cBlock = column.get(row);

                drawableBoard[row][col] = translateGamePiece(drawableBoard[row][col], cBlock);
            }
            col++;
        }
    }

    public void setCurrentPiece(GamePiece piece){
        if(piece != null){
            piece.setPosition(bounds.x + (getPrefBlockWidth() * 2), bounds.y + bounds.height);
            currentPiece = piece;
        }
    }

    public void moveCurrentPieceLeft(){
        if(currentPiece != null && !isLeftSpaceOccupied(currentPiece.getBottomBlock())) {
            float blockSize = currentPiece.getX();
            if(blockSize > bounds.x){
                currentPiece.setPosition(blockSize - getPrefBlockWidth(), currentPiece.getY());
            }
        }
    }

    public void moveCurrentPieceRight(){
        if(currentPiece != null && !isRightSpaceOccupied(currentPiece.getBottomBlock())) {
            float blockSize = currentPiece.getX() + getPrefBlockWidth();
            if(blockSize < bounds.x + getPrefWidth()){
                currentPiece.setPosition(blockSize, currentPiece.getY());
            }
        }
    }

    public void moveCurrentPieceDown(){
        if(currentPiece != null){
            GameBlock block = currentPiece.getBottomBlock();

            int row = getRowFromBoard(block);
            int col = getColumnFromBoard(block);
            float cPieceY = currentPiece.getY();

            currentPiece.setPosition(currentPiece.getX(),  cPieceY - 1 * getSpeed());

            if(bBlock != null && !Util.isClearBlock(bBlock)){
                setCurrentPieceToBoard(row + 1, col);
            }

            if(cPieceY < bounds.y){
                setCurrentPieceToBoard(row, col);
            }
        }
    }

    private float getSpeed(){
        if(isSpeedDown){
            return MAX_BLOCK_DROP_SPEED;
        }
        return BLOCK_DROP_SPEED;
    }

    public void setSpeedDown(boolean isSpeedDown){
        this.isSpeedDown = isSpeedDown;
    }

    private boolean isRightSpaceOccupied(GameBlock block) {
        if(block != null){
            int row = getRowFromBoard(block);
            int col = getColumnFromBoard(block);

            YokelBlock rightSpace = myBoard.getGameBlock(row, col + 1);
            return rightSpace == null || !Util.isClearBlock(rightSpace);

        }
        return false;
    }

    public YokelGameBoard getMyBoard() {
        return myBoard;
    }

    private boolean isLeftSpaceOccupied(GameBlock block) {
            if(block != null){
                int col = getColumnFromBoard(block);
                int row = getRowFromBoard(block);

                YokelBlock leftSpace = myBoard.getGameBlock(row, col - 1);
                return leftSpace == null || !Util.isClearBlock(leftSpace);
            }
        return false;
    }

    private GameBlock translateGamePiece(GameBlock gameObject, YokelBlock block) {
        if(gameObject == null){
            return app.getAssetsManager().getGameObjectFactory().translateGameBlock(block);
        } else {
            YokelBlock obj = gameObject.getBlock();

            if(obj != null && !obj.equals(block)){
                return app.getAssetsManager().getGameObjectFactory().translateGameBlock(block);
            } else {
                return gameObject;
            }
        }
    }

    @Override
    public void act(float delta){
        //System.out.println("pre act ***\n");
        //System.out.println("yokelboard=\n" + myBoard);
       // System.out.println("drawboard=\n" + this);
       // System.out.println("end pre act ***\n");

        updateDrawableBoard();  //draws a broken block on the screen
        updateRemoveBlocks(myBoard.removeBrokenBlocks());

        //act falling blocks
        if(drawableBrokenBlocks != null){
            for(GameBlock block : drawableBrokenBlocks){
                if(block != null){
                    block.setPosition(block.getX(), block.getY() - (delta * FALL_BLOCK_SPEED));
                    block.act(delta);

                    if(block.getY() < 0){
                        app.getAssetsManager().getGameObjectFactory().freeObject(block);
                        drawableBrokenBlocks.removeValue(block, false);
                    }
                }
            }
        }

        //act board pieces
        if (drawableBoard != null){
            for(int r = 0; r < YokelGameBoard.MAX_ROWS; r++){
                for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                    actOnGamePiece(drawableBoard[r][c], delta);
                }
            }
        }

        if(currentPiece != null){
            currentPiece.act(delta);
        }
        moveCurrentPieceDown();
        myBoard.clearBrokenBlocks();
        //System.out.println("post act ***\n");
        //System.out.println("yokelboard=\n" + myBoard);
        //System.out.println("drawboard=\n" + this);
        //System.out.println("end post act ***\n");
    }


    private void updateRemoveBlocks(Queue<Vector2> brokenBlocks){
        if(brokenBlocks != null){
            for(Vector2 block : brokenBlocks){
                if (block != null){
                    int row = (int) block.x;
                    int col = (int) block.y;

                    GameBlock brokenBlock = drawableBoard[row][col];
                    if(brokenBlock != null){
                        brokenBlock.setPosition(translateXFromCol(col, brokenBlock.getPrefWidth()),
                                translateYFromRow(row, brokenBlock.getPrefHeight()));
                        drawableBrokenBlocks.addFirst(brokenBlock);
                        drawableBoard[row][col] = null;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //System.out.println(myBoard);
        positionChanged();

        if(getDebug()){
            Util.drawBackgroundRect(batch, bounds, new Color(Color.CYAN));
        }

        //draw board pieces
        if (drawableBoard != null){
            for(int r = 0; r < YokelGameBoard.MAX_ROWS; r++){
                for(int c = 0; c < YokelGameBoard.MAX_COLS; c++){
                    renderBlock(batch, parentAlpha, r, c);
                }
            }
        }

        //draw falling blocks
        if(drawableBrokenBlocks != null){
            for(GameBlock block : drawableBrokenBlocks){
                if(block != null){
                    block.draw(batch, parentAlpha);
                }
            }
        }

        if(currentPiece != null){
            currentPiece.draw(batch, parentAlpha);
        }
    }

    private void renderBlock(Batch batch, float parentAlpha, int row, int col) {
        if(batch != null){
            float localWidth = getPrefBlockWidth();
            float localHeight = getPrefBlockHeight();

            float localX = getX() + (col * localWidth);
            float localY = getY() + (row * localHeight);

            GameBlock block = drawableBoard[row][col];

            if(block != null){
                block.setPosition(localX, localY);
                block.draw(batch, parentAlpha);
            }
        }
    }

    public Queue<YokelBlock> checkBoardMatches(){
        if(myBoard != null){
            return myBoard.checkMatches();
        }
        return null;
    }

    public void positionChanged(){
        if(bounds.x != getX()){
            bounds.setX(getX());
        }

        if(bounds.y != getY()){
            bounds.setY(getY());
        }
    }

    private void setCurrentPieceToBoard(int r, int c){
        if(currentPiece != null){
            GameBlock top = currentPiece.getTopBlock();
            GameBlock mid = currentPiece.getMiddleBlock();
            GameBlock bot = currentPiece.getBottomBlock();

            YokelBlock yTop = getYokelBlock(top);
            YokelBlock yMid = getYokelBlock(mid);
            YokelBlock yBot = getYokelBlock(bot);

            myBoard.setGameBlock(r + 2, c, yTop);
            myBoard.setGameBlock(r + 1, c, yMid);
            myBoard.setGameBlock(r, c, yBot);
        }
        currentPiece = null;
    }

    private YokelBlock getYokelBlock(GameBlock gameBlock){
        if(gameBlock != null){
            return gameBlock.getBlock();
        }
        return null;
    }

    private void actOnGamePiece(GameBlock gameBlock, float delta) {
        if(gameBlock != null){
            gameBlock.act(delta);
        }
    }

    public int getNumber() {
        if(myBoard != null){
            return myBoard.getBoardNumber();
        } else {
            return -1;
        }
    }

    public float getPrefWidth(){
        float width = 0;
        float localWidth = Util.getClearBlockWidth();

        if (localWidth > 0){
            width = localWidth * 6;
        }
        return width;
    }

    public float getPrefHeight(){
        float height = 0;
        float localHeight = Util.getClearBlockHeight();

        if (localHeight > 0){
            height = localHeight * 16;
        }
        return height;
    }

    public float getPrefBlockWidth(){
        return Util.getClearBlockWidth();
    }

    public float getPrefBlockHeight(){
        return Util.getClearBlockHeight();
    }

    public boolean needsCurrentPiece(){
        return currentPiece == null;
    }

    public void cycleClockWise() {
        if(currentPiece != null){
            currentPiece.cycleClockWise();
        }
    }

    private float translateXFromCol(int col, float blockWidth){
        return bounds.x + col * blockWidth;
    }

    private float translateYFromRow(int row, float blockHeight){
        return bounds.y + row * blockHeight;
    }

    private int getRowFromBoard(GameBlock block){
        try{
            if(block != null){
                if(bounds != null){
                    float clip = block.getY() - bounds.y;
                    return MathUtils.floor((clip < 0? 0 : clip) / block.getPrefHeight());
                }
            }
        } catch (Exception e){
            try {
                ErrorUtils.getInstance().showErrorPopup(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return -1;
        }
        return -1;
    }

    private int getColumnFromBoard(GameBlock block){
        try{
            if(block != null){
                if(bounds != null){
                    float clip = block.getX() - bounds.x;
                    return MathUtils.floor((clip < 0? 0 : clip) / block.getPrefWidth());
                }
            }
        } catch (Exception e){
            try {
                ErrorUtils.getInstance().showErrorPopup(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return -1;
        }
        return -1;
    }

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
