package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Queue;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockMove;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.utils.YokelUtilities;

import java.util.Vector;

/**
 * Created by eboateng on 3/19/2018.
 */

public class GameBoard extends Table {
    private static float MAX_KEY_HOLD_TIME = 15;

    private GameNameLabel nameLabel;
    private GamePiece next;
    private GamePowersQueue powers;
    private GameBlockArea area;

    private float blockWidth;
    private float blockPrevWidth;
    private float blockHeight;
    private float blockPrevHeight;

    private boolean isLeftBar = true;

    public GameBoard(Skin skin) {
        super(skin);
        initialize(skin);
        GameBlock block = YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK);
        GameBlock previewBlock = YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK, area.isPreview());

        blockWidth = block.getWidth();
        blockPrevWidth = previewBlock.getWidth();
        blockHeight = block.getHeight();
        blockPrevHeight = previewBlock.getHeight();
    }

    private void initialize(Skin skin) {
        next = new GamePiece(skin);
        powers = new GamePowersQueue(skin);
        area = new GameBlockArea(skin);
        nameLabel = new GameNameLabel(skin);
        setUpBoard();
    }

    private void setUpBoard(){
        clearChildren();

        Table right = new Table(getSkin());
        right.add(area);

        if(area.isPreview()){
            add(nameLabel).left().row();
            add(right);
        } else {
            Table left = new Table(getSkin());
            left.add(next).top().row();
            left.add(new GamePiece(getSkin())).row();
            left.add(powers).bottom();

            add(nameLabel).left().colspan(2);
            row();

            if(isLeftBar){
                add(left).top();
                add(right).expandX();
            } else {
                add(right).expandX();
                add(left).top();
            }
        }
    }

    @Override
    public void setDebug(boolean debug){
        super.setDebug(YokelUtilities.setDebug(debug, nameLabel, next, powers, area));
    }

    public void setPreview(boolean preview){
        area.setPreview(preview);
        setUpBoard();
    }

    public void update(YokelGameBoard board){
        if(board != null){
            area.updateData(board);
            powers.updateQueue(blockToGameBlocks(board.getPowers()));
            setUpNext(board);
        }
    }

    private void setUpNext(YokelGameBoard board){
        YokelPiece piece = board.fetchCurrentNextPiece();
        if(piece != null){
            next.setData(piece.toString());
        }
    }

    public void setPlayerLabel(String data){
        if(data != null){
            nameLabel.setData(data);
        }
        setUpBoard();
    }

    public void setLeftBarOrientation(boolean isLeft){
        isLeftBar = isLeft;
        setUpBoard();
    }

    @Override
    public float getPrefHeight() {
        if(area.isPreview()){
            return blockPrevHeight * YokelGameBoard.MAX_PLAYABLE_ROWS + nameLabel.getPrefHeight();
        } else {
            return blockHeight * YokelGameBoard.MAX_PLAYABLE_ROWS + nameLabel.getPrefHeight();
        }
    }

    @Override
    public float getPrefWidth() {
        if(area.isPreview()){
            return blockPrevWidth * YokelGameBoard.MAX_COLS + nameLabel.getPrefWidth();
        } else {
            return blockWidth * YokelGameBoard.MAX_COLS + blockWidth * 2;
        }
    }

    public void setBoardNumber(int boardNumber) {
        area.setBoardNumber(boardNumber);
    }

    public int getBoardNumber() {
        return area.getBoardNumber();
    }

    public YokelGameBoard getYokelGameBoard() {
        return area.getBoard();
    }

    private Queue<GameBlock> blockToGameBlocks(Queue<Integer> blocks){
        Queue<GameBlock> gameBlocks = new Queue<>();
        for(int block : blocks){
            gameBlocks.addFirst(YokelUtilities.getBlock(block, area.isPreview()));
        }
        return gameBlocks;
    }

    public void setActive(boolean b) {
        area.setActive(b);
    }

    public void setPlayerView(boolean b) {
        area.setPlayerView(b);
    }

    public void killPlayer(){
        area.killPlayer();
    }

    public void pushCellsToMove(Vector<YokelBlockMove> toDrop) {
        area.pushCellsToMove(toDrop);
    }

    public void addBlocksToDrop(Vector<YokelBlockMove> cellsToDrop) {
        area.pushCellsToMove(cellsToDrop);
    }

    public boolean isActionFinished(){
        return area.isActionFinished();
    }
}