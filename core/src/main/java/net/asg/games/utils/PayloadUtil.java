package net.asg.games.utils;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.Util;

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
    public static String[] createPlayerRegisterRequest(YokelPlayer player){
        if(validatedInputs(player)){
            return new String[]{player.toString()};
        }
        return EMPTY_ARRAY;
    }

    public static String[] createJoinRoomRequest(YokelPlayer player, String loungeName, String roomName){
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

    public static String[] createTableInfoRequest(String loungeName, String roomName){
        if(validatedInputs(loungeName, roomName)){
            return new String[]{loungeName, roomName};
        }
        return EMPTY_ARRAY;
    }

    //From payload

    //0 = Player JSON
    public static YokelPlayer getRegisterPlayerFromPayload(String[] clientPayload){
        if(Util.isValidPayload(clientPayload, 1)){
            return Util.getObjectFromJsonString(YokelPlayer.class, clientPayload[0]);
        }
        return null;
    }

    //0 = GameLounge Name
    public static String[] getAllLoungesRequest(String[] clientPayload) {
        Logger.trace("Enter getLoungesRequest()");
        String[] ret = new String[1];
        if(Util.isValidPayload(clientPayload, 1)){
            String loungeName = clientPayload[0];
            //YokelLounge lounge = getLounge(loungeName);
            //ret[0] = lounge == null ? null : lounge.toString();
        }
        Logger.trace("Exit getLoungesRequest()");
        return ret;
    }
}