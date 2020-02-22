package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.service.SessionService;

@View(id = "lounge", value = "ui/templates/lounge.lml")
public class LoungeController implements ViewRenderer {
    @Inject
    private SessionService sessionService;

    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }

    @LmlAction("requestAllLounges")
    public Array<YokelLounge> requestAllLounges() throws InterruptedException {
        return sessionService.getAllLounges();
    }
}
