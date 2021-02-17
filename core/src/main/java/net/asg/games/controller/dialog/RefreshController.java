package net.asg.games.controller.dialog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogController;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlAction;

import net.asg.games.service.SessionService;
import net.asg.games.utils.GlobalConstants;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = GlobalConstants.REFRESH_DIALOG, value = GlobalConstants.REFRESH_DIALOG_PATH)
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
        return GlobalConstants.REFRESH_DIALOG;
    }

    @Override
    public void destroyDialog() {

    }
}