package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GameIcon extends ImageButton {
    private static final String ICON_ATTR_NAME = "player_icon";
    private int currentIconNumber = 0;

    public GameIcon(ImageButtonStyle imageButtonStyle, Skin skin){
        super(imageButtonStyle);
        setSkin(skin);
        addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                cycleIcon();
            }
        });
    }

    public static ImageButtonStyle getGameDefaultIconStyle(Skin skin){
        if(skin == null){
            throw new GdxRuntimeException("Skin cannot be null to set Default Icon Style.");
        }
        Drawable icon = skin.getDrawable(ICON_ATTR_NAME + 1);
        return new ImageButtonStyle(icon,icon,icon,icon,icon,icon);
    }

    private void cycleIcon(){
        currentIconNumber++;
        if(currentIconNumber > 18){
            currentIconNumber = 1;
        }

        Drawable icon = getSkin().getDrawable(ICON_ATTR_NAME + currentIconNumber);
        setStyle(new ImageButtonStyle(icon,icon,icon,icon,icon,icon));
    }

    public String getIconAttrName(){
        return ICON_ATTR_NAME + currentIconNumber;
    }
}