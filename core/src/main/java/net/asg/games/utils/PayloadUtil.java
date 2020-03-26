package net.asg.games.utils;

import com.badlogic.gdx.utils.Array;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelTable;

import org.pmw.tinylog.Logger;

public class PayloadUtil {
    private static final String[] EMPTY_ARRAY = {""};

    private static boolean validatedInputs(Object... objects){
        boolean isValid = objects != null;

        if(isValid){
            for(Object object : objects){
                if(object == null){
                    isValid = false;
                    break;
                }
            }
        }
        return isValid;
    }

    //Create Payload
    public static String[] createPlayerRegisterRequest(String clientId, YokelPlayer player){
        if(validatedInputs(player)){
            return new String[]{clientId, player.toString()};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createPlayerDisconnectRequest(String clientId){
        if(validatedInputs(clientId)){
            return new String[]{clientId, ""};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createJoinLeaveRoomRequest(YokelPlayer player, String loungeName, String roomName){
        if(validatedInputs(player, loungeName, roomName)){
            return new String[]{player.getId(), loungeName, roomName};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createNewGameRequest(String loungeName, String roomName, YokelTable.ACCESS_TYPE type, boolean isRated){
        if(validatedInputs(loungeName, roomName)){
            return new String[]{loungeName, roomName, Util.otos(type), Util.otos(isRated)};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createTableJoinRequest(YokelPlayer player, String loungeName, String roomName, int tableNumber, int seatNumber){
        if(validatedInputs(player, loungeName, roomName)){
            return new String[]{player.getId(), loungeName, roomName, Util.otos(tableNumber), Util.otos(seatNumber)};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createGameStartRequest(String loungeName, String roomName, int tableNumber){
        if(validatedInputs(loungeName, roomName)){
            return new String[]{loungeName, roomName, Util.otos(tableNumber)};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createTableStandRequest(String loungeName, String roomName, int tableNumber, int seatNumber){
        if(validatedInputs(loungeName, roomName)){
            return new String[]{loungeName, roomName, Util.otos(tableNumber), Util.otos(seatNumber)};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createTableSitRequest(YokelPlayer player, String loungeName, String roomName, int tableNumber, int seatNumber){
        if(validatedInputs(loungeName, roomName)){
            return new String[]{player.getId(), loungeName, roomName, Util.otos(tableNumber), Util.otos(seatNumber)};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createTableInfoRequest(String loungeName, String roomName){
        if(validatedInputs(loungeName, roomName)){
            return new String[]{loungeName, roomName};
        }
        return EMPTY_ARRAY;
    }

    //From payload
    public static YokelPlayer getRegisterPlayerFromPayload(String[] clientPayload){
        if(Util.isValidPayload(clientPayload, 2)){
            return Util.getObjectFromJsonString(YokelPlayer.class, clientPayload[1]);
        }
        return null;
    }

    public static String getClientIDFromPayload(String[] clientPayload){
        if(Util.isValidPayload(clientPayload, 2)){
            return Util.otos(clientPayload[0]);
        }
        return null;
    }

    public static Array<YokelLounge> getAllLoungesRequest(String[] clientPayload) {
        Logger.trace("Enter getLoungesRequest()");

        Array<YokelLounge> ret = new Array<>();
        if(validatedInputs(clientPayload)){
            for(String payload : clientPayload){
                ret.add(Util.getObjectFromJsonString(YokelLounge.class, payload));
            }
        }
        Logger.trace("Exit getLoungesRequest()");
        return ret;
    }

    public static Array<YokelPlayer> getAllRegisteredPlayersRequest(String[] clientPayload) {
        Logger.trace("Enter getAllRegisteredPlayersRequest()");

        Array<YokelPlayer> ret = new Array<>();
        if(validatedInputs(clientPayload)){
            for(String payload : clientPayload){
                ret.add(Util.getObjectFromJsonString(YokelPlayer.class, payload));
            }
        }
        Logger.trace("Exit getAllRegisteredPlayersRequest()");
        return ret;
    }

    public static Array<YokelTable> getAllTablesRequest(String[] clientPayload) {
        Logger.trace("Enter getAllTablesRequest()");

        Array<YokelTable> ret = new Array<>();
        if(validatedInputs(clientPayload)){
            for(String payload : clientPayload){
                ret.add(Util.getObjectFromJsonString(YokelTable.class, payload));
            }
        }
        Logger.trace("Exit getAllTablesRequest()");
        return ret;
    }
}