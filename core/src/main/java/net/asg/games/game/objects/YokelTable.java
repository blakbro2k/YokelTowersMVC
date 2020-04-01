package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.utils.Util;

import org.apache.commons.lang.StringUtils;

public class YokelTable extends AbstractYokelObject {
    public static final String ARG_TYPE = "type";
    public static final String ARG_RATED = "rated";

    public enum ACCESS_TYPE {PRIVATE("PRIVATE"), PUBLIC("PUBLIC"), PROTECTED("PROTECTED");
        private String accessType;

        ACCESS_TYPE(String accessType){
            this.accessType = accessType;
        }

        public String getValue() {
            return accessType;
        }
    }

    public static final int MAX_SEATS = 8;

    private int tableNumber;

    private Array<YokelSeat> seats;
    private ACCESS_TYPE accessType;
    private boolean isRated;
    private boolean isSoundOn;

    //Empty Contructor required for Json.Serializable
    public YokelTable(){}

    public YokelTable(int tableNumber){
        this(tableNumber, null);
    }

    public YokelTable(int tableNumber, OrderedMap<String, Object> arguments){
        initialize(tableNumber, arguments);
    }

    private void initialize(int tableNumber, OrderedMap<String, Object> arguments){
        if(tableNumber < 1) throw new IllegalArgumentException("Table number cannot be less than 1.");
        seats = new Array<>();

        setUpSeats();
        setTableNumber(tableNumber);
        setAccessType(ACCESS_TYPE.PUBLIC);
        setRated(false);
        setSound(true);
        setUpArguments(arguments);
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
            if(StringUtils.equalsIgnoreCase(ARG_TYPE, arg)){
                setAccessType(Util.otos(value));
            } else if(StringUtils.equalsIgnoreCase(ARG_RATED, arg)){
                setRated(Util.otob(value));
            }
        }
    }

    public String getTableId() {
        return getId();
    }

    public void setTableNumber(int number){this.tableNumber = number;}

    public int getTableNumber(){ return this.tableNumber;}

    public void setAccessType(ACCESS_TYPE accessType){
        this.accessType = accessType;
    }

    public void setAccessType(String accessType){
        if(StringUtils.equalsIgnoreCase(ACCESS_TYPE.PRIVATE.toString(), accessType)){
            setAccessType(ACCESS_TYPE.PRIVATE);
        } else if(StringUtils.equalsIgnoreCase(ACCESS_TYPE.PROTECTED.toString(), accessType)){
            setAccessType(ACCESS_TYPE.PROTECTED);
        } else {
            setAccessType(ACCESS_TYPE.PUBLIC);
        }
    }

    public ACCESS_TYPE getAccessType(){
        return accessType;
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

    public boolean isGroupReady(int g){
        if(g < 0 || g > 3){
            return false;
        }
        return isSeatOccupied(seats.get(g * 2)) || isSeatOccupied(seats.get((g * 2) + 1));
    }

    private boolean isSeatOccupied(YokelSeat seat){
        if(seat != null){
            return seat.isOccupied();
        }
        return false;
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

    private void setUpSeats(){
        for(int i = 0; i < MAX_SEATS; i++){
            seats.add(new YokelSeat(i));
        }
    }

    public Array<YokelSeat> getSeats(){
        return seats;
    }

    public YokelSeat getSeat(int seatNum){
        return seats.get(seatNum);
    }

    @Override
    public void dispose() {
        if(seats != null){
            seats.clear();
        }
    }
}
