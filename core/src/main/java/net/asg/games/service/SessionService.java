package net.asg.games.service;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
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

import net.asg.games.controller.dialog.ErrorController;
import net.asg.games.game.managers.ClientManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

/** Manages an authorized user's current session
 * Includes the client and communicates with the server
 *
 * @author Blakbro2k */
@Component
public class SessionService {
    private static final Logger LOGGER = LoggerService.forClass(SessionService.class);
    @Inject private InterfaceService interfaceService;

    private String message = "Connecting...";
    private ClientManager client;
    private String currentLoungeName;
    private String currentRoomName;
    private YokelTable currentTable;
    private int currentSeat;
    private String userName;
    private YokelPlayer player;
    private ObjectMap<String, ViewController> views = GdxMaps.newObjectMap();
    private String currentErrorMessage;

    @Initiate
    public void initialize() throws WebSocketException {
        client = new ClientManager("localhost", 8000);
        //TODO: Create PHPSESSION token6
        //TODO: Create CSRF Token
        //TODO: Get host and port from configuration or preferences
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

    public void requestTableSitRequest(int tableNumber, int seatNumber) throws InterruptedException {
        client.requestTableSit(player, currentLoungeName, currentRoomName, tableNumber, seatNumber);
        client.waitForOneRequest();
    }

    public void asyncPlayerAllRequest() throws InterruptedException {
        client.requestPlayers();
    }

    public Array<YokelPlayer> asyncGetPlayerAllRequest(){
        return PayloadUtil.getAllRegisteredPlayersRequest(client.getNextRequest(ServerRequest.REQUEST_ALL_REGISTERED_PLAYERS));
    }

    public void asyncTableAllRequest() throws InterruptedException {
        client.requestTables(currentLoungeName, currentRoomName);
    }

    public void asyncCreateGameRequest(YokelTable.ACCESS_TYPE accessType, boolean isRated) throws InterruptedException {
        client.requestCreateGame(currentLoungeName, currentRoomName, accessType, isRated);
    }

    public void asyncTableSitRequest(int tableNumber, int seatNumber) throws InterruptedException {
        client.requestTableSit(player, currentLoungeName, currentRoomName, tableNumber, seatNumber);
    }

    public void asyncTableStandRequest(int tableNumber, int seatNumber) throws InterruptedException {
        client.requestTableStand(currentLoungeName, currentRoomName, tableNumber, seatNumber);
    }

    public Array<YokelTable> asyncGetTableAllRequest(){
        //TODO: Save tables states
        return PayloadUtil.getAllTablesRequest(client.getNextRequest(ServerRequest.REQUEST_TABLE_INFO));
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
            //If view does not exist, return current view
            return interfaceService.getCurrentController();
        }
    }

    public void setCurrentUserName(String userName){
        this.userName = userName;
    }

    public String getCurrentUserName(){
        return userName;
    }

    public void setCurrentSeat(int currentSeat){
        this.currentSeat = currentSeat;
    }

    public int getCurrentSeat(){
        return currentSeat;
    }

    public void setCurrentTable(YokelTable currentTable){
        this.currentTable = currentTable;
    }

    public YokelTable getCurrentTable(){
        return currentTable;
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

    public void showError(Throwable throwable) {
        if(throwable == null) return;
        setCurrentError(throwable.getCause(), throwable.getMessage());
        interfaceService.showDialog(ErrorController.class);
    }
}