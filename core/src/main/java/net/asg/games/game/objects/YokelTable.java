package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.utils.Log4LibGDXLogger;
import net.asg.games.utils.Log4LibGDXLoggerService;
import net.asg.games.utils.YokelUtilities;

import org.apache.commons.lang.StringUtils;

public class YokelTable extends AbstractYokelObject {
    private final Log4LibGDXLogger logger = Log4LibGDXLoggerService.forClass(YokelTable.class);
    private static final String ARG_TYPE = "type";
    private static final String ARG_RATED = "rated";
    public static final int MAX_SEATS = 8;

    public enum ACCESS_TYPE {PRIVATE("PRIVATE"), PUBLIC("PUBLIC"), PROTECTED("PROTECTED");
        private String accessType;

        ACCESS_TYPE(String accessType){
            this.accessType = accessType;
        }

        public String getValue() {
            return accessType;
        }
    }

    private int tableNumber;

    private Array<YokelSeat> seats;
    private ACCESS_TYPE accessType;
    private boolean isRated;
    private boolean isSoundOn;

    //Empty Constructor required for Json.Serializable
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
                setAccessType(YokelUtilities.otos(value));
            } else if(StringUtils.equalsIgnoreCase(ARG_RATED, arg)){
                setRated(YokelUtilities.otob(value));
            }
        }
    }

    public String getTableId() {
        return getId();
    }

    private void setTableNumber(int number){this.tableNumber = number;}

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
        int seatNumber = g * 2;
        return isSeatReady(getSeat(seatNumber)) || isSeatReady(getSeat(seatNumber + 1));
    }

    private boolean isSeatReady(YokelSeat seat){
        if(seat != null){
            return seat.isSeatReady();
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

    public boolean isStartable(){
        boolean startable = false;
        int count = 0;
        for(YokelSeat seat : seats){
            if(seat.isOccupied()){
                if(++count > 1){
                    startable = true;
                    break;
                }
            }
        }
        return startable;
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