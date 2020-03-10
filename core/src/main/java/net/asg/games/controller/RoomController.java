package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;

import net.asg.games.controller.dialog.ErrorController;
import net.asg.games.provider.actors.GamePlayerList;
import net.asg.games.service.SessionService;
import net.asg.games.utils.Util;

@View(id = "room", value = "ui/templates/room.lml")
public class RoomController implements ViewRenderer, ActionContainer {
    @Inject private InterfaceService interfaceService;
    @Inject private SessionService sessionService;

    @LmlActor("playersList") private GamePlayerList playersList;
    private float refresh = 0;

    @Override
    public void render(Stage stage, float delta) {
        //System.out.println(interfaceService.getHidingActionProvider());
        System.out.println(refresh);
        if(++refresh > 100){
            refresh=0;
           // playersList.add(new Label("kjkjk", interfaceService.getSkin())).row();
            try {
                //System.out.println(Util.toPlainTextArray(sessionService.getAllPlayers()));
                playersList.updatePlayerList(sessionService.getAllPlayers());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stage.act(delta);
        stage.draw();
    }

    @LmlAction("requestAllPlayers")
    public Array<String> requestAllPlayers() {
        try{
            return Util.toPlainTextArray(sessionService.getAllPlayers());
        } catch (Exception e){
            e.printStackTrace();
            sessionService.setCurrentError(e.getCause(), e.getMessage());
            interfaceService.showDialog(ErrorController.class);
            return GdxArrays.newArray();
        }
    }
}