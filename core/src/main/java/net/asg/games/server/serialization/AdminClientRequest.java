package net.asg.games.server.serialization;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;
import com.github.czyzby.websocket.serialization.impl.Size;

import java.util.Arrays;

public class AdminClientRequest implements Transferable {
    private final String message;
    private final String sessionId;
    private final int requestSequence;
    private final String[] payload;

    public AdminClientRequest(final int requestSequence, final String sessionId, final String message, String[] payload) {
        this.requestSequence = requestSequence;
        this.sessionId = sessionId;
        this.message = message;
        this.payload = payload;
    }

    @Override
    public void serialize(Serializer serializer) throws SerializationException {
        serializer.serializeInt(requestSequence); // Assuming String is no longer than Short#MAX_VALUE.
        serializer.serializeString(sessionId, Size.SHORT); // Assuming String is no longer than Short#MAX_VALUE.
        serializer.serializeString(message, Size.SHORT); // Assuming String is no longer than Short#MAX_VALUE.
        serializer.serializeStringArray(payload);
    }

    @Override
    public Transferable deserialize(Deserializer deserializer) throws SerializationException {
        return new ClientRequest(deserializer.deserializeInt(),
                deserializer.deserializeString(Size.SHORT),
                deserializer.deserializeString(Size.SHORT),
                deserializer.deserializeStringArray());
    }

    public String getMessage() {
        return message;
    }

    public int getRequestSequence() {
        return requestSequence;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String[] getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" +
                getRequestSequence() + ":" +
                getSessionId() + ":message:" +
                getMessage() + "]" +
                Arrays.toString(getPayload()) + "}";
    }
}
