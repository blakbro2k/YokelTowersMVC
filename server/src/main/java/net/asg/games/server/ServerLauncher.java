package net.asg.games.server;

import net.asg.games.game.ServerManager;

import org.pmw.tinylog.Logger;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

/** Launches the server application. */
public class ServerLauncher {
    private final static String SERVER_BUILD = "0.0.1";

    private ServerManager serverDaemon;
    private final Vertx vertx = Vertx.vertx();

    private ServerLauncher() {}

    public static void main(final String... args) throws Exception {
        try{
            new ServerLauncher().launch(args);
        } catch (Exception e) {
            Logger.error(e,"Error in main: ");
            throw new Exception("Error in main: ", e);
        }
    }

    private void launch(String... args) throws Exception {
        try {
            Logger.trace("Enter launch()");
            Logger.info("Launching YokelTowers-server build: {}", SERVER_BUILD);
            initialize(args);
            initializeNetwork();
            //serverDaemon.run();
            Logger.trace("Exit launch()");
        } catch (Exception e) {
            Logger.error(e,"Error Launching Server: ");
            serverDaemon.shutDownServer(1);
            throw new Exception("Error Launching Server: ", e);
        }
    }

    private void initializeNetwork()throws Exception{
        try{
            Logger.trace("Enter initializeNetwork()");
            Logger.info("Initializing Network Listener.");
            if(serverDaemon == null) throw new Exception("Server daemon was not started!");

            final HttpServer server = vertx.createHttpServer();
            server.websocketHandler(webSocket -> {

                // Printing received packets to console, sending response:
                webSocket.frameHandler(frame -> {
                    try {
                        serverDaemon.handleFrame(webSocket, frame);
                    } catch (Exception e) {
                        Logger.error(e, "There was an error handling client request");
                        Logger.error(e);
                    }
                });
            }).listen(serverDaemon.getPort());
            Logger.trace("Exit initializeNetwork()");
        } catch(Exception e) {
            Logger.error(e,"Error Setting up Network Listener: ");
            throw new Exception("Error Setting up Network Listener: ", e);
        }
    }

    private void initialize(String... args) throws Exception{
        try {
            Logger.trace("Enter initialize()");
            Logger.info("Initializing server arguments: ");
            serverDaemon = new ServerManager(args);
            Logger.trace("Exit initialize()");
        } catch (Exception e) {
            Logger.error(e,"Error during initialization: ");
            throw new Exception("Error during initialization: ", e);
        }
    }
}