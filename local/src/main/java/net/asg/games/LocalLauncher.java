package net.asg.games;

import net.asg.games.game.objects.YokelGameBoard;

import org.pmw.tinylog.Logger;

/** Launches the server application. */
public class LocalLauncher {
    public static void main(final String... args) throws Exception {
        try{
            YokelGameBoard board = new YokelGameBoard(1L);
            //System.out.println("cells do be dropped: " + board.getCellsToBeDropped());
            board.addNextPiece();
           // 165
            //System.out.println(board);
            //System.out.println("cells do be dropped: " + board.getCellsToBeDropped());
            int index = 0;
            while(!board.hasPlayerDied()){
                board.update(1);
                //System.out.println(("is Dead?=" + ));
                System.out.println(board);
                index++;
            }

        } catch (Exception e) {
            Logger.error(e,"Failed to launch server: ");
            throw new Exception("Failed to launch server: ", e);
        }
    }
}