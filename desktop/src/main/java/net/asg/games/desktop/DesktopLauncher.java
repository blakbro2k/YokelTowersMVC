package net.asg.games.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.Dtd;
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.websocket.CommonWebSockets;

import net.asg.games.YokelTowersMVC;
import net.asg.games.utils.GlobalConstants;
import net.asg.games.utils.PostLoader;

import java.io.Writer;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        if(args != null){
            for (String arg : args) {
                if ("texturepacker".equalsIgnoreCase((arg))) {
                    // Create two run configurations
                    // 1. For texture packing. Pass 'texturepacker' as argument and use desktop/src
                    //    as working directory
                    // 2. For playing game with android/assets as working directory
                    TexturePacker.Settings settings = new TexturePacker.Settings();
                    settings.maxWidth = GlobalConstants.MAX_WIDTH;
                    settings.maxHeight = GlobalConstants.MAX_HEIGHT;
                    TexturePacker.process(settings, "../" + GlobalConstants.SOURCE_ASSETS_FOLDER_PATH,
                            "../" + GlobalConstants.TARGET_ASSETS_FOLDER_PATH, GlobalConstants.GAME_ATLAS_NAME);

                    System.exit(0);
                }

                if("-uiTest".equalsIgnoreCase((arg))) {
                    PostLoader.getInstance().setUIPreLoader();
                }

                if("-dtd".equalsIgnoreCase((arg))) {
                    saveDtdSchema(Gdx.files.local("dtd/lml.dtd"));
                }
            }
        }

        LwjglApplication app = createApplication();
        //app.exit();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new AutumnApplication(new DesktopClassScanner(), YokelTowersMVC.class), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "yokel-towers-mvc";
        configuration.width = YokelTowersMVC.WIDTH;
        configuration.height = YokelTowersMVC.HEIGHT;
        for (int size : new int[] { 128, 64, 32, 16 }) {
            configuration.addIcon("ui/icons/libgdx" + size + ".png", FileType.Internal);
        }

        // Initiating web sockets module:
        CommonWebSockets.initiate();
        return configuration;
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
    private static void saveDtdSchema(final FileHandle file) {
        //LOGGER.info("Saving DTD information");
        try {
            final LmlParser parser = Lml.parser().build();
            final Writer appendable = file.writer(false, "UTF-8");
            final boolean strict = parser.isStrict();
            parser.setStrict(false); // Temporary setting to non-strict to generate as much tags as possible.
            createDtdSchema(parser, appendable);
            appendable.close();
            parser.setStrict(strict);
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
    private static void createDtdSchema(final LmlParser parser, final Appendable appendable) throws Exception {
        //LOGGER.info("Creating DTD Schema");
        Dtd.saveSchema(parser, appendable);
    }
}

