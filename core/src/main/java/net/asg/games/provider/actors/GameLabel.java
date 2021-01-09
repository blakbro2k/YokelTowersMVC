package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameLabel extends Label {
    public GameLabel(CharSequence text, Skin skin) {
        super(text, skin);
    }

    public GameLabel(CharSequence text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public GameLabel(CharSequence text, Skin skin, String fontName, Color color) {
        super(text, skin, fontName, color);
    }

    public GameLabel(CharSequence text, Skin skin, String fontName, String colorName) {
        super(text, skin, fontName, colorName);
    }

    public GameLabel(CharSequence text, LabelStyle style) {
        super(text, style);
    }

    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
