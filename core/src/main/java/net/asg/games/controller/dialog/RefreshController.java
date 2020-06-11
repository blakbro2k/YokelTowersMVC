package net.asg.games.controller.dialog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogController;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.controller.ControllerNames;
import net.asg.games.service.SessionService;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = ControllerNames.REFRESH_DIALOG, value = "ui/templates/dialogs/refresh.lml")
public class RefreshController implements ViewDialogController {
    @Inject private SessionService sessionService;

    /** @return array of available GUI scales. */
    @LmlAction("getCurrentErrorMessage")
    public String getCurrentErrorMessage() {
        return sessionService.getCurrentError();
    }

    @Override
    public void show(Stage stage) {

    }

    @Override
    public String getId() {
        return ControllerNames.REFRESH_DIALOG;
    }

    @Override
    public void destroyDialog() {

    }
}