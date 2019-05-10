package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.utils.Util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelRoom extends YokelObject {
    private String name;
    private String roomId;
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
        roomId = Util.IDGenerator.getID();
        tables = new OrderedMap<>();
        players = new Array<>();
    }

    public String getRoomId() {
        return roomId;
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

    public void addTable(int num, OrderedMap<String, Object> attributes){
        //TODO: Make table counts self contained
        if(attributes == null){
            tables.put(num, new YokelTable(num, attributes));
        } else {
            tables.put(num, new YokelTable(num));
        }
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
