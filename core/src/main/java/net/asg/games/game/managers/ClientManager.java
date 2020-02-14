package net.asg.games.game.managers;

import net.asg.games.server.serialization.ServerResponse;
import net.asg.games.utils.PayloadUtil;
import net.asg.games.utils.enums.ServerRequest;

import org.apache.commons.lang.StringUtils;

public class ClientManager {

    private void handleServerResponse(ServerResponse request) throws Exception {
        String sessionId = null;
        String message = null;
        int requestSequence = -1;
        String[] payload = null;

        if(request != null){
            System.out.println("request=" + request);

            message = request.getMessage();
            sessionId = request.getSessionId();
            requestSequence = request.getRequestSequence();
            payload = request.getPayload();
            //System.out.println("2payload=" + Arrays.asList(payload));

            decodePayload(message, payload);
        }
    }

    private void decodePayload(String message, String[] payload) throws Exception {
        String[] load = null;
        if(!StringUtils.isEmpty(message)){
            System.out.println("(decodePayload)message=" + message);
            System.out.println("(decodePayload)payload=" + payload);

            ServerRequest value = ServerRequest.valueOf(message);
            switch (value) {
                case REQUEST_LOUNGE_ALL:
                    PayloadUtil.getAllLoungesRequest(payload);
                    break;
                case REQUEST_CREATE_GAME:
                    //createNewGame
                    break;
                default:
                    throw new Exception("Unknown Server Response: " + value);
            }
        }
    }
}
