package net.asg.games.game.objects;

import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelSeat {
    private int seatNumber;
    private YokelPlayer seatedPlayer;

    //Empty Contructor required for Json.Serializable
    public YokelSeat(){}

    public YokelSeat(int seatNumber){
        if(seatNumber < 1 || seatNumber > 8) throw new IllegalArgumentException("Seat number must be between 1 - 8.");
        this.seatNumber = seatNumber;
    }

    public void sitDown(YokelPlayer player){
        if(!isOccupied()){
            seatedPlayer = player;
        }
    }

    public void standUp(){
        seatedPlayer = null;
    }

    public boolean isOccupied(){
        return seatedPlayer != null;
    }

    public YokelPlayer getSeatedPlayer(){
        return seatedPlayer;
    }

    public int getSeatNumber(){
        return seatNumber;
    }

    @Override
    public String toString(){
        return Util.convertJsonString(Util.getJsonString(this));
    }
}
