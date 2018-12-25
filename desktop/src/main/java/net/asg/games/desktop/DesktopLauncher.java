package net.asg.games.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.czyzby.autumn.fcs.scanner.DesktopClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import com.github.czyzby.websocket.CommonWebSockets;

import net.asg.games.YokelTowersMVC;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new AutumnApplication(new DesktopClassScanner(), YokelTowersMVC.class),
                getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "yokel-towers-mvc";
        configuration.width = YokelTowersMVC.WIDTH;
        configuration.height = YokelTowersMVC.HEIGHT;
        for (int size : new int[] { 128, 64, 32, 16 }) {
            configuration.addIcon("images/libgdx" + size + ".png", FileType.Internal);
        }
        // Initiating web sockets module:
        CommonWebSockets.initiate();
        return configuration;
    }
}