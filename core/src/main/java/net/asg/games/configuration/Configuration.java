package net.asg.games.configuration;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.mvc.component.ui.SkinService;
import com.github.czyzby.autumn.mvc.stereotype.preference.AvailableLocales;
import com.github.czyzby.autumn.mvc.stereotype.preference.I18nBundle;
import com.github.czyzby.autumn.mvc.stereotype.preference.I18nLocale;
import com.github.czyzby.autumn.mvc.stereotype.preference.LmlMacro;
import com.github.czyzby.autumn.mvc.stereotype.preference.LmlParserSyntax;
import com.github.czyzby.autumn.mvc.stereotype.preference.Preference;
import com.github.czyzby.autumn.mvc.stereotype.preference.StageViewport;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.MusicEnabled;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.MusicVolume;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.SoundEnabled;
import com.github.czyzby.autumn.mvc.stereotype.preference.sfx.SoundVolume;
import com.github.czyzby.kiwi.util.gdx.asset.lazy.provider.ObjectProvider;
import com.github.czyzby.lml.parser.LmlSyntax;
import com.github.czyzby.lml.util.Lml;
import com.github.czyzby.lml.vis.parser.impl.VisLmlSyntax;
import com.github.czyzby.lml.vis.util.ColorPickerContainer;
import com.kotcrab.vis.ui.VisUI;

import net.asg.games.YokelTowersMVC;
import net.asg.games.provider.attributes.GameBlockAreaDataLmlAttribute;
import net.asg.games.provider.attributes.GameBlockAreaNumberLmlAttribute;
import net.asg.games.provider.attributes.GameBlockPreviewLmlAttribute;
import net.asg.games.provider.attributes.GameBlockTypeLmlAttribute;
import net.asg.games.provider.attributes.GamePieceOrientationLmlAttribute;
import net.asg.games.provider.tags.GameBlockAreaLmlTagProvider;
import net.asg.games.provider.tags.GameBlockLmlTagProvider;
import net.asg.games.provider.tags.GameBoardLmlTagProvider;
import net.asg.games.provider.tags.GameClockLmlTagProvider;
import net.asg.games.provider.tags.GameJoinWindowLmlTagProvider;
import net.asg.games.provider.tags.GameLoungeLmlTagProvider;
import net.asg.games.provider.tags.GameNameLabelmlTagProvider;
import net.asg.games.provider.tags.GamePieceLmlTagProvider;
import net.asg.games.provider.tags.GamePlayerIconLmlTagProvider;
import net.asg.games.provider.tags.GamePlayerListLmlTagProvider;
import net.asg.games.provider.tags.GamePowersQueueLmlTagProvider;
import net.asg.games.provider.tags.GameTableListLmlTagProvider;
import net.asg.games.service.ScaleService;
import net.asg.games.utils.Log4LibGDXLoggerService;

/**
 * Thanks to the Component annotation, this class will be automatically found and processed.
 * <p>
 * This is a utility class that configures application settings.
 */
@Component
public class Configuration {
    /**
     * Name of the application's preferences file.
     */
    private static final String PREFERENCES = "yokel-towers-mvc";
    /**
     * Path to the internationalization bundle.
     */
    @I18nBundle
    private final String bundlePath = "i18n/bundle";
    /**
     * Enabling VisUI usage.
     */
    @LmlParserSyntax
    private final LmlSyntax syntax = new VisLmlSyntax();
    /**
     * Parsing macros available in all views.
     */
    @LmlMacro
    private final String globalMacro = "ui/templates/macros/global.lml";
    /**
     * Using a custom viewport provider - Autumn MVC defaults to the ScreenViewport, as it is the only viewport that
     * doesn't need to know application's targeted screen size. This provider overrides that by using more sophisticated
     * FitViewport that works on virtual units rather than pixels.
     */
    @StageViewport
    private final ObjectProvider<Viewport> viewportProvider = new ObjectProvider<Viewport>() {
        @Override
        public Viewport provide() {
            return new FitViewport(YokelTowersMVC.WIDTH, YokelTowersMVC.HEIGHT);
        }
    };

