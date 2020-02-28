package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.kotcrab.vis.ui.widget.VisTextField;

import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.service.SessionService;


@View(id = "login", value = "ui/templates/login.lml")
public class LoginController implements ViewRenderer, ActionContainer {
    @Inject private SessionService sessionService;
    @Inject private InterfaceService interfaceService;

    @LmlActor("username") private VisTextField username;
    @LmlActor("password") private VisTextField password;


    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }

    @LmlAction("registerUser")
    public void registerUser() {
        try{
            if(!username.isEmpty()){
                if(sessionService.register(new YokelPlayer(username.getText()))){
                    interfaceService.show(sessionService.getView("uitest"));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            //interfaceService.show(sessionService.getView("error"));
        }
    }
}
