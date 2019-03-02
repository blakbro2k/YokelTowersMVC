package net.asg.games.game;

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

    @Initiate
    @SuppressWarnings("unchecked")
    private void loadGameData() {

    }

    public void initiateGame() {
        uiService.loadDrawable(uiView.yBlockImage);
        uiService.loadDrawable(uiView.oBlockImage);
        uiService.loadDrawable(uiView.kBlockImage);
        uiService.loadDrawable(uiView.eBlockImage);
        uiService.loadDrawable(uiView.lBlockImage);
        uiService.loadDrawable(uiView.bashBlockImage);
        uiService.loadDrawable(uiView.defenseYBlockImage);
        uiService.loadDrawable(uiView.defenseOBlockImage);
        uiService.loadDrawable(uiView.defenseKBlockImage);
        uiService.loadDrawable(uiView.defenseEBlockImage);
        uiService.loadDrawable(uiView.defenseLBlockImage);
        uiService.loadDrawable(uiView.defenseBashBlockImage);
        uiService.loadDrawable(uiView.powerYBlockImage);
        uiService.loadDrawable(uiView.powerOBlockImage);
        uiService.loadDrawable(uiView.powerKBlockImage);
        uiService.loadDrawable(uiView.powerEBlockImage);
        uiService.loadDrawable(uiView.powerLBlockImage);
        uiService.loadDrawable(uiView.powerBashBlockImage);
        uiService.loadDrawable(uiView.brokenYBlockImage);
        uiService.loadDrawable(uiView.brokenOBlockImage);
        uiService.loadDrawable(uiView.brokenKBlockImage);
        uiService.loadDrawable(uiView.brokenEBlockImage);
        uiService.loadDrawable(uiView.brokenLBlockImage);
        uiService.loadDrawable(uiView.brokenBashBlockImage);
    }

    public void update(){}
    public void handleMoveRight(){}
    public void handleMoveLeft(){}
    public void handleSetPiece(){}
    public String[] getBoardState(){ return null;}


}