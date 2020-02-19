package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.utils.Util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelRoom extends AbstractYokelObject {
    private String name;
    //private Chat chatRoom;
    private Array<YokelPlayer> players;
    private OrderedMap<Integer, YokelTable> tables;
    private int curTableNum = 0;

    //Empty Contructor required for Json.Serializable
    public YokelRoom(){}

    public YokelRoom(String name){
        this.name = name;
        initialize();
    }

    private void initialize(){
        tables = new OrderedMap<>();
        players = new Array<>();
    }

    public String getRoomId() {
        return getId();
    }

    public OrderedMap<Integer, YokelTable> getAllTables(){
        return tables;
    }

    public Array<YokelPlayer> getAllPlayers(){
        return players;
    }

    public void joinRoom(YokelPlayer player){
        if(player != null && !players.contains(player, false)){
            players.add(player);
        }
    }

    public void leaveRoom(YokelPlayer player){
            players.removeValue(player, false);
    }

    public void addTable(OrderedMap<String, Object> attributes){
        int num = getNextTableNumber();
        if(attributes != null){
            tables.put(num, new YokelTable(num, attributes));
        } else {
            tables.put(num, new YokelTable(num));
        }
    }

    private int getNextTableNumber(){
        return ++curTableNum;
    }

    public YokelTable getTable(int t){
        return tables.get(t);
    }

    public void removeTable(int index){
        tables.remove(index);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!this.getClass().equals(o.getClass())) return false;
        YokelRoom room = getRoom(o);
        return isNameSame(room);
    }

    private YokelRoom getRoom(Object o){
        return (YokelRoom) o;
    }

    private boolean isNameSame(YokelRoom room){
        if(room == null) return false;
        return StringUtils.equalsIgnoreCase(name, room.getName());
    }

    @Override
    public void dispose() {
        if(players != null){
            players.clear();
        }

        if(tables != null){
            tables.clear();
        }
    }
}
