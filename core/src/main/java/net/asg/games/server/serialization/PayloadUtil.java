package net.asg.games.server.serialization;

import net.asg.games.game.objects.YokelPlayer;

public class PayloadUtil {
    private static final String[] EMPTY_ARRAY = {""};
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
}