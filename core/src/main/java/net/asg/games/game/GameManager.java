package net.asg.games.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.config.AutumnMessage;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.lml.annotation.LmlActor;

import net.asg.games.controller.UITestController;

@Component
public class GameManager {
    @Inject private InterfaceService interfaceService;
    @Inject private UITestController uiView;

    @Initiate
    @SuppressWarnings("unchecked")
    private void loadGameData() {

    }

    public void initiateGame() {
        System.out.println("initiateGame");
        System.out.println("uiView=" + uiView);
        System.out.println("uiView.bashBlockImage=" + uiView.bashBlockImage);

        uiView.yBlockImage.setDrawable(interfaceService.getSkin(), "Y_block");
        uiView.oBlockImage.setDrawable(interfaceService.getSkin(), "O_block");
        uiView.kBlockImage.setDrawable(interfaceService.getSkin(), "K_block");
        uiView.eBlockImage.setDrawable(interfaceService.getSkin(), "E_block");
        uiView.lBlockImage.setDrawable(interfaceService.getSkin(), "L_block");
        uiView.bashBlockImage.setDrawable(interfaceService.getSkin(), "Bash_block");
    }

    public void update(){}
    public void handleMoveRight(){}
    public void handleMoveLeft(){}
    public void handleSetPiece(){}
    public String[] getBoardState(){ return null;}
}
