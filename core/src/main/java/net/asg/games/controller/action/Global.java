package net.asg.games.controller.action;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.controller.ControllerNames;
import net.asg.games.controller.dialog.GameController;
import net.asg.games.provider.actors.GameTableList;
import net.asg.games.service.SessionService;
import net.asg.games.utils.GlobalConstants;
import net.asg.games.utils.PostLoader;

/**
 * Since this class implements ActionContainer and is annotated with ViewActionContainer, its methods will be reflected
 * and available in all LML templates. Note that this class is a component like any other, so it can inject any fields,
 * use Initiate-annotated methods, etc.
 */
@ViewActionContainer("global")
public class Global implements ActionContainer {
    @Inject private SessionService sessionService;
    @Inject private InterfaceService interfaceService;

    /**
     * This is a mock-up method that does nothing. It will be available in LML templates through "close" (annotation
     * argument) and "noOp" (method name) IDs.
     */
    @LmlAction("close")
    public void noOp() {
    }

    @LmlAction("quitToRoom")
    public void quitToRoom() {
        interfaceService.show(sessionService.getView(GlobalConstants.ROOM_VIEW));
    }

    @LmlAction("isUITest")
    public boolean isUITest(){
        return PostLoader.UI_TEST.equalsIgnoreCase(PostLoader.getInstance().getPreLoader());
    }

    @LmlAction("isDebug")
    public boolean isDebug(){
        System.out.println("isDebugResult  called.");
        return PostLoader.DEBUG.equalsIgnoreCase(PostLoader.getInstance().getPreLoader());
    }

    @LmlAction("getCurrentPlayerName")
    public String getCurrentPlayerName(){
        return sessionService.getCurrentUserName();
    }


    @LmlAction("requestJoinTable")
    public void requestJoinTable(Button button) {
        try{
            sessionService.asyncTableSitRequest(GameTableList.getTableNumberFromButton(button), GameTableList.getSeatNumberFromButton(button));
            interfaceService.showDialog(GameController.class);
        } catch (Exception e){
            e.printStackTrace();
            sessionService.showError(e);
        }
    }
}
