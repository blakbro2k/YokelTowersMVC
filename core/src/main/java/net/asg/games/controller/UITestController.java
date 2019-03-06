package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.GameManager;
import net.asg.games.service.NetworkService;
import net.asg.games.service.UserInterfaceService;

@View(id = "uitest", value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
    @Inject private NetworkService networkService;
    @Inject private GameManager gameManager;

    @LmlActor("Y_block") public Image yBlockImage;
    @LmlActor("O_block") public Image oBlockImage;
    @LmlActor("K_block") public Image kBlockImage;
    @LmlActor("E_block") public Image eBlockImage;
    @LmlActor("L_block") public Image lBlockImage;
    @LmlActor("Bash_block") public Image bashBlockImage;
    @LmlActor("defense_Y_block") public AnimatedImage defenseYBlockImage;
    @LmlActor("defense_O_block") public AnimatedImage defenseOBlockImage;
    @LmlActor("defense_K_block") public AnimatedImage defenseKBlockImage;
    @LmlActor("defense_E_block") public AnimatedImage defenseEBlockImage;
    @LmlActor("defense_L_block") public AnimatedImage defenseLBlockImage;
    @LmlActor("defense_Bash_block") public AnimatedImage defenseBashBlockImage;
    @LmlActor("power_Y_block") public AnimatedImage powerYBlockImage;
    @LmlActor("power_O_block") public AnimatedImage powerOBlockImage;
    @LmlActor("power_K_block") public AnimatedImage powerKBlockImage;
    @LmlActor("power_E_block") public AnimatedImage powerEBlockImage;
    @LmlActor("power_L_block") public AnimatedImage powerLBlockImage;
    @LmlActor("power_bash_block") public AnimatedImage powerBashBlockImage;
    @LmlActor("Y_block_Broken") public AnimatedImage brokenYBlockImage;
    @LmlActor("O_block_Broken") public AnimatedImage brokenOBlockImage;
    @LmlActor("K_block_Broken") public AnimatedImage brokenKBlockImage;
    @LmlActor("E_block_Broken") public AnimatedImage brokenEBlockImage;
    @LmlActor("L_block_Broken") public AnimatedImage brokenLBlockImage;
    @LmlActor("Bash_block_Broken") public AnimatedImage brokenBashBlockImage;
    @LmlActor("stone") public Image stoneBlockImage;

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
