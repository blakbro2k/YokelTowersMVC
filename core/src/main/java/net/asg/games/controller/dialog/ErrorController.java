package net.asg.games.controller.dialog;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.kotcrab.vis.ui.VisUI.SkinScale;

import net.asg.games.service.ScaleService;
import net.asg.games.service.SessionService;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = "error", value = "ui/templates/dialogs/error.lml")
public class ErrorController implements ActionContainer {
    @Inject private SessionService sessionService;

    /** @return array of available GUI scales. */
    @LmlAction("getCurrentErrorMessage")
    public String getCurrentErrorMessage() {
        return sessionService.getCurrentError();
    }
}