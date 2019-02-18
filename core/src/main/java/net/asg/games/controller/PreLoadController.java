package net.asg.games.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.config.AutumnMessage;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.kotcrab.vis.ui.widget.VisProgressBar;

import net.asg.games.service.DTDService;
import net.asg.games.utils.PreLoader;

/** Thanks to View annotation, this class will be automatically found and initiated.
 *
 * This is the first application's views, it will check if the debug Players will be shown, the UI tester, or the normal login Page
 * shown right after the application starts. It will hide after all assests are
 * loaded. */
@View(value = "ui/templates/preload.lml", first = true)
public class PreLoadController implements ViewRenderer {
    @Override
    public void render(Stage stage, final float delta) {
        stage.act(delta);
        stage.draw();
    }

    @LmlAction("getPreLoader")
    public String getPreLoader(){
        return PreLoader.getInstance().getPreLoader();
    }
}


