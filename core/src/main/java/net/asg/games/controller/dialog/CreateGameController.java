package net.asg.games.controller.dialog;

import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisSelectBox;

import net.asg.games.controller.ControllerNames;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GamePlayerList;
import net.asg.games.service.SessionService;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = ControllerNames.CREATE_GAME_DIALOG, value = "ui/templates/dialogs/createGame.lml")
public class CreateGameController implements ActionContainer {
    @Inject private InterfaceService interfaceService;
    @Inject private SessionService sessionService;

    @LmlActor("accessType") private VisSelectBox accessType;
    @LmlActor("isRated") private VisCheckBox isRated;
    @LmlActor("playersList") private GamePlayerList playersList;

    @LmlAction("createGame")
    public void createGame() {
        try{
            sessionService.asyncCreateGameRequest(getAccessType(accessType), isRated.isChecked());
            interfaceService.destroyDialog(CreateGameController.class);
        } catch (Exception e){
            e.printStackTrace();
            sessionService.showError(e);
        }
    }

    private YokelTable.ACCESS_TYPE getAccessType(VisSelectBox accessType){
        return YokelTable.ACCESS_TYPE.valueOf(accessType.getSelected().toString().toUpperCase());
    }
}