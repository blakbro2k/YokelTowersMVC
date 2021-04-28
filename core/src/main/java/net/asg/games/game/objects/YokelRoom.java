package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.utils.YokelUtilities;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelRoom extends AbstractYokelObject {
    //private Chat chatRoom;
    private Array<YokelPlayer> players = new Array<>();
    private ObjectMap<Integer, YokelTable> tables = new ObjectMap<>();
    private int curTableNum = 0;

    //Empty Constructor required for Json.Serializable
    public YokelRoom(){}

    public YokelRoom(String name){
        setName(name);
    }

    public String getRoomId() {
        return getId();
    }

    public Array<YokelTable> getAllTables(){
        return YokelUtilities.getMapValues(tables).toArray();
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

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!this.getClass().equals(o.getClass())) return false;
        return isNameSame((YokelRoom) o);
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