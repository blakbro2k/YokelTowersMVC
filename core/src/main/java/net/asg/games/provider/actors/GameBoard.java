package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Queue;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.utils.Util;

/**
 * Created by eboateng on 3/19/2018.
 */

public class GameBoard extends Table {
    private static float MAX_KEY_HOLD_TIME = 15;

    private GameNameLabel nameLabel;
    private GamePiece next;
    private GamePiece piece;
    private GamePowersQueue powers;
    private GameBlockArea area;

    private float blockWidth;
    private float blockPrevWidth;
    private float blockHeight;
    private float blockPrevHeight;

    private boolean isPreview = false;
    private boolean isLeftBar = true;

    public GameBoard(Skin skin) {
        super(skin);
        GameBlock block = Util.getBlock(YokelBlock.CLEAR_BLOCK);
        GameBlock previewBlock = Util.getBlock(YokelBlock.CLEAR_BLOCK, true);

        blockWidth = block.getWidth();
        blockPrevWidth = previewBlock.getWidth();
        blockHeight = block.getHeight();
        blockPrevHeight = previewBlock.getHeight();

        initialize(skin);
    }

    private void initialize(Skin skin) {
        next = new GamePiece(skin);
        powers = new GamePowersQueue(skin);
        area = new GameBlockArea(skin);
        setUpBoard();

    }

    private void setUpBoard(){
        clearChildren();

        Table right = new Table(getSkin());
        right.add(area);

        if(isPreview){
            add(nameLabel);
            row();
            add(right);
        } else {
            Table left = new Table(getSkin());
            left.add(next).top().row();
            left.add(powers).bottom();

            add(nameLabel).colspan(2);
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
        super.setDebug(Util.setDebug(debug, nameLabel, next, powers, area));
    }

    public void setPreview(boolean preview){
        isPreview = preview;
        area.setPreview(preview);
        setUpBoard();
    }

    public void update(YokelGameBoard board){
        if(board != null){
            area.updateData(board);
            powers.updateQueue(blockToGameBlocks(board.getPowers()));
            setUpNext(board.fetchCurrentNextPiece());
        }
    }

    private void setUpNext(YokelPiece piece){
        if(piece != null){
            next.setData(piece.toString());
        }
    }

    public void setPlayerLabel(String data){
        if(data != null){
            if(nameLabel == null){
                nameLabel = new GameNameLabel(getSkin());
            }
            nameLabel.setData(data);
        } else {
            nameLabel = null;
        }
        setUpBoard();
    }

    public void setLeftBarOrientation(boolean isLeft){
        isLeftBar = isLeft;
        setUpBoard();
    }

    @Override
    public float getPrefHeight() {
        if(isPreview){
            return blockPrevHeight * YokelGameBoard.MAX_PLAYABLE_ROWS + blockPrevHeight;
        } else {
            return blockHeight * YokelGameBoard.MAX_PLAYABLE_ROWS + 40;
        }
    }

    @Override
    public float getPrefWidth() {
        if(isPreview){
            return blockPrevWidth * YokelGameBoard.MAX_COLS + blockPrevWidth;
        } else {
            return blockWidth * YokelGameBoard.MAX_COLS + blockWidth * 2;
        }
    }

    public void setBoardNumber(int boardNumber) {
        area.setBoardNumber(boardNumber);
    }

    private Queue<GameBlock> blockToGameBlocks(Queue<Integer> blocks){
        Queue<GameBlock> bs = new Queue<>();
        for(int block : blocks){
            bs.addFirst(Util.getBlock(block, isPreview));
        }
        return bs;
    }
}