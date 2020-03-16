package net.asg.games.service;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewController;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.websocket.data.WebSocketException;

import net.asg.games.game.managers.ClientManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

@Component
public class SessionService {
    private static final Logger LOGGER = LoggerService.forClass(SessionService.class);
    @Inject private InterfaceService interfaceService;

    private String message = "Connecting...";
    private ClientManager client;
    private String currentLoungeName;
    private String currentRoomName;
    private String currentSeat;
    private String userName;
    private YokelPlayer player;
    private ObjectMap<String, ViewController> views = GdxMaps.newObjectMap();
    private String currentErrorMessage;

    @Initiate
    public void initialize() throws WebSocketException {
        client = new ClientManager("localhost", 8000);
        //TODO: Create Unique client ID
        //TODO: Create PHPSESSION token6
        //TODO: Create CSRF Token
    }

    @Destroy
    public void destroy() {
        closeClient();
        views.clear();
    }

    public void closeClient() {
        client.dispose();
    }

    public boolean connectToServer() throws InterruptedException {
        return client.connectToServer();
    }

    public void registerPlayer() throws InterruptedException {
        if(player == null) throw new InterruptedException("No Authorized player in current session!");
        client.requestPlayerRegister(getCurrentPlayer());
    }

    public Array<YokelLounge> getAllLounges() throws InterruptedException {
        client.requestLounges();
        client.waitForOneRequest();
        return PayloadUtil.getAllLoungesRequest(client.getNextRequest().getPayload());
    }

    public Array<YokelPlayer> getAllPlayers() throws InterruptedException {
        client.requestPlayers();
        client.waitForOneRequest();
        return PayloadUtil.getAllRegisteredPlayersRequest(client.getNextRequest().getPayload());
    }

    public void asyncPlayerAllRequest() throws InterruptedException {
        client.requestPlayers();
    }

    public Array<YokelPlayer> asyncGetPlayerAllRequest(){
        return PayloadUtil.getAllRegisteredPlayersRequest(client.getNextRequest(ServerRequest.REQUEST_ALL_REGISTERED_PLAYERS));
    }

    public Array<String> toPlayerNames(Array<YokelPlayer> players) {
        Array<String> playerNames = GdxArrays.newArray();
        if(players != null){
            for(YokelPlayer player : players){
                if(player != null){
                    playerNames.add(player.getName());
                }
            }
        }
        return playerNames;
    }

    public ViewController getView(String viewId){
        if(views.containsKey(viewId)){
            return views.get(viewId);
        } else {
            for(ViewController ctrl : interfaceService.getControllers()){
                if(StringUtils.equalsIgnoreCase(viewId, ctrl.getViewId())){
                    views.put(viewId, ctrl);
                    return ctrl;
                }
            }
        }
        return interfaceService.getCurrentController();
    }

    public void setCurrentUserName(String userName){
        this.userName = userName;
    }

    public String getCurrentUserName(){
        return userName;
    }

    public void setCurrentSeat(String currentSeat){
        this.currentSeat = currentSeat;
    }

    public String getCurrentSeat(){
        return currentSeat;
    }

    public void setCurrentRoomName(String currentRoomName){
        this.currentRoomName = currentRoomName;
    }

    public String getCurrentRoomName(){
        return currentRoomName;
    }

    public void setCurrentLoungeName(String currentLoungeName){
        this.currentLoungeName = currentLoungeName;
    }

    public String getCurrentLoungeName(){
        return currentLoungeName;
    }

    public void setCurrentError(Throwable cause, String message) {
        currentErrorMessage = message;
    }

    public String getCurrentError() {
        return currentErrorMessage;
    }

    public void setCurrentPlayer(YokelPlayer yokelPlayer) {
        this.player = yokelPlayer;
    }

    public YokelPlayer getCurrentPlayer() {
        return player;
    }
}