package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.service.NetworkService;
import net.asg.games.service.UserInterfaceService;

@View(id = "uitest", value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
    @Inject private NetworkService networkService;
    @Inject private UserInterfaceService uiService;

    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.addActor(uiService.createAnimatedImage("defense_K_block", true));
        stage.draw();
    }

    //@LmlAction("requestPlayerRegistration")
    public void requestPlayerRegistration(final Object player) {
        System.out.println("Starting requestPlayers");

        //networkService.requestDebugPlayersFromServer();
        //networkService.requestDebugPlayersFromServer();
    }
}
