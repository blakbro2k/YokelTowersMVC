package net.asg.games.controller.dialog;

import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.parser.action.ActionContainer;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = "createGame", value = "ui/templates/dialogs/createGame.lml")
public class CreateGameController implements ActionContainer {
    // @Inject-annotated fields will be automatically filled with values from the context.
    //@Inject private ScaleService networkService;
}