package net.asg.games.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.config.AutumnMessage;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.lml.annotation.LmlActor;
import com.kotcrab.vis.ui.widget.VisProgressBar;

import net.asg.games.service.DTDService;

/** Thanks to View annotation, this class will be automatically found and initiated.
 *
 * This is the first application's views, shown right after the application starts. It will hide after all assests are
 * loaded. */
@View(value = "ui/templates/loading.lml", first = true)
public class LoadingController implements ViewRenderer {
    /** Will be injected automatically. Manages assets. Used to display loading progress. */
    @Inject private AssetService assetService;
    @Inject private InterfaceService interfaceService;
    @Inject private DTDService dtdService;

    /** This is a widget injected from the loading.lml template. "loadingBar" is its ID. */
    @LmlActor("loadingBar") private VisProgressBar loadingBar;
    @Asset("ui/game/game.atlas") private TextureAtlas gameAtlas;

    private boolean regionsAssigned;
    private boolean dtdSaved;

    // Since this class implements ViewRenderer, it can modify the way its views is drawn. Additionally to drawing the
    // stage, this views also updates assets manager and reads its progress.
    @Override
    public void render(final Stage stage, final float delta) {
        assetService.update();
        loadingBar.setValue(assetService.getLoadingProgress());
        stage.act(delta);
        stage.draw();
    }

    @OnMessage(AutumnMessage.ASSETS_LOADED)
    private void assignRegions() {
        if (!regionsAssigned) {
            regionsAssigned = true;
            interfaceService.getSkin().addRegions(gameAtlas);
        }
        if(!dtdSaved){
            dtdService.saveDtdSchema(Gdx.files.local("core/lml.dtd"));
            dtdSaved = true;
        }
    }
}


