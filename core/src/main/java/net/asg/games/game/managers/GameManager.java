package net.asg.games.game.managers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;

import net.asg.games.controller.UITestController;
import net.asg.games.game.objects.YokelBoardPair;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.service.UserInterfaceService;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameManager {
    @Inject private UserInterfaceService uiService;
    @Inject private UITestController uiView;
    YokelTable table;
    Array<YokelGameBoard> gameBoards;
    int thresh = 0;
    private boolean isGameRunning;

    public GameManager(YokelTable table){
        this.table = table;
        gameBoards = new Array<>();
        init();
    }

    private void loadGameData() {}
    public void update(){
        //System.out.println(thresh);
        for(int i = 0; i < 8; i++){
            YokelSeat seat = table.getSeat(i);
            YokelGameBoard board = gameBoards.get(i);
            if(isOccupied(seat) && !isPlayerDead(board)){
                board.update(1);
            }
        }

        thresh++;
        if(thresh > 100){
            stopGame();
        }
    }

    private boolean isOccupied(YokelSeat seat){
        if(seat != null){
            return seat.isOccupied();
        }
        return false;
    }

    private boolean isPlayerDead(YokelGameBoard board){
        if(board != null){
            return board.hasPlayerDied();
        }
        return true;
    }

    public void init(){
        long seed = getSeed();
        isGameRunning = false;
        for(int i = 0; i < 8; i++){
            gameBoards.add(new YokelGameBoard(seed));
        }
    }

    private long getSeed() {
        return 1L;
    }

    public void handleMoveRight(){}
    public void handleMoveLeft(){}
    public void handleSetPiece(){}
    public String[] getBoardState(){ return null;}

    public String printTables(){
        StringBuilder sbSeats = new StringBuilder();
        //String t = "";
        if(table != null){
            for(YokelGameBoard board : gameBoards){
                sbSeats.append(board.toString());
            }
        }
        return sbSeats.toString();
        //return table.getSeat(1).toString();.
    }

    public boolean startGame() {
        if(!isGameRunning){
            table.isTableStartReady();
        }
        return isGameRunning = true;
    }

    public boolean stopGame(){
        return isGameRunning = false;
    }

    public boolean isRunning() {
        return isGameRunning;
    }

    public int thresh() {
        return thresh;
    }

    private class GameState{

    }
}