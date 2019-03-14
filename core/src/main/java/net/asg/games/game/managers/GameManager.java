package net.asg.games.game.managers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;

import net.asg.games.controller.UITestController;
import net.asg.games.service.UserInterfaceService;

@Component
public class GameManager {
    @Inject private UserInterfaceService uiService;
    @Inject private UITestController uiView;

    @SuppressWarnings("unchecked")
    private void loadGameData() {}
    public void update(){}
    public void handleMoveRight(){}
    public void handleMoveLeft(){}
    public void handleSetPiece(){}
    public String[] getBoardState(){ return null;}
}