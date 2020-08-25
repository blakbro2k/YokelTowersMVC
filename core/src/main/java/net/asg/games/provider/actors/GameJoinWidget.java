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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
public class GameJoinWidget extends Window {
    static private final String WAITING_TEXT = "Waiting for\nMore\nPlayers";
    static private final String JOIN_TEXT = "Join";

    static private final Vector2 tmpPosition = new Vector2();
    static private final Vector2 tmpSize = new Vector2();
    static private final int MOVE = 1 << 5;

    private GameWindowStyle style;
    boolean isMovable = true, isModal, isResizable;
    int resizeBorder = 8;
    boolean keepWithinStage = true;
    boolean isSeated = false;
    boolean preview = false;

    private TextButton joinButton;
    private final TextButton spaceButton;
    private final Label waitingLabel;

    public GameJoinWidget(Skin skin) {
        this(skin, new GameWindowStyle(skin.getDrawable("window-bg")));
    }

    public GameJoinWidget(Skin skin, GameWindowStyle style) {
        super("", skin);
        setSkin(skin);
        setStyle(style);
        setMovable(false);

        setWidth(150);
        setHeight(50);

        pad(4f);

        setTouchable(Touchable.enabled);
        setClip(true);

        joinButton = new TextButton(JOIN_TEXT, getSkin());
        spaceButton = new TextButton("", getSkin());
        spaceButton.setDisabled(true);


        waitingLabel = new Label(WAITING_TEXT, getSkin());
        waitingLabel.setAlignment(Align.center);
        setUpJoinButton();
    }

    private void setUpJoinButton(){
        clearChildren();
        if(isSeated){
            add(waitingLabel).row();
        } else {
            add(joinButton).row();
            add(spaceButton);
        }

    }

    public void setSeated(boolean isSeated){
        this.isSeated = isSeated;
    }

    public void setStyle (GameWindowStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        this.style = style;
        setBackground(style.background);
        invalidateHierarchy();
    }

    public GameWindowStyle getStyle () {
        return style;
    }

    public void setButtonListener(InputListener listener){
        joinButton.clearListeners();
        joinButton.addListener(listener);
    }

    /** The style for a window, see {@link Window}.
     * @author Nathan Sweet */
    static public class GameWindowStyle extends WindowStyle{
        public Drawable previewBackground;
        /** Optional */
        public Button.ButtonStyle buttonStyle;


        public GameWindowStyle (Drawable background) {
            this.background = background;
        }

        public GameWindowStyle (Drawable background, Button.ButtonStyle buttonStyle) {
            this.background = background;
            this.buttonStyle = buttonStyle;
        }

        public GameWindowStyle (GameWindowStyle style) {
            this.background = style.background;
            this.buttonStyle = style.buttonStyle;
        }
    }
}