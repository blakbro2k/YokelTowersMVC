package net.asg.games.server;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelSeat {
    private int seatNumber;
    private Player seatedPlayer;

    public YokelSeat(int serverId, int roomId, int seatNumber){
        this.seatNumber = seatNumber;
    }

    public boolean sitDown(Player player){
        if(!isOccupied()){
            seatedPlayer = player;
            return true;
        }
        return false;
    }

    public void standUp(){
        seatedPlayer = null;
    }

    public boolean isOccupied(){
        return seatedPlayer != null;
    }

    public Player getSeatedPlayer(){
        return seatedPlayer;
    }

    public int getSeatNumber(){
        return seatNumber;
    }

    @Override
    public String toString(){
        return "[" + seatNumber + ":" + getSeatedPlayer() + "]";
    }
}
