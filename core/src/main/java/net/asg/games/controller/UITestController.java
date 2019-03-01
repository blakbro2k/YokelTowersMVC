package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.GameManager;
import net.asg.games.service.NetworkService;
import net.asg.games.service.UserInterfaceService;

@View(id = "uitest", value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
    @Inject private NetworkService networkService;
    @Inject private UserInterfaceService uiService;
    @Inject private GameManager gameManager;
    @LmlActor("Y_block") public Image yBlockImage;
    @LmlActor("O_block") public Image oBlockImage;
    @LmlActor("K_block") public Image kBlockImage;
    @LmlActor("E_block") public Image eBlockImage;
    @LmlActor("L_block") public Image lBlockImage;
    @LmlActor("Bash_block") public Image bashBlockImage;
    @LmlActor("Bash_broken_block") public AnimatedImage bashBrokenBlockImage;

    private boolean isInitiated;

    @Override
    public void render(Stage stage, float delta) {
        initiate();
        stage.act(delta);
        //stage.addActor(uiService.createAnimatedImage("defense_K_block", true));
        stage.draw();
    }

    private void initiate(){
        if(!isInitiated){
            isInitiated = true;
            gameManager.initiateGame();
        }
    }

    //@LmlAction("requestPlayerRegistration")
    public void requestPlayerRegistration(final Object player) {
        System.out.println("Starting requestPlayers");

        //networkService.requestDebugPlayersFromServer();
        //networkService.requestDebugPlayersFromServer();
    }
}
