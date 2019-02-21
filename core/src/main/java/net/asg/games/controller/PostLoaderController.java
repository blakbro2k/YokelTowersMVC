package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;

/** Thanks to View annotation, this class will be automatically found and initiated.
 *
 * This is the first application's views, it will check if the debug Players will be shown, the UI tester, or the normal login Page
 * shown right after the application starts. It will hide after all assests are
 * loaded. */
@View(id = "postload", value = "ui/templates/postloader.lml")
public class PostLoaderController implements ViewRenderer {

    @Override
    public void render(Stage stage, final float delta) {
        stage.act(delta);
        stage.draw();
    }
}


