package net.asg.games.game.objects;

import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.Queue;

import net.asg.games.utils.Util;

import org.apache.commons.lang.StringUtils;

public class YokelTable {
    public enum ACCESS_TYPE {PRIVATE, PUBLIC, PROTECTED}

    private final int MAX_SEATS = 8;
    private String tableId;
    private int tableNumber;

    private Queue<YokelSeat> seats;
    private ACCESS_TYPE accessType;
    private boolean isStarted;
    private boolean isRated;
    private boolean isSoundOn;

    //Empty Contructor required for Json.Serializable
    public YokelTable(){}

    public YokelTable(int tableNumber){
        initialize(tableNumber, null);
    }

    public YokelTable(int tableNumber, OrderedMap<String, Object> arguments){
        initialize(tableNumber, arguments);
    }

    private void initialize(int tableNumber, OrderedMap<String, Object> arguments){
        tableId = Util.IDGenerator.getID();
        seats = new Queue<YokelSeat>();
        this.tableNumber = tableNumber;

        setAccessType(ACCESS_TYPE.PUBLIC);
        setUpArguments(arguments);
        setUpSeats();
        setRated(false);
        setSound(true);
        retartTable();
    }

    private void setUpArguments(OrderedMap<String, Object> arguments){
        if(arguments != null){
            for(String key : arguments.keys()){
                if(key != null){
                    Object o = arguments.get(key);
                    processArg(key, o);
                }
            }
        }
    }

    private void processArg(String arg, Object value){
        if(arg != null && value != null){
            if(StringUtils.equalsIgnoreCase("type", arg)){
                setAccessType(Util.otos(value));
            } else if(StringUtils.equalsIgnoreCase("rated", arg)){
                setRated(Util.otob(value));
            }
        }
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

    public void setAccessType(String accessType){
        if(StringUtils.equalsIgnoreCase("private", accessType)){
            setAccessType(ACCESS_TYPE.PRIVATE);
        } else if(StringUtils.equalsIgnoreCase("public", accessType)){
            setRated(Util.otob(ACCESS_TYPE.PUBLIC));
        } else if(StringUtils.equalsIgnoreCase("protected", accessType)){
            setRated(Util.otob(ACCESS_TYPE.PROTECTED));
        }
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

    public void setRated(boolean rated){
        this.isRated = rated;
    }

    public void setSound(boolean sound){
        this.isSoundOn = sound;
    }

    public boolean isRated() {
        return isRated;
    }

    public boolean isSoundOn() {
        return isSoundOn;
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
        return "table: " + tableNumber + "\n" + ret.toString();
    }
}
