package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.kotcrab.vis.ui.widget.VisTextButton;

import net.asg.games.controller.dialog.ErrorController;
import net.asg.games.service.SessionService;
import net.asg.games.utils.GlobalConstants;
import net.asg.games.utils.YokelUtilities;

@View(id = GlobalConstants.LOUNGE_VIEW, value = GlobalConstants.LOUNGE_VIEW_PATH)
public class LoungeController implements ViewRenderer, ActionContainer {
    @Inject private SessionService sessionService;
    @Inject private InterfaceService interfaceService;

    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }

    @LmlAction("requestAllLounges")
    public Array<String> requestAllLounges() {
        try{
            return YokelUtilities.toPlainTextArray(sessionService.getAllLounges());
        } catch (Exception e){
            e.printStackTrace();
            sessionService.setCurrentError(e.getCause(), e.getMessage());
            interfaceService.showDialog(ErrorController.class);
            return GdxArrays.newArray();
        }
    }

    @LmlAction("selectLounge")
    public void selectLounge(VisTextButton button) {
        try{
            if(button != null){
                sessionService.setCurrentLoungeName(button.getName());
                sessionService.setCurrentRoomName(button.getLabel().getText().toString());
                sessionService.asyncTableAllRequest();
                interfaceService.show(sessionService.getView(GlobalConstants.ROOM_VIEW));
            }
        } catch (Exception e){
            e.printStackTrace();
            sessionService.showError(e);
        }
    }
}
