package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;

@View(id = "login", value = "ui/templates/login.lml")
public class LoginController implements ViewRenderer {
    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }
}
