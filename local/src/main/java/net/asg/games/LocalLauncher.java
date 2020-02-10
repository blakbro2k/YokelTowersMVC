package net.asg.games;

import com.badlogic.gdx.math.MathUtils;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.provider.actors.GameBoard;

import org.pmw.tinylog.Logger;

/** Launches the server application. */
public class LocalLauncher {
    public static void main(final String... args) throws Exception {
        try{
            YokelGameBoard board = new YokelGameBoard(2L);
            //System.out.println("cells do be dropped: " + board.getCellsToBeDropped());
            board.getNextPiece();
           // 165
            //System.out.println(board);
            //System.out.println("cells do be dropped: " + board.getCellsToBeDropped());
            boolean isRunning = true;
            while(isRunning){
                board.update(1);
                System.out.println(("is Dead?=" + board.hasPlayerDied()));
                System.out.println(board);

                if(MathUtils.random(10) == 3)  board.attemptMovePieceRight();
                if(MathUtils.random(10) == 1)  board.attemptMovePieceLeft();

                if(board.hasPlayerDied()){
                    isRunning = false;
                }
            }
        } catch (Exception e) {
            Logger.error(e,"Local Launcher Failed: ");
            throw new Exception("Local Launcher Failed: ", e);
        }
    }
}