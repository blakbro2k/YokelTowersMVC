package net.asg.games.provider.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

/** A table that can be dragged and act as a modal window. The top padding is used as the window's title height.
 * <p>
 * The preferred size of a window is the preferred size of the title text and the children as laid out by the table. After adding
 * children to the window, it can be convenient to call {@link #pack()} to size the window to the size of the children.
 * @author Nathan Sweet */
public class GameJoinWindow extends Table {
    static private final Vector2 tmpPosition = new Vector2();
    static private final Vector2 tmpSize = new Vector2();
    static private final int MOVE = 1 << 5;

    private GameWindowStyle style;
    boolean isMovable = true, isModal, isResizable;
    int resizeBorder = 8;
    boolean keepWithinStage = true;
    //Label titleLabel;
    //Table titleTable;

    protected int edge;
    protected boolean dragging;
    private TextButton joinButton;

    public GameJoinWindow (Skin skin) {
        this(skin, skin.get(WindowStyle.class));
    }

    public GameJoinWindow (Skin skin, WindowStyle style) {
        setSkin(skin);
        GameWindowStyle style1 = new GameWindowStyle(skin.getDrawable("panel"), skin.getDrawable("panel"));

        setTouchable(Touchable.enabled);
        setClip(true);

        setStyle(style1);
        setWidth(150);
        setHeight(150);

        joinButton = new TextButton("Join", getSkin());
        add(joinButton);
    }

    public void setStyle (GameWindowStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        setBackground(style.background);
        //titleLabel.setStyle(new Label.LabelStyle(style.titleFont, style.titleFontColor));
        invalidateHierarchy();
    }

    public GameWindowStyle getStyle () {
        return style;
    }

    public void setButtonListener(InputListener listener){
        joinButton.clearListeners();
        joinButton.addListener(listener);
    }

    public void keepWithinStage () {
        if (!keepWithinStage) return;
        Stage stage = getStage();
        if (stage == null) return;
        Camera camera = stage.getCamera();
        if (camera instanceof OrthographicCamera) {
            OrthographicCamera orthographicCamera = (OrthographicCamera)camera;
            float parentWidth = stage.getWidth();
            float parentHeight = stage.getHeight();
            if (getX(Align.right) - camera.position.x > parentWidth / 2 / orthographicCamera.zoom)
                setPosition(camera.position.x + parentWidth / 2 / orthographicCamera.zoom, getY(Align.right), Align.right);
            if (getX(Align.left) - camera.position.x < -parentWidth / 2 / orthographicCamera.zoom)
                setPosition(camera.position.x - parentWidth / 2 / orthographicCamera.zoom, getY(Align.left), Align.left);
            if (getY(Align.top) - camera.position.y > parentHeight / 2 / orthographicCamera.zoom)
                setPosition(getX(Align.top), camera.position.y + parentHeight / 2 / orthographicCamera.zoom, Align.top);
            if (getY(Align.bottom) - camera.position.y < -parentHeight / 2 / orthographicCamera.zoom)
                setPosition(getX(Align.bottom), camera.position.y - parentHeight / 2 / orthographicCamera.zoom, Align.bottom);
        } else if (getParent() == stage.getRoot()) {
            float parentWidth = stage.getWidth();
            float parentHeight = stage.getHeight();
            if (getX() < 0) setX(0);
            if (getRight() > parentWidth) setX(parentWidth - getWidth());
            if (getY() < 0) setY(0);
            if (getTop() > parentHeight) setY(parentHeight - getHeight());
        }
    }

    public void draw (Batch batch, float parentAlpha) {
        //Stage stage = getStage();
        //if (stage.getKeyboardFocus() == null) stage.setKeyboardFocus(this);

        //keepWithinStage();

        /*if (style.stageBackground != null) {
            stageToLocalCoordinates(tmpPosition.set(0, 0));
            stageToLocalCoordinates(tmpSize.set(stage.getWidth(), stage.getHeight()));
            drawStageBackground(batch, parentAlpha, getX() + tmpPosition.x, getY() + tmpPosition.y, getX() + tmpSize.x,
                    getY() + tmpSize.y);
        }*/

        super.draw(batch, parentAlpha);
    }

    protected void drawStageBackground (Batch batch, float parentAlpha, float x, float y, float width, float height) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        //style.stageBackground.draw(batch, x, y, width, height);
    }

    protected void drawBackground (Batch batch, float parentAlpha, float x, float y) {
        super.drawBackground(batch, parentAlpha, x, y);

        ////batch.begin();
        //joinButton.setPosition(4, 300);
        //joinButton.draw(batch, parentAlpha);
        //batch.end();
        // Manually draw the title table before clipping is done.
        //titleTable.getColor().a = getColor().a;
        //float padTop = getPadTop(), padLeft = getPadLeft();
        //titleTable.setSize(getWidth() - padLeft - getPadRight(), padTop);
        //titleTable.setPosition(padLeft, getHeight() - padTop);
        //drawTitleTable = true;
        //titleTable.draw(batch, parentAlpha);
        //drawTitleTable = false; // Avoid drawing the title table again in drawChildren.
    }

    public float getPrefWidth () {
        return super.getPrefWidth();
        //return Math.max(super.getPrefWidth(), titleTable.getPrefWidth() + getPadLeft() + getPadRight());
    }

    public float getPrefHeight () {
        return super.getPrefHeight() + 0;
        //return Math.max(super.getPrefWidth(), titleTable.getPrefWidth() + getPadLeft() + getPadRight());
    }

    /** The style for a window, see {@link Window}.
     * @author Nathan Sweet */
    static public class GameWindowStyle {
        public Drawable previewBackground;
        public Drawable background;
        public Drawable waitingBackground;
        /** Optional */
        public Button.ButtonStyle buttonStyle;

        public GameWindowStyle () {
        }

        public GameWindowStyle (Drawable background, Drawable previewBackground) {
            this.previewBackground = previewBackground;
            this.background = background;
        }

        public GameWindowStyle (Drawable background, Drawable previewBackground, Button.ButtonStyle buttonStyle) {
            this.previewBackground = previewBackground;
            this.background = background;
            this.buttonStyle = buttonStyle;
        }

        public GameWindowStyle (GameWindowStyle style) {
            this.background = style.background;
            this.previewBackground = style.previewBackground;
            this.buttonStyle = style.buttonStyle;
        }
    }
}