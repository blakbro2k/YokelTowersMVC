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
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.service.UserInterfaceService;

@Component
public class GameManager {
    @Inject private UserInterfaceService uiService;
    @Inject private UITestController uiView;
    YokelTable table;
    Array<YokelGameBoard> boards;

    int thresh = 0;

    public GameManager(YokelTable table){
        this.table = table;
        boards = new Array<>();
        init();
    }

    private void loadGameData() {}
    public void update(){
        //System.out.println(thresh);
        thresh++;
        if(thresh > 100){
            table.stopGame();
        }
    }
    public void init(){
        for(int i = 0; i < 8; i++){
            boards.add(new YokelGameBoard(1L));
        }
    }
    public void handleMoveRight(){}
    public void handleMoveLeft(){}
    public void handleSetPiece(){}
    public String[] getBoardState(){ return null;}

    public String printTables(){
        StringBuilder sbSeats = new StringBuilder();
//String t = "";
        if(table != null){
            for(YokelGameBoard board : boards){
                sbSeats.append(board.toString());
            }
        }
        return sbSeats.toString();
        //return table.getSeat(1).toString();
    }

    public boolean startGame() {
        if(!table.isGameRunning()){
            table.startGame();
        }
        return table.isGameRunning();
    }

    public boolean isRunning() {
        return table.isGameRunning();
    }

    public int thresh() {
        return thresh;
    }

    private class GameState{

    }
}