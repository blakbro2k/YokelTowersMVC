package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;

import net.asg.games.utils.YokelUtilities;

public class GameIcon extends ImageButton implements Disposable {
    private static final String ICON_ATTR_NAME = "player_icon";
    private int currentIconNumber = 1;
    private ObjectMap<String, ImageButtonStyle> styles = GdxMaps.newObjectMap();

    public GameIcon(ImageButtonStyle imageButtonStyle, Skin skin){
        super(imageButtonStyle);
        setSkin(skin);
        addListener(new ClickListener() {
            public void clicked (InputEvent event, float x, float y) {
                cycleIcon();
            }
        });
    }

    public void dispose(){
        if(styles != null){
            styles.clear();
        }
    }

    public static ImageButtonStyle getGameDefaultIconStyle(Skin skin){
        return getGameIconStyle(skin, 1);
    }

    private static ImageButtonStyle getGameIconStyle(Skin skin, int currentIconNumber){
        if(skin == null){
            throw new GdxRuntimeException("Skin cannot be null to set Default Icon Style.");
        }
        Drawable icon = skin.getDrawable(ICON_ATTR_NAME + currentIconNumber);
        return new ImageButtonStyle(icon, icon, icon, icon, icon, icon);
    }

    private void setDrawable(){
        String key = ICON_ATTR_NAME + currentIconNumber;
        ImageButtonStyle style = styles.get(key);
        if(style == null){
            style = getGameIconStyle(getSkin(), currentIconNumber);
            styles.put(key, style);
        }
        YokelUtilities.setSizeFromDrawable(this, style.imageUp);
        setStyle(style);
    }

    private void cycleIcon(){
        if(++currentIconNumber > 18){
            currentIconNumber = 1;
        }
        setDrawable();
    }

    void setIconNumber(int num){
        currentIconNumber = num;
        setDrawable();
    }

    public int getCurrentIconNumber(){
        return currentIconNumber;
}

    public String getIconAttrName(){
        return ICON_ATTR_NAME + currentIconNumber;
    }
}