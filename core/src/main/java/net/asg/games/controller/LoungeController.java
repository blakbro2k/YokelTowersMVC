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

import net.asg.games.controller.dialog.ErrorController;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.service.SessionService;
import net.asg.games.utils.Util;

@View(id = "lounge", value = "ui/templates/lounge.lml")
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
            //interfaceService.showDialog(LoadingController.class);
            Array<String> lounges = Util.toPlainTextArray(sessionService.getAllLounges());
            //interfaceService.destroyDialog(LoadingController.class);
            return lounges;
        } catch (Exception e){
            e.printStackTrace();
            sessionService.setCurrentError(e.getCause(), e.getMessage());
            interfaceService.showDialog(ErrorController.class);
            return GdxArrays.newArray();
        }
    }
}
