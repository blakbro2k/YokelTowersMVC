package net.asg.games.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.annotation.OnMessage;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewRenderer;
import com.github.czyzby.autumn.mvc.config.AutumnMessage;
import com.github.czyzby.autumn.mvc.stereotype.Asset;
import com.github.czyzby.autumn.mvc.stereotype.View;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.Dtd;
import com.kotcrab.vis.ui.widget.VisProgressBar;

import net.asg.games.service.UserInterfaceService;
import net.asg.games.utils.UIUtil;

import java.io.Writer;

/** Thanks to View annotation, this class will be automatically found and initiated.
 *
 * This is the first application's views, shown right after the application starts. It will hide after all assests are
 * loaded. */
@View(value = "ui/templates/loading.lml", first = true)
public class LoadingController implements ViewRenderer {
    /** Kiwi logger for this class. */
    private static final Logger LOGGER = LoggerService.forClass(LoadingController.class);

    /** Will be injected automatically. Manages assets. Used to display loading progress. */
    @Inject private AssetService assetService;
    @Inject private InterfaceService interfaceService;
    @Inject private UserInterfaceService uiService;

    /** This is a widget injected from the loading.lml template. "loadingBar" is its ID. */
    @LmlActor("loadingBar") private VisProgressBar loadingBar;
    @Asset("ui/game/game.atlas") private TextureAtlas gameAtlas;

    private boolean regionsAssigned;
    private boolean dtdSaved = true;

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
            UIUtil.getInstance().setFactory(uiService.getFactory());
        }
        if(!dtdSaved){
            saveDtdSchema(Gdx.files.local("dtd/lml.dtd"));
            dtdSaved = true;
        }
    }

    /** Uses current {@link LmlParser} to generate a DTD schema file with all supported tags, macros and attributes.
     * Should be used only during development: DTD allows to validate LML templates during creation (and add content
     * assist thanks to XML support in your IDE), but is not used in any way by the {@link LmlParser} in runtime.
     *
     * @param file path to the file where DTD schema should be saved. Advised to be local or absolute. Note that some
     *            platforms (GWT) do not support file saving - this method should be used on desktop platform and only
     *            during development.
     * @throws GdxRuntimeException when unable to save DTD schema.
     * @see Dtd */
    public void saveDtdSchema(final FileHandle file) {
        //LOGGER.info("Saving DTD information");
        try {
            LmlParser lmlParser = interfaceService.getParser();

            final Writer appendable = file.writer(false, "UTF-8");
            final boolean strict = lmlParser.isStrict();
            lmlParser.setStrict(false); // Temporary setting to non-strict to generate as much tags as possible.
            createDtdSchema(lmlParser, appendable);
            appendable.close();
            lmlParser.setStrict(strict);
        } catch (final Exception exception) {
            throw new GdxRuntimeException("Unable to save DTD schema.", exception);
        }
    }

    /** This is a utility method that allows you to hook up into DTD generation process or even modify it completely.
     * This method is called by {@link #saveDtdSchema(FileHandle)} after the parser was already set to non-strict. By
     * default, this method calls standard DTD utility method: {@link Dtd#saveSchema(LmlParser, Appendable)}. By
     * overriding this method, you can generate minified schema with
     * {@link Dtd#saveMinifiedSchema(LmlParser, Appendable)} or manually append some customized tags and attributes
     * using {@link Appendable} API.
     *
     * <p>
     * If you want to generate DTD schema file for your LML parser, use {@link #saveDtdSchema(FileHandle)} method
     * instead.
     *
     * @param parser its schema will be generated.
     * @param appendable a reference to target file.
     * @see #saveDtdSchema(FileHandle)
     * @throws Exception if your saving method throws any exception, it will wrapped with {@link GdxRuntimeException}
     *             and rethrown. */
    protected void createDtdSchema(final LmlParser parser, final Appendable appendable) throws Exception {
        //LOGGER.info("Creating DTD Schema");
        Dtd.saveSchema(parser, appendable);
    }
}


