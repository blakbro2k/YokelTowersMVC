package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.kotcrab.vis.ui.widget.VisLabel;

import net.asg.games.provider.actors.GameNameLabel;
import net.asg.games.provider.actors.GamePlayerList;
import net.asg.games.provider.actors.GameTableList;
import net.asg.games.provider.tags.GameTableListLmlTag;
import net.asg.games.service.SessionService;
import net.asg.games.utils.YokelUtilities;

@View(id = ControllerNames.ROOM_VIEW, value = "ui/templates/room.lml")
public class RoomController implements ViewRenderer, ActionContainer {
    @Inject private InterfaceService interfaceService;
    @Inject private SessionService sessionService;

    @LmlActor("playersList") private GamePlayerList playersList;
    @LmlActor("tableList") private GameTableList tableList;
    @LmlActor("roomName") private VisLabel roomName;
    @LmlActor("1:nameTag") private GameNameLabel nameTagOne;
    @LmlActor("2:nameTag") private GameNameLabel nameTagTwo;

    private float refresh = 500;

    @Override
    public void render(Stage stage, float delta) {
        roomName.setText(sessionService.getCurrentRoomName());
        //nameTagOne.setData();

        if(++refresh > 300){
            refresh = 0;
            try {
                playersList.updatePlayerList(sessionService.asyncGetPlayerAllRequest());
                sessionService.asyncPlayerAllRequest();
                tableList.updateTableList(sessionService.asyncGetTableAllRequest());
                GameTableListLmlTag.setUpListeners(interfaceService.getParser(), tableList.getRoomsButtons());
                sessionService.asyncTableAllRequest();
            } catch (Exception e) {
                e.printStackTrace();
                sessionService.showError(e);
            }
        }
        stage.act(delta);
        stage.draw();
    }

    @LmlAction("requestAllPlayers")
    public Array<String> requestAllPlayers() {
        try{
            return YokelUtilities.toPlainTextArray(sessionService.getAllPlayers());
        } catch (Exception e){
            e.printStackTrace();
            sessionService.showError(e);
            return GdxArrays.newArray();
        }
    }

    @LmlAction("requestRandomSeat")
    public void requestRandomSeat() {
        try{
            sessionService.asyncTableSitRequest(1,0);
        } catch (Exception e){
            e.printStackTrace();
            sessionService.showError(e);
        }
    }
}