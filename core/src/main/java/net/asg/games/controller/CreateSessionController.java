package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;

import net.asg.games.utils.GlobalConstants;

/** Thanks to View annotation, this class will be automatically found and initiated.
 *
 * This is the first application's views, it will check if the debug Players will be shown, the UI tester, or the normal login Page
 * shown right after the application starts. It will hide after all assests are
 * loaded. */
@View(id = GlobalConstants.CREATE_SESSION_VIEW, value = GlobalConstants.CREATE_SESSION_VIEW_PATH)
public class CreateSessionController implements ViewRenderer {

    @Override
    public void render(Stage stage, final float delta) {
        stage.act(delta);
        stage.draw();
    }
}


