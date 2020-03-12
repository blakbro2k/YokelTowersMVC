package net.asg.games.utils.enums;

public enum ServerRequest {
    REQUEST_ALL_DEBUG_PLAYERS(0),
    REQUEST_ALL_REGISTERED_PLAYERS(1),
    REQUEST_LOGOFF(2),
    REQUEST_PLAYER_REGISTER(3),
    REQUEST_CREATE_GAME(4),
    REQUEST_PLAY_GAME(5),
    REQUEST_TABLE_STAND(6),
    REQUEST_TABLE_JOIN(7),
    REQUEST_TABLE_SIT(8),
    REQUEST_ROOM(9),
    REQUEST_ROOM_JOIN(10),
    REQUEST_ROOM_LEAVE(11),
    REQUEST_LOUNGE(12),
    REQUEST_TABLE_INFO(13),
    REQUEST_LOUNGE_ALL(14),
    REQUEST_CLIENT_ID(15),
    REQUEST_CLIENT_DISCONNECT(16);

    private int requestYpe;

    ServerRequest(int type){
        this.requestYpe = type;
    }

    public int getValue() {
        return requestYpe;
    }
}