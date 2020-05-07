package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelObjectFactory;

/**
 * Created by eboateng on 3/19/2018.
 */

public class GameBoard extends Table {
    private static float MAX_KEY_HOLD_TIME = 15;

    private GameNameLabel nameLabel;
    private GameNextPieceQueue next;
    private GamePowersQueue powers;
    private GameBlockArea area;

    private boolean isPreview = false;
    private boolean isLeftBar = true;

    public GameBoard(Skin skin, YokelObjectFactory factory) {
        super(skin);

        initialize(skin, factory);
        invalidateHierarchy();
    }

    private void initialize(Skin skin, YokelObjectFactory factory) {
        nameLabel = new GameNameLabel(skin);
        next = new GameNextPieceQueue(skin);
        powers = new GamePowersQueue(skin);
        area = new GameBlockArea(factory);

        setUpBoard(isPreview);
    }

    private void setUpBoard(boolean preview){
        clearChildren();

        Table right = new Table(getSkin());
        right.add(area);

        if(preview){
            add(nameLabel);
            row();
            add(right);
        } else {
            Table left = new Table(getSkin());
            left.add(next).row();
            left.add(powers);

            add(nameLabel).colspan(2);
            row();

            if(isLeftBar){
                add(left);
                add(right);
            } else {
                add(right);
                add(left);
            }
        }
    }

    public void setPreview(boolean preview){
        isPreview = preview;
        setUpBoard(isPreview);
    }

    public void update(YokelGameBoard board){
        if(board != null){
            area.updateData(board);
            powers.updateQueue(board.getPowers());
        }
    }

    public void setLeftBarOrientation(boolean isLeft){
        isLeftBar = isLeft;
    }

    public void setBoardNumber(int boardNumber) {
        area.setBoardNumber(boardNumber);
    }
}