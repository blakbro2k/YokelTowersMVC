package net.asg.games.server;

import com.badlogic.gdx.utils.Queue;

import net.asg.games.utils.Util;

public class YokelTable {
    public enum ACCESS_TYPE {PRIVATE, PUBLIC, PROTECTED}

    private final int MAX_SEATS = 8;
    private String tableId;

    private Queue<YokelSeat> seats;
    private ACCESS_TYPE accessType;
    private boolean isStarted;

    public YokelTable(){
        initialize();
    }

    private void initialize(){
        tableId = Util.IDGenerator.getID();
        seats = new Queue<YokelSeat>();
        setAccessType(ACCESS_TYPE.PUBLIC);
        setUpSeats();
        retartTable();
    }

    public String getTableId() {
        return tableId;
    }

    private void retartTable(){
        isStarted = false;
    }

    public void setAccessType(ACCESS_TYPE accessType){
        this.accessType = accessType;
    }

    public ACCESS_TYPE getAccessType(){
        return accessType;
    }

    public void startGame(){
        if(isTableStartReady()) {
            isStarted = true;
        }
    }

    public void stopGame(){
        isStarted = false;
    }

    public boolean isGameRunning(){
        return isStarted;
    }

    public boolean isGroupReady(int g){
        if(g < 0 || g > 3){
            return false;
        }
        return seats.get(g * 2).isOccupied() || seats.get((g * 2) + 1).isOccupied();
    }

    public boolean isTableStartReady(){
        if(isGroupReady(0)){
            return isGroupReady(1) || isGroupReady(2) || isGroupReady(3);
        }
        if(isGroupReady(1)){
            return isGroupReady(0) || isGroupReady(2) || isGroupReady(3);
        }
        if(isGroupReady(2)){
            return isGroupReady(0) || isGroupReady(1) || isGroupReady(3);
        }
        if(isGroupReady(3)){
            return isGroupReady(0) || isGroupReady(1) || isGroupReady(2);
        }
        return false;
    }

    public void setUpSeats(){
        for(int i = 0; i < MAX_SEATS; i++){
            seats.addLast(new YokelSeat(1, 1, i));
        }
    }

    public Queue<YokelSeat> getSeats(){
        return seats;
    }

    public YokelSeat getSeat(int seatNum){
        return seats.get(seatNum);
    }

    @Override
    public String toString(){
        StringBuilder top = new StringBuilder("+");
        StringBuilder middle1 = new StringBuilder("|");
        StringBuilder middle2 = new StringBuilder("|");
        StringBuilder bottom = new StringBuilder("+");

        YokelSeat seat1;
        YokelSeat seat2;
        for(int a = 0; a < seats.size; a += 2){
            seat1 = getSeat(a);
            seat2 = getSeat(a);
            if(a < seats.size){
                seat2 = getSeat(a+1);
            }

            top.append(getDashes(seat1.toString().length()));
            middle1.append(seat1.toString());
            middle2.append(seat2.toString());
            bottom.append(getDashes(seat1.toString().length()));
        }

        return top.toString()  + "+\n"
                + middle1.toString() + "|\n"
                + middle2.toString() + "|\n"
                + bottom.toString() + "+";
    }

    private String getDashes(int i){
        StringBuilder ret = new StringBuilder();
        for(int j = 0; j < i; j++){
            ret.append("-");
        }
        return ret.toString();
    }
}
