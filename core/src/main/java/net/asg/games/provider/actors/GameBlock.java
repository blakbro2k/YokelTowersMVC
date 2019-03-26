package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.utils.Util;


/**
 * Created by Blakbro2k on 3/15/2018.
 */

public class GameBlock extends Actor implements Pool.Poolable{
    private Image uiBlock;
    private boolean isActive;

    public GameBlock(){
        super();
    }

    public void setImage(Image image){

    }

    @Override
    public void act(float delta) {
        uiBlock.act(delta);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        uiBlock.draw(batch, parentAlpha);
    }

    @Override
    public void drawDebug(ShapeRenderer shapes) {
        uiBlock.drawDebug(shapes);
    }

    @Override
    public void reset() {
        //setPosition(0,0);
        isActive = false;
    }
}
