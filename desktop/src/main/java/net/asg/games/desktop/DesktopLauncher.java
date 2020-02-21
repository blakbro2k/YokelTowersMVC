package net.asg.games.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import com.github.czyzby.websocket.CommonWebSockets;

import net.asg.games.YokelTowersMVC;
import net.asg.games.game.managers.GameManager;
import net.asg.games.game.managers.ServerManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.GlobalConstants;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.PostLoader;
import net.asg.games.utils.TestingUtils;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import java.lang.reflect.InvocationTargetException;

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
                    TexturePacker.process(settings, GlobalConstants.SOURCE_ASSETS_FOLDER_PATH,
                            GlobalConstants.TARGET_ASSETS_FOLDER_PATH, GlobalConstants.GAME_ATLAS_NAME);

                    System.exit(0);
                }

                if("-uiTest".equalsIgnoreCase((arg))) {
                    PostLoader.getInstance().setUIPreLoader();
                }
            }
        }

        LwjglApplication app = createApplication();
        app.exit();
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
}

