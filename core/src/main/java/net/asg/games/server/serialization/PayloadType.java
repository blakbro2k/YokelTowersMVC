package net.asg.games.server.serialization;

import net.asg.games.utils.enums.ServerRequest;

public class PayloadType {
    private ServerRequest requestType;
    private String[] payload;

    public PayloadType(ServerRequest requestType, String[] payload){
        if(requestType == null) throw new IllegalArgumentException("Server Request message cannot be null.");
        if(payload == null) throw new IllegalArgumentException("Payload cannot be null.");
        this.requestType = requestType;
        this.payload = payload;
    }

    public ServerRequest getRequestType() {
        return requestType;
    }

    public String[] getPayload() {
        return payload;
    }
}
