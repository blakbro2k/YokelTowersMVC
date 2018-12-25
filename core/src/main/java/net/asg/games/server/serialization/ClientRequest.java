package net.asg.games.server.serialization;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;
import com.github.czyzby.websocket.serialization.impl.Size;

/** Client message packet using gdx-websocket-serialization.
 *
 * @author MJ */
public class ClientRequest implements Transferable<ClientRequest> {
    private final String message;
    private final String sessionId;
    private final int requestSequence;

    public ClientRequest(final int requestSequence, final String sessionId, final String message) {
        this.requestSequence = requestSequence;
        this.sessionId = sessionId;
        this.message = message;
    }

    @Override
    public void serialize(final Serializer serializer) throws SerializationException {
        serializer.serializeInt(requestSequence); // Assuming String is no longer than Short#MAX_VALUE.
        serializer.serializeString(sessionId, Size.SHORT); // Assuming String is no longer than Short#MAX_VALUE.
        serializer.serializeString(message, Size.SHORT); // Assuming String is no longer than Short#MAX_VALUE.
    }

    @Override
    public ClientRequest deserialize(final Deserializer deserializer) throws SerializationException {
        return new ClientRequest(deserializer.deserializeInt(),
                deserializer.deserializeString(Size.SHORT),
                deserializer.deserializeString(Size.SHORT));
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + getRequestSequence() + ":" + getSessionId() + ":message:" + getMessage() + "]";
    }
}
