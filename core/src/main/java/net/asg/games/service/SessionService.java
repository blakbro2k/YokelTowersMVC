package net.asg.games.service;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.autumn.annotation.Component;
import com.github.czyzby.autumn.annotation.Initiate;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.websocket.WebSocket;
import com.github.czyzby.websocket.WebSocketHandler;
import com.github.czyzby.websocket.WebSocketListener;
import com.github.czyzby.websocket.data.WebSocketException;
import com.github.czyzby.websocket.net.ExtendedNet;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;

import net.asg.games.game.managers.ClientManager;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.server.serialization.ClientRequest;
import net.asg.games.server.serialization.Packets;
import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.NetworkUtil;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

@Component
public class SessionService {
    private static final Logger LOGGER = LoggerService.forClass(SessionService.class);

    // @Inject-annotated fields will be automatically filled by the context initializer.

    private String message = "Connecting...";
    private ClientManager client;
    private String currentLoungeName;
    private String currentRoomName;
    private String currentSeat;
    private String userName;
    private YokelPlayer player;

    @Initiate
    public void initialize() throws WebSocketException {
        client = new ClientManager("localhost", 8000);
        //TODO: Create Unique client ID
        //TODO: Create PHPSESSION token
        //TODO: Create CSRF Token

    }

    public boolean register(YokelPlayer player) throws InterruptedException {
        this.player = player;
        return client.register(player);
    }

    public Array<YokelLounge> getAllLounges() throws InterruptedException {
        client.requestLounges();
        client.waitForRequest(30);
        return PayloadUtil.getAllLoungesRequest(client.getRequests().removeFirst());
    }
}