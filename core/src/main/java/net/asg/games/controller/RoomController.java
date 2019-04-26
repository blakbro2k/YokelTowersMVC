package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlActor;

@View(id = "room", value = "ui/templates/room.lml")
public class RoomController implements ViewRenderer {

    @LmlActor("location") public Image locationImage;

    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }
}