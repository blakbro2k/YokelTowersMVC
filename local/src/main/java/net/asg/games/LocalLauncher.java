package net.asg.games;

import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.provider.actors.GameBoard;

import org.pmw.tinylog.Logger;

/** Launches the server application. */
public class LocalLauncher {
    public static void main(final String... args) throws Exception {
        try{
            YokelGameBoard board = new YokelGameBoard();
            board.placeBlockAt(new YokelPiece(12), 2, 12);
            System.out.println(board);
        } catch (Exception e) {
            Logger.error(e,"Failed to launch server: ");
            throw new Exception("Failed to launch server: ", e);
        }
    }
}