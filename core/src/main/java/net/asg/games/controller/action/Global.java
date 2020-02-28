package net.asg.games.controller.action;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.stereotype.ViewActionContainer;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.action.ActionContainer;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.controller.UITestController;
import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GamePiece;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.service.UserInterfaceService;
import net.asg.games.utils.PostLoader;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Since this class implements ActionContainer and is annotated with ViewActionContainer, its methods will be reflected
 * and available in all LML templates. Note that this class is a component like any other, so it can inject any fields,
 * use Initiate-annotated methods, etc.
 */
@ViewActionContainer("global")
public class Global implements ActionContainer {
    @Inject private UITestController ui;
    @Inject private UserInterfaceService uiService;

    private OrderedMap<String, YokelPlayer> players = new OrderedMap<>();
    private OrderedMap<String, YokelLounge> lounges = new OrderedMap<>();

    /**
     * This is a mock-up method that does nothing. It will be available in LML templates through "close" (annotation
     * argument) and "noOp" (method name) IDs.
     */
    @LmlAction("close")
    public void noOp() {
    }

    @LmlAction("isUITest")
    public boolean isUITest(){
        System.out.println("isUITestResult  called.");
        return PostLoader.UI_TEST.equalsIgnoreCase(PostLoader.getInstance().getPreLoader());
    }

    @LmlAction("isDebug")
    public boolean isDebug(){
        System.out.println("isDebugResult  called.");
        return PostLoader.DEBUG.equalsIgnoreCase(PostLoader.getInstance().getPreLoader());
    }

    @LmlAction("toggleGameStart")
    public void toggleGameStart(Actor actor) {
        if(!uiService.gameClock.isRunning()){
            uiService.gameClock.start();
        } else {
            uiService.gameClock.stop();
        }
    }

    @LmlAction("setGamePiece")
    private void setGamePiece(){
        GamePiece gp = new GamePiece(uiService.getSkin(),null,null,null);
        gp.setData(new String[]{"12","16","3"});
        //System.out.println("lksjfd\n" + gp);
        uiService.area1.setGamePiece(gp);
    }

    @LmlAction("getTestBoard")
    private YokelGameBoard getTestBoard(){

        YokelGameBoard board = new YokelGameBoard(1L);

        board.setCell(0,0, getRandomBlockId());
        board.setCell(0,1, getRandomBlockId());
        board.setCell(0,2, getRandomBlockId());
        board.setCell(0,3, getRandomBlockId());
        board.setCell(0,4, getRandomBlockId());
        board.setCell(0,5, getRandomBlockId());

        board.setCell(1,0, getRandomBlockId());
        board.setCell(1,1, getRandomBlockId());
        board.setCell(1,2, getRandomBlockId());
        board.setCell(1,3, getRandomBlockId());
        board.setCell(1,4, getRandomBlockId());
        board.setCell(1,5, getRandomBlockId());

        board.setCell(2,0, getRandomBlockId());
        board.setCell(2,1, getRandomBlockId());
        board.setCell(2,2, getRandomBlockId());
        board.setCell(2,3, getRandomBlockId());
        board.setCell(2,4, getRandomBlockId());
        board.setCell(2,5, getRandomBlockId());
        return board;
    }

    private int getRandomBlockId(){
        return MathUtils.random(YokelBlock.EX_BLOCK);
    }

    @LmlAction("getTimerSeconds")
    public int getTimerSeconds() {
        return uiService.gameClock.isRunning() ? uiService.gameClock.getElapsedSeconds() : 0;
    }
}
