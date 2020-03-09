package net.asg.games.controller.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogController;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlAction;

import net.asg.games.service.SessionService;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = "refresh", value = "ui/templates/dialogs/refresh.lml")
public class LoadingController implements ViewDialogController {
    @Inject private SessionService sessionService;
    @Asset("ui/game/refresh.png") private Texture refresh;


    /** @return array of available GUI scales. */
    @LmlAction("getCurrentErrorMessage")
    public String getCurrentErrorMessage() {
        return sessionService.getCurrentError();
    }

    @Override
    public void show(Stage stage) {
        stage.act(Gdx.graphics.getDeltaTime());

        final Batch batch = stage.getBatch();
        batch.setColor(stage.getRoot().getColor()); // We want the logo to share color alpha with the stage.
        batch.begin();
        batch.draw(refresh, (int) (stage.getWidth() - refresh.getWidth()) / 2,
                (int) (stage.getHeight() - refresh.getHeight()) / 2);
        batch.end();

        stage.draw();
    }

    @Override
    public String getId() {
        return "refresh";
    }

    @Override
    public void destroyDialog() {

    }
}