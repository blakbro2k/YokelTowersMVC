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

import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.service.UserInterfaceService;

@View(id = "uitest", value = "ui/templates/uitester.lml")
public class UITestController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
     @Inject private UserInterfaceService uiService;




    private void loadGameData() {}



    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }


}
