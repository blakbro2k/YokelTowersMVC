package net.asg.games.provider;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;

public class Board extends Table {
    public Board() {
        super(VisUI.getSkin());
    }

    public Board(Skin skin) {
        super(skin);
    }
}
