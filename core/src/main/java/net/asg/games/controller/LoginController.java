package net.asg.games.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.parser.impl.attribute.OnChangeLmlAttribute;
import com.kotcrab.vis.ui.widget.VisTextField;

import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.service.SessionService;

@View(id = "login", value = "ui/templates/login.lml")
public class LoginController implements ViewRenderer, ActionContainer {
    private final static OnChangeLmlAttribute onChange = new OnChangeLmlAttribute();

    @Inject
    private SessionService sessionService;

    @LmlActor("username")
    private VisTextField username;

    @LmlActor("password")
    private VisTextField password;

    //@LmlActor("registered")
    //private boolean registered;

    @Override
    public void render(Stage stage, float delta) {
        stage.act(delta);
        stage.draw();
    }

    @LmlAction("registerUser")
    public void registerUser() throws InterruptedException {
        if(sessionService.register(new YokelPlayer(username.getText()))){
            System.out.println("userName=" + username.getText());
            System.out.println("password=" + password.getText());
            //registered = true;
            System.out.println("Registration of " + username.getText() + " successful!!!");
        }
    }
}
