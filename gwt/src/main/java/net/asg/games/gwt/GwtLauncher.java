package net.asg.games.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.czyzby.autumn.gwt.scanner.GwtClassScanner;
import com.github.czyzby.autumn.mvc.application.AutumnApplication;
import com.github.czyzby.websocket.GwtWebSockets;

import net.asg.games.YokelTowersMVC;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(YokelTowersMVC.WIDTH, YokelTowersMVC.HEIGHT);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        GwtWebSockets.initiate();
        return new AutumnApplication(new GwtClassScanner(), YokelTowersMVC.class);
    }
}