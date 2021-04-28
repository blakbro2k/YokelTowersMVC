package net.asg.games.server.serialization;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

import java.util.Arrays;

/** Server response packet using gdx-websocket-serialization.
 *
 * @author MJ */
public class ServerResponse implements Transferable<ServerResponse> {
    private final String message;
    private final String sessionId;
    private final int requestSequence;
    private final int serverId;
    private final String[] payload;

    public ServerResponse(final int requestSequence,
                          final String sessionId,
                          final String message,
                          final String[] payload,
                          final int serverId){
        this.requestSequence = requestSequence;
        this.sessionId = sessionId;
        this.message = message;
        this.serverId = serverId;
        this.payload = payload;
    }

    @Override
    public void serialize(final Serializer serializer) throws SerializationException {
        serializer.serializeInt(requestSequence);
        serializer.serializeString(sessionId);
        serializer.serializeString(message);
        serializer.serializeInt(serverId);
        serializer.serializeStringArray(payload);
    }

    @Override
    public ServerResponse deserialize(final Deserializer deserializer) throws SerializationException {
        return new ServerResponse(deserializer.deserializeInt(),
                deserializer.deserializeString(),
                deserializer.deserializeString(),
                deserializer.deserializeStringArray(),
                deserializer.deserializeInt());
    }

    public String getMessage() {
        return message;
    }

    public int getServerId() {
        return serverId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getRequestSequence() {
        return requestSequence;
    }

    public String[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                getRequestSequence() + ":" +
                getSessionId() + ":message:" +
                getMessage() + "]{" +
                getServerId() + "}{" +
                Arrays.toString(getPayload()) + "}";
    }
}