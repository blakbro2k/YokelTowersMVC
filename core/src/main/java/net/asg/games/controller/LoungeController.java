package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;

@View(id = "lounge", value = "ui/templates/lounge.lml")
public class LoungeController implements ViewRenderer {
    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }
}
