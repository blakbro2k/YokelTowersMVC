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
    private static ServerManager daemon;

    public static void main(String[] args) {
        String[] args2 = {ServerManager.LOG_LEVEL_ATTR, "trace", ServerManager.DEBUG_ATTR};
        daemon = new ServerManager(args2);

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

                if("-debugPlayers".equalsIgnoreCase((arg))) {
                    PostLoader.getInstance().setDebugPreloader();
                }

                if("-uiTest".equalsIgnoreCase((arg))) {
                    PostLoader.getInstance().setUIPreLoader();
                }
            }
        }

        //createApplication();

        //LwjglApplication app = createApplication();
        //app.exit();


        try {
            YokelPlayer player1 = new YokelPlayer("blakbro2k");
            YokelPlayer player2 = new YokelPlayer("lholtham");
            String roomName = "Eiffel Tower";

            YokelTable table = new YokelTable(1);

            table.getSeat(0).sitDown(player1);
            table.getSeat(2).sitDown(player2);

            System.out.println("table=" + table);
            System.out.println("is Ready=" + table.isTableStartReady());

            if(table.isTableStartReady()){
                GameRunner game = new GameRunner(daemon, table);
                Thread thread = new Thread(game);
                thread.start();

                while(game.isRunning()){
                    System.out.println(game.gameManager.thresh());
                }
            }

            daemon.shutDownServer(-1);
        } catch (Exception e) {
            //System.err.println(e);
            e.printStackTrace();
        } finally {
            daemon.shutDownServer(-1);
        }
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
            configuration.addIcon("ui/icons/libgdx" + size + ".png", FileType.Internal);
        }

        // Initiating web sockets module:
        CommonWebSockets.initiate();
        return configuration;
    }

    private static class GameRunner implements Runnable{
        ServerManager serverManager;
        GameManager gameManager;
        boolean running;
        float tickRate;

        public GameRunner(ServerManager manager, YokelTable table){
            this.serverManager = manager;
            this.gameManager = new GameManager(table);
            //tickRate = daemon.getTickRate();
            tickRate = 60;

        }

        public void setRunning(boolean b){
            this.running = b;
        }
        public boolean isRunning(){
            return this.running;
        }

        private void tick(){
            gameManager.update();
            setRunning(gameManager.isRunning());
            System.out.println(gameManager.printTables());
        }

        public void run() {
            /**
             * while(true)
             *     check for client commands
             *     sanity check client commands
             *     move all entities
             *     resolve collisions
             *     sanity check world data
             *     send updates about the game to the clients
             *     handle client disconnects
             * end while
             */

            setRunning(gameManager.startGame());

            double accumulator = 0.0;
            //The timestep
            final double dt = 1/(double) tickRate;
            long previousTime = System.currentTimeMillis();

            System.out.println("accumulator=" + accumulator);
            System.out.println("dt=" + dt);
            System.out.println("previousTime=" + previousTime);


            //State previous;
            //State current;
            while(running){
                long currentTime = System.currentTimeMillis();
                double frameTime = (currentTime - previousTime)/1000.0;

                if(frameTime > 0.25){
                    frameTime = 0.25;
                }
                previousTime = currentTime;
                accumulator += frameTime;
                /*
                System.out.println("accumulator=" + accumulator);
                System.out.println("dt=" + dt);
                System.out.println("currentTime=" + currentTime);
                System.out.println("previousTime=" + previousTime);
                System.out.println("frameTime=" + frameTime);
                System.out.println("bottom---=");*/

                while(accumulator >= dt){
                    //previousState = currentState;
                    //this.gamePanel.update(dt);
                    tick();
                    accumulator -= dt;
                }

                //Interpolation
                // final double alpha = currentState * alpha +
                // previousState * (1.0 - alpha);

                //This is the draw call. Not yet using interpolation
                //this.gamePanel.postInvalidate();
            }
        }
    }
}