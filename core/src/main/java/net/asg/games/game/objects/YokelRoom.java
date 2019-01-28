package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelRoom extends YokelObject {
    private String name;
    private String roomId;
    private String group;
    //private Chat chatRoom;
    private Array<YokelPlayer> players;
    private OrderedMap<Integer, YokelTable> tables;

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

    public void addTable(int num){
        tables.put(num, new YokelTable(num));
    }

    public void addTable(int num, OrderedMap<String, Object> attributes){
        tables.put(num, new YokelTable(num, attributes));
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
    public void dispose() {
        if(players != null){
            players.clear();
        }

        if(tables != null){
            tables.clear();
        }
    }
}
