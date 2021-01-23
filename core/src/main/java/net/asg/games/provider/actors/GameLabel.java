package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StringBuilder;

public class GameLabel extends Label {
    private Array<Label> charLabels;
    private boolean isSingle;
    private boolean isWhole;

    public GameLabel(CharSequence text, Skin skin) {
        super(text, skin);
        if(text == null) throw new GdxRuntimeException("Text cannot be null");
        StringBuilder test = getText();
        for(int i = 0; i < text.length(); i++){
            charLabels.add(new Label(text.charAt(i) + "", skin));
        }
    }

    public void draw (Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
