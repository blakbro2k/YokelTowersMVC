package net.asg.games.game.objects;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelSeat extends AbstractYokelObject {
    private int seatNumber;
    private YokelPlayer seatedPlayer;

    //Empty Constructor required for Json.Serializable
    public YokelSeat(){}

    public YokelSeat(int seatNumber){
        if(seatNumber < 0 || seatNumber > 7) throw new IllegalArgumentException("Seat number must be between 0 - 7.");
        this.seatNumber = seatNumber;
    }

    public boolean sitDown(YokelPlayer player){
        if(!isOccupied()){
            seatedPlayer = player;
            return true;
        }
        return false;
    }

    public YokelPlayer standUp(){
        YokelPlayer var = seatedPlayer;
        seatedPlayer = null;
        return var;
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
        if(isOccupied()){
            seatedPlayer.dispose();
            standUp();
        }
    }
}