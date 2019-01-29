package net.asg.games.game.objects;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelSeat extends YokelObject {
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
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!this.getClass().equals(o.getClass())) return false;
        YokelSeat seat = getSeat(o);
        return seatNumber == seat.getSeatNumber() && isSeatedSame(seat.getSeatedPlayer());
    }

    private boolean isSeatedSame(YokelPlayer player){
        if(player == null) return seatedPlayer == null;
        return player.equals(seatedPlayer);
    }

    private YokelSeat getSeat(Object o){
        return (YokelSeat) o;
    }

    @Override
    public void dispose() {
        seatedPlayer.dispose();
    }
}
