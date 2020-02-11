package net.asg.games;

import com.badlogic.gdx.math.MathUtils;

import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelPiece;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.provider.actors.GameBoard;

import org.pmw.tinylog.Logger;

import java.util.Scanner;

/** Launches the server application. */
public class LocalLauncher {
    private final static float MS_PER_UPDATE = 1f;
    public static void main(final String... args) throws Exception {
        // Input from player

        try (Scanner scanner = new Scanner(System.in)) {
            YokelPlayer player1 = new YokelPlayer("blakbro2k");
            YokelPlayer player2 = new YokelPlayer("lholtham");
            String roomName = "Eiffel Tower";

            YokelTable table = new YokelTable(1);

            table.getSeat(0).sitDown(player1);
            table.getSeat(2).sitDown(player2);

            GameManager gm = new GameManager(table);
            gm.startGame();

            double ns = 1000000000.0 / 60.0;
            double delta = 0;

            double previous = getCurrentTime();

            int hardOut = 0;
            boolean isRunning = gm.isRunning();
            while (isRunning) {
                System.out.println("hard Out=" + hardOut);

                double current = getCurrentTime();
                double elapsed = current - previous;
                previous = current;
                delta = elapsed;
                System.out.println("elapsed=" + elapsed);

                //processInput(scanner.nextLine().charAt(0));

                while (delta >= MS_PER_UPDATE) {
                    //update();
                    //System.out.println("--- delta=" + delta);

                    //gm.update(lag);
                    //System.out.println("--- lag=" + delta);
                    delta--;
                }

                if(++hardOut > 4){
                    isRunning = false;
                }
            }
        } catch (Exception e) {
            Logger.error(e, "Local Launcher Failed: ");
            throw new Exception("Local Launcher Failed: ", e);
        }
    }

    private static long getCurrentTime(){
        return System.nanoTime();
    }

    private static void processInput(char input){
        System.out.println("input= " + input);
    }
}
/*
    double ns = 1000000000.0 / 60.0;
    double delta = 0;

    long lastTime = System.nanoTime();
    long timer = System.currentTimeMillis();

    while (running) {
        long now = System.nanoTime();
        delta += (now - lastTime) / ns;
        lastTime = now;

        while (delta >= 1) {
            tick();
            delta--;
        }
    }

    double previous = getCurrentTime();
double lag = 0.0;
while (true)
{
  double current = getCurrentTime();
  double elapsed = current - previous;
  previous = current;
  lag += elapsed;

  processInput();

  while (lag >= MS_PER_UPDATE)
  {
    update();
    lag -= MS_PER_UPDATE;
  }

  render();
}
 */