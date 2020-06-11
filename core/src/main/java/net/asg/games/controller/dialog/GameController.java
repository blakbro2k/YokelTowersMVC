package net.asg.games.controller.dialog;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.ViewDialog;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.controller.ControllerNames;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GamePiece;
import net.asg.games.provider.actors.GamePowersQueue;
import net.asg.games.service.SessionService;

/** This is a settings dialog, which can be shown in any views by using "show:settings" LML action or - in Java code -
 * through InterfaceService.showDialog(Class) method. Thanks to the fact that it implements ActionContainer, its methods
 * will be available in the LML template. */
@ViewDialog(id = ControllerNames.GAME_DIALOG, value = "ui/templates/dialogs/game.lml")
public class GameController implements ViewRenderer, ActionContainer {
    @Inject private InterfaceService interfaceService;
    @Inject private SessionService sessionService;

    @LmlActor("1:area") private GameBlockArea area1;
    @LmlActor("2:area") private GameBlockArea area2;
    @LmlActor("3:area") private GameBlockArea area3;
    @LmlActor("4:area") private GameBlockArea area4;
    @LmlActor("5:area") private GameBlockArea area5;
    @LmlActor("6:area") private GameBlockArea area6;
    @LmlActor("7:area") private GameBlockArea area7;
    @LmlActor("8:area") private GameBlockArea area8;
    @LmlActor("1:next") private GamePiece next1;
    @LmlActor("2:next") private GamePiece next2;
    @LmlActor("1:powers") private GamePowersQueue powersQueue1;
    @LmlActor("2:powers") private GamePowersQueue powersQueue2;

    private float refresh = 500;

    @Override
    public void render(Stage stage, float delta) {
        if(++refresh > 300){
            refresh = 0;
            try {
                sessionService.asyncPlayerAllRequest();
                sessionService.asyncTableAllRequest();
            } catch (Exception e) {
                e.printStackTrace();
                sessionService.showError(e);
            }
        }

        stage.act(delta);
        stage.draw();
    }
}