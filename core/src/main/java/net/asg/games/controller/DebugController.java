package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.service.SessionService;
import net.asg.games.utils.GlobalConstants;

@View(id = GlobalConstants.DEBUG_VIEW, value = GlobalConstants.DEBUG_VIEW_PATH)
public class DebugController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
    @Inject private SessionService sessionService;

    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }

    @LmlAction("requestPlayerRegistration")
    public void requestPlayerRegistration(final Object player) {
        System.out.println("Starting requestPlayers");
    }
}
