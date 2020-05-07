package net.asg.games.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Version;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.kotcrab.vis.ui.VisUI;

public class YahooUi extends VisUI {
    /** Current YahooUi version, does not include SNAPSHOT even if this version is snapshot. */
    public static final String VERSION = "1.0.0";

    private static final String TARGET_GDX_VERSION = "1.9.9";
    private static boolean skipGdxVersionCheck = false;

    private static int defaultTitleAlign = Align.left;

    private static YahooUi.SkinScale scale;
    private static Skin skin;

    /** Defines possible built-in skin scales. */
    public enum SkinScale {
        /** Standard YahooUi skin */
        X1("net/asg/games/ui/skin/x1/uiskin.json", "default"),
        /** YahooUi skin 2x upscaled */
        X2("net/asg/games/ui/skin/x2/uiskin.json", "x2");

        private final String classpath;
        private final String sizesName;

        SkinScale (String classpath, String sizesName) {
            this.classpath = classpath;
            this.sizesName = sizesName;
        }

        public FileHandle getSkinFile () {
            return Gdx.files.classpath(classpath);
        }

        public String getSizesName () {
            return sizesName;
        }
    }

    private static void checkBeforeLoad () {
        if (skin != null) throw new GdxRuntimeException("VisUI cannot be loaded twice");
        if (skipGdxVersionCheck == false && Version.VERSION.equals(TARGET_GDX_VERSION) == false) {
            Gdx.app.log("YahooUi", "Warning, using invalid libGDX version for YahooUi " + VERSION + ".\n" +
                    "You are using libGDX " + Version.VERSION + " but you need " + TARGET_GDX_VERSION + ". This may cause " +
                    "unexpected problems and runtime exceptions.");
        }
    }
}
