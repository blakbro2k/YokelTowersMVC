package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import net.asg.games.utils.Util;

public class GameIcon extends ImageButton {
    private static final String ICON_ATTR_NAME = "player_icon";
    private int currentIconNumber = 1;

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
        return getGameIconStyle(skin, 1);
    }

    private static ImageButtonStyle getGameIconStyle(Skin skin, int currentIconNumber){
        if(skin == null){
            throw new GdxRuntimeException("Skin cannot be null to set Default Icon Style.");
        }
        Drawable icon = skin.getDrawable(ICON_ATTR_NAME + currentIconNumber);
        return new ImageButtonStyle(icon,icon,icon,icon,icon,icon);
    }

    private void cycleIcon(){
        currentIconNumber++;
        if(currentIconNumber > 18){
            currentIconNumber = 1;
        }

        Drawable icon = getSkin().getDrawable(ICON_ATTR_NAME + currentIconNumber);
        Util.setSizeFromDrawable(this, icon);
        setStyle(new ImageButtonStyle(icon,icon,icon,icon,icon,icon));
    }

    public void setIconNumber(int num){
        currentIconNumber = num;
}

    public int getCurrentIconNumber(){
        return currentIconNumber;
}

    public String getIconAttrName(){
        return ICON_ATTR_NAME + currentIconNumber;
    }
}