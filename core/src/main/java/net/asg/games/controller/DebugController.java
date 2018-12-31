package net.asg.games.controller;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.parser.action.ActionContainer;

@View(id = "debug", value = "ui/templates/debug.lml")
public class DebugController extends ApplicationAdapter implements ViewRenderer, ActionContainer {
    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void changeScreens(){
        //setView
    }
}