    /**
     * These sound-related fields allow MusicService to store settings in preferences file. Sound preferences will be
     * automatically saved when the application closes and restored the next time it's turned on. Sound-related methods
     * methods will be automatically added to LML templates - see settings.lml template.
     */
    @SoundVolume(preferences = PREFERENCES)
    private final String soundVolume = "soundVolume";
    @SoundEnabled(preferences = PREFERENCES)
    private final String soundEnabled = "soundOn";
    @MusicVolume(preferences = PREFERENCES)
    private final String musicVolume = "musicVolume";
    @MusicEnabled(preferences = PREFERENCES)
    private final String musicEnabledPreference = "musicOn";

    /**
     * These i18n-related fields will allow LocaleService to save game's locale in preferences file. Locale changing
     * actions will be automatically added to LML templates - see settings.lml template.
     */
    @I18nLocale(propertiesPath = PREFERENCES, defaultLocale = "en")
    private final String localePreference = "locale";
    @AvailableLocales
    private final String[] availableLocales = new String[]{"en"};

    /**
     * Setting the default Preferences object path.
     */
    @Preference
    private final String preferencesPath = PREFERENCES;
    //@Skin(fonts = { "ui/neutralizer/skin/font-export.fnt"}, fontNames = { "neutralizer"}, value = "neutralizer")
    //    private final String skinPath = "ui/neutralizer/skin/neutralizer-ui";

    /**
     * Thanks to the Initiate annotation, this method will be automatically invoked during context building. All
     * method's parameters will be injected with values from the context.
     *
     * @param scaleService contains current GUI scale.
     * @param skinService  contains GUI skin.
     */
    @Initiate
    public void initiateConfiguration(final ScaleService scaleService, final SkinService skinService) {
        // Loading default VisUI skin with the selected scale:
        VisUI.load(scaleService.getScale());

        // Registering VisUI skin with "default" name - this skin will be the default one for all LML widgets:
        skinService.addSkin("default", VisUI.getSkin());
        //skinService.addSkin("default", YahooUi.getSkin());

        //skinService.addSkin("default", new Skin(Gdx.files.internal("U:\\YokelTowersMVC\\assets\\ui\\skins\\sgx\\skin\\sgx-ui.json")));
        //skinService.addSkin("default", new Skin(Gdx.files.internal("C:\\Users\\bigg_\\StudioProjects\\YokelTowersMVC\\assets\\ui\\skins\\Neutralizer_UI_Skin\\neutralizerui\\neutralizer-ui.json")));

        // Thanks to this setting, only methods annotated with @LmlAction will be available in views, significantly
        // speeding up method look-up:
        Lml.EXTRACT_UNANNOTATED_METHODS = false;
        setUpLogger();
        addCustomLmlTags();
    }

    private void setUpLogger() {
        Log4LibGDXLoggerService.INSTANCE.setProductionMode(false);
    }

    @Destroy
    public static void destroyColorPicker() {
        ColorPickerContainer.dispose();
    }

    private void addCustomLmlTags() {
        syntax.addTagProvider(new GameLoungeLmlTagProvider(), "lounge");
        syntax.addTagProvider(new GameBoardLmlTagProvider(), "gameboard");
        syntax.addTagProvider(new GameClockLmlTagProvider(), "gameclock");
        syntax.addTagProvider(new GamePlayerIconLmlTagProvider(), "playericon");
        syntax.addTagProvider(new GameBlockAreaLmlTagProvider(), "gameblockarea");
        syntax.addTagProvider(new GameBlockLmlTagProvider(), "gameblock");
        syntax.addTagProvider(new GamePieceLmlTagProvider(), "gamepiece");
        syntax.addTagProvider(new GamePowersQueueLmlTagProvider(), "gamepowers");
        syntax.addTagProvider(new GamePlayerListLmlTagProvider(), "gameplayerlist");
        syntax.addTagProvider(new GameTableListLmlTagProvider(), "gametablelist");
        syntax.addTagProvider(new GameNameLabelmlTagProvider(), "gamenametag");
        syntax.addTagProvider(new GameJoinWindowLmlTagProvider(), "gameJoinButton");

        syntax.addAttributeProcessor(new GameBlockTypeLmlAttribute(), "blocktype");
        syntax.addAttributeProcessor(new GameBlockAreaNumberLmlAttribute(), "areanumber");
        syntax.addAttributeProcessor(new GameBlockAreaDataLmlAttribute(), "blockareadata");
        syntax.addAttributeProcessor(new GameBlockPreviewLmlAttribute(), "preview");
        syntax.addAttributeProcessor(new GamePieceOrientationLmlAttribute(), "left");
    }
}