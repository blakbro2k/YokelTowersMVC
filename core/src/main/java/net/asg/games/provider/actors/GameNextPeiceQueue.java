package net.asg.games.provider.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class GameNextPeiceQueue extends Table {
    /*
    private Drawable background;
    private Rectangle bounds = new Rectangle();

    public GameNextPeiceQueue(YokelTowersGame app){
        this.background = app.getAssetsManager().getImageProvider().getNextBackground();
        setBackground(this.background);
        add(app.getAssetsManager().getGameObjectFactory().getClearGameObject());
    }

    public void update(GamePiece piece){
        if(piece != null){
            Util.resetGameBlockActors(getChildren());
            clear();

            add(piece.getTopBlock()).row();
            add(piece.getMiddleBlock()).row();
            add(piece.getBottomBlock());
        }
    }

    private void setBounds() {
        bounds.set(getX(), getY(), getWidth(), getHeight());
        setBounds(getX(), getY(), getWidth(), getHeight());
    }

    public float getPrefWidth() {
        float width = background.getMinWidth();

        if(width < 1){
            float localWidth = Util.getClearBlockWidth();

            if (localWidth > 0){
                width = localWidth * 1;
            }
        }
        return width;
    }

    public float getPrefHeight() {
        float height = background.getMinHeight();

        if(height < 1){
            float localHeight = Util.getClearBlockHeight();

            if (localHeight > 0){
                height = localHeight * 3;
            }
        }
        return height;
    }
    */
}