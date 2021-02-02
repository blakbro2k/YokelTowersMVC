package net.asg.games.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Destroy;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewController;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.kiwi.util.gdx.collection.GdxMaps;
import com.github.czyzby.websocket.data.WebSocketException;

import net.asg.games.controller.dialog.ErrorController;
import net.asg.games.game.managers.ClientManager;
import net.asg.games.game.managers.GameManager;
import net.asg.games.game.objects.PlayerKeyMap;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.Log4LibGDXLogger;
import net.asg.games.utils.Log4LibGDXLoggerService;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

/** Manages an authorized user's current session
 * Includes the client and communicates with the server
 *
 * @author Blakbro2k */
@Component
public class SessionService {
    // Getting a utility logger:
    private Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(SessionService.class);

    @Inject private InterfaceService interfaceService;

    private final String CONNECT_MSG = "Connecting...";
    private ClientManager client;
    private String currentLoungeName;
    private String currentRoomName;
    private YokelTable currentTable;
    private int currentSeat;
    private String userName;
    private YokelPlayer player;
    private ObjectMap<String, ViewController> views = GdxMaps.newObjectMap();
    private PlayerKeyMap keyMap = new PlayerKeyMap();
    private String currentErrorMessage;

    @Initiate
    public void initialize() throws WebSocketException {
        logger.enter("initialize");
        client = new ClientManager("localhost", 8000);

        //TODO: Create PHPSESSION token6
        //TODO: Create CSRF Token
        //TODO: Get host and port from configuration or preferences
        logger.exit("initialize");
    }

    @Destroy
    public void destroy() {
        logger.enter("destroy");
        closeClient();
        Disposables.disposeOf(currentTable, player);
        views.clear();
        logger.enter("destroy");
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

    private void asyncMoveRightRequest() throws InterruptedException {
        client.requestMoveRight(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    private void asyncMoveLeftRequest() throws InterruptedException {
        client.requestMoveLeft(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    private void asyncCycleDownRequest() throws InterruptedException {
        client.requestCycleDown(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    private void asyncCycleUpRequest() throws InterruptedException {
        client.requestCycleUp(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    private void asyncMoveStartDownRequest() throws InterruptedException {
        client.requestMoveStartDown(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    private void asyncMoveStopDownRequest() throws InterruptedException {
        client.requestMoveStopDown(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    private void asyncTargetAttackRequest() throws InterruptedException {
        //client.requestMoveRight(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    private void asyncRandomAttackRequest() throws InterruptedException {
        //client.requestMoveRight(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    public void asyncGameManagerFromServerRequest() throws InterruptedException {
        client.requestGameManager(currentLoungeName, currentRoomName, getCurrentTableNumber(), currentSeat);
    }

    public GameManager asyncGetGameManagerFromServerRequest() {
        return PayloadUtil.getGameManagerRequest(client.getNextRequest(ServerRequest.REQUEST_TABLE_GAME_MANAGER));
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
                if(ctrl != null && StringUtils.equalsIgnoreCase(viewId, ctrl.getViewId())){
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

    public int getCurrentTableNumber(){
        currentTable = getCurrentTable();
        if(currentTable != null){
            return currentTable.getTableNumber();
        } else {
            return -1;
        }
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
        logger.enter("showError");
        if(throwable == null) return;
        setCurrentError(throwable.getCause(), throwable.getMessage());
        interfaceService.showDialog(ErrorController.class);
        logger.exit("showError");
    }

    public void handleLocalPlayerInput(GameManager game){
        logger.enter("handleLocalPlayerInput");

        if(game == null) return;
        int currentSeat = getCurrentSeat();
        logger.debug("currentSeat={0}", currentSeat);
        game.handleMoveRight(1);

        if (Gdx.input.isKeyJustPressed(keyMap.getRightKey())) {
            game.handleMoveRight(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(keyMap.getLeftKey())) {
            game.handleMoveLeft(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(keyMap.getCycleDownKey())) {
            game.handleCycleDown(currentSeat);
        }
        if (Gdx.input.isKeyPressed(keyMap.getDownKey())) {
            game.handleStartMoveDown(currentSeat);
        }
        if (!Gdx.input.isKeyPressed(keyMap.getDownKey())) {
            game.handleStopMoveDown(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(keyMap.getRandomAttackKey())) {
            game.handleRandomAttack(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            game.testMedusa(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            game.testMidas(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            game.showGameBoard(currentSeat);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            game.testGameBoard(currentSeat);
        }
        logger.exit("handleLocalPlayerInput");
    }

    public void handlePlayerInputToServer() throws InterruptedException {
        logger.debug("Enter handlePlayerInput()");

        int currentSeat = getCurrentSeat();
        logger.debug("currentSeat={0}", currentSeat);

        if (Gdx.input.isKeyJustPressed(keyMap.getRightKey())) {
            asyncMoveRightRequest();
        }
        if (Gdx.input.isKeyJustPressed(keyMap.getLeftKey())) {
            asyncMoveLeftRequest();
        }
        if (Gdx.input.isKeyJustPressed(keyMap.getCycleDownKey())) {
            asyncCycleDownRequest();
        }
        if (Gdx.input.isKeyPressed(keyMap.getDownKey())) {
            asyncMoveStartDownRequest();
        }
        if (!Gdx.input.isKeyPressed(keyMap.getDownKey())) {
            asyncMoveStopDownRequest();
        }
        if (Gdx.input.isKeyJustPressed(keyMap.getRandomAttackKey())) {
            asyncRandomAttackRequest();
        }
        logger.debug("Exit handlePlayerInput()");
    }
}