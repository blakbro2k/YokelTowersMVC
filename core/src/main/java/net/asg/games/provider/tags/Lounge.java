package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.VisUI;

public class Lounge extends Table {
    public Lounge() {
        super(VisUI.getSkin());
    }

    public Lounge(Skin skin) {
        super(skin);
    }
}
