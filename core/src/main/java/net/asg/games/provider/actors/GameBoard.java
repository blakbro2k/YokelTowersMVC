package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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

    public GameBoard(Skin skin, YokelObjectFactory factory) {
        super(skin);

        initialize(skin, factory);
        invalidateHierarchy();
    }

    private void initialize(Skin skin, YokelObjectFactory factory) {
        GameNameLabel nameLabel = new GameNameLabel(skin);
        GameNextPieceQueue next = new GameNextPieceQueue(skin);
        GamePowersQueue powers = new GamePowersQueue(skin);
        GameBlockArea area = new GameBlockArea(factory);

        Table left = new Table(skin);
        Table right = new Table(skin);

        left.add(next).row();
        left.add(powers);

        right.add(area);

        add(nameLabel).colspan(2).row();
        add(left);
        add(right);
    }
}
