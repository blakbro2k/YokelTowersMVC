package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;

/**
 * Created by eboateng on 3/19/2018.
 */

public class GameBoard extends Table {
    private static float MAX_KEY_HOLD_TIME = 15;

    public GameBoard(Skin skin) {
        super(skin);

        initialize();
        invalidateHierarchy();
    }

    private void initialize(){
        /*
        resetBoard();

        float prefWidth = getPrefWidth();
        float prefHeight = getPrefHeight();

        setSize(prefWidth, prefHeight);

        background = new Rectangle(getX(), getY(), prefWidth, prefHeight);
        //nextPieceBuffer = new Queue<>();

        Table sidePanel = new Table();
        Table gameTable = new Table();
        gameTable.setDebug(true);
        sidePanel.setDebug(true);

        //next = new NextPeiceQueue(app);
        //powers = new PowersQueue();
        //blockArea = new GameBlockArea(board, app);

        Image playerIcon = new Image(app.getAssetsManager().getImageProvider().getPlayerIcon(7));

        gameTable.add(playerIcon).align(Align.top);
        gameTable.add(new Label("ready player 1", skin)).align(Align.top).row();

        sidePanel.add(next).row();
        sidePanel.add(powers).align(Align.bottom);

        gameTable.add(sidePanel);
        gameTable.add(blockArea);

        add(gameTable).row();
        add(new Label(blockArea.getNumber() + "", skin));*/
    }

    /*


    private float keyHeld;

    private int index;
    private Rectangle background;

    //UI
    private GamePiece currentPiece;
    private NextPeiceQueue next;
    private PowersQueue powers;
    private GameBlockArea blockArea;

    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;
    private boolean isDownKeyPressed = false;

    private boolean isBoardRunning = false;
    private boolean needsReset = false;
    private boolean isKeyHeld = false;

    public GameBoard() {

    }



    public GameBoard(int boardNumber){
        initialize();
        invalidateHierarchy();
    }



    public YokelPiece getNextPiece(){
        if (nextPieceBuffer.size < 1){
            setNextPiece(fetchNextYokelPiece());
        }
        return nextPieceBuffer.removeFirst();
    }

    public void setNextPiece(YokelPiece piece){
        if(piece != null){
            this.nextPieceBuffer.addFirst(piece);

            GamePiece gPiece = createGamePiece(piece);

            if(gPiece != null){
                this.next.update(gPiece);
            }
        }
    }

    private GamePiece createGamePiece(YokelPiece piece){
        if(piece != null){
            GameBlock top = createGameBlock(piece.getTopBlock());
            GameBlock mid = createGameBlock(piece.getMiddleBlock());
            GameBlock bot = createGameBlock(piece.getBottomBlock());

            return new GamePiece(top, mid, bot);
        }
        return null;
    }

    private GameBlock createGameBlock(YokelBlock block){
        if(block != null){
            return app.getAssetsManager().getGameObjectFactory().translateGameBlock(block);
        }
        return null;
    }

    @Override
    public void act(float delta){
        //next Queue
        next.act(delta);

        //powers queue
        powers.act(delta);
        updatePowers(blockArea.checkBoardMatches());

        //game area
        blockArea.act(delta);

        setCurrentPiece();
        processInput();
    }

    private void processInput() {
        if(isKeyHeld){
            keyHeld++;
        }

        if(isDownKeyPressed){
            attemptSpeedDown(true);
        } else {
            attemptSpeedDown(false);
        }

        if(keyHeld > MAX_KEY_HOLD_TIME){
            if(isLeftKeyPressed){
                attemptMoveLeft();
            }

            if(isRightKeyPressed){
                attemptMoveRight();
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //blockArea.draw(batch, parentAlpha);

        //powers.draw(batch, parentAlpha);

        //setColor(new Color(0.3f, 0.3f, 0.5f, parentAlpha));

        //draw Background
        //background.setPosition(localX, localY);
        //Util.drawBackgroundRect(batch, background, new Color(0.3f, 0.3f, 0.5f, parentAlpha));

        //draw number

        //boardNumber.setColor(Color.BLACK);
        //boardNumber.setPosition(localX,localY);
        //boardNumber.draw(batch, parentAlpha);
    }

    private void updatePowers(Queue<YokelBlock> yokelBlocks) {
        if(yokelBlocks != null){
            powers.update(createGameBlockQueue(yokelBlocks));
        }
    }

    private Queue<GameBlock> createGameBlockQueue(Queue<YokelBlock> queue){
        Queue<GameBlock> powers = null;
        if(queue != null){
            powers = new Queue<GameBlock>();
            for(YokelBlock block : queue){
                if(block != null){
                    powers.addFirst(createGameBlock(block));
                }
            }
        }
        return powers;
    }

    private boolean isNextPieceEqual(YokelPiece nextPiece, GameBlock top, GameBlock mid, GameBlock bot){
        if(nextPiece == null || top == null || mid == null || bot == null) {
            return false;
        }

        YokelBlock topBlock = nextPiece.getTopBlock();
        YokelBlock middleBlock = nextPiece.getMiddleBlock();
        YokelBlock bottomBlock = nextPiece.getBottomBlock();

        return topBlock.matchesGenericType(top.getBlock())
                && middleBlock.matchesGenericType(mid.getBlock())
                && bottomBlock.matchesGenericType(bot.getBlock());
    }

    public float getPrefWidth () {
        float width = super.getPrefWidth();
        float localWidth = Util.getClearBlockWidth();

        if (localWidth > 0){
            width = localWidth * NUMBER_WIDTH_BLOCKS;
        }
        return width;
    }

    public float getPrefHeight () {
        float height = super.getPrefHeight();
        float localHeight = Util.getClearBlockHeight();

        if (localHeight > 0){
            height = localHeight * NUMBER_HEIGHT_BLOCKS;
        }
        return height;
    }

    public void startBoard(){
        if(!needsReset){
            isBoardRunning = true;
            needsReset = true;
            setNextPiece(fetchNextYokelPiece());
            setCurrentPiece();
        } else {
            throw new GdxRuntimeException("You must reset the table RND before starting this board.");
        }
    }

    private void setCurrentPiece(){
        if(canSetCurrentPiece()){
            blockArea.setCurrentPiece(createGamePiece(getNextPiece()));
            setNextPiece(fetchNextYokelPiece());
        }
    }

    private boolean canSetCurrentPiece(){
        return blockArea != null && blockArea.needsCurrentPiece() && isBoardRunning;
    }

    public void stopBoard(){
        isBoardRunning = false;
    }

    public void resetBoard(){
        needsReset = false;
        index = 0;
    }

    private YokelPiece fetchNextYokelPiece(){
        return new YokelGamePiece(new YokelBlock(fetchYokelBlock()),
                new YokelBlock(fetchYokelBlock()),
                new YokelBlock(fetchYokelBlock()));
    }

    private YokelBlockType fetchYokelBlock(){
        index++;
        return app.requestGameBlock(index);
    }

    public void attemptMoveRight() {
        if(isBoardRunning){
            blockArea.moveCurrentPieceRight();
        }
    }

    public void attemptMoveLeft() {
        if(isBoardRunning){
            blockArea.moveCurrentPieceLeft();
        }
    }

    public void attemptSpeedDown(boolean isSpeedDown) {
        if(isBoardRunning) {
            blockArea.setSpeedDown(isSpeedDown);
        }
    }

    public void clockWiseCycle(){
        if(isBoardRunning){
            blockArea.cycleClockWise();
        }
    }

    public void setDownKeyPressed(boolean isDownKeyPressed){
        this.isDownKeyPressed = isDownKeyPressed;
    }

    public void holdDownKey(int keyCode){
        isKeyHeld = true;
        switch (keyCode){
            case Input.Keys.RIGHT:
                isRightKeyPressed = true;
                attemptMoveRight();
                break;
            case Input.Keys.LEFT:
                isLeftKeyPressed = true;
                attemptMoveLeft();
                break;
            default:
                break;
        }
    }

    public void releaseDownKey(){
        isKeyHeld = false;
        keyHeld = 0;
        isLeftKeyPressed = false;
        isRightKeyPressed = false;
        //isCycleKeyPressed = false;
        isDownKeyPressed = false;
    }
    */
}
