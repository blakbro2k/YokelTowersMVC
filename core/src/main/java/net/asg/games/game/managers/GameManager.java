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
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.service.UserInterfaceService;

@Component
public class GameManager {
    @Inject private UserInterfaceService uiService;
    @Inject private UITestController uiView;
    YokelTable table;
    int thresh = 0;

    public GameManager(YokelTable table){this.table = table;}

    private void loadGameData() {}
    public void update(double delta){
        System.out.println(thresh);
        thresh++;
        if(thresh > 100){
            table.stopGame();
        }
    }
    public void handleMoveRight(){}
    public void handleMoveLeft(){}
    public void handleSetPiece(){}
    public String[] getBoardState(){ return null;}

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