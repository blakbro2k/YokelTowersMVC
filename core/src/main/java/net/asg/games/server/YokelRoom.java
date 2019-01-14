package net.asg.games.server;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelRoom{
    private String name;
    private String roomId;
    private String group;
    //private Chat chatRoom;
    private Array<YokelPlayer> players;
    private Array<YokelTable> tables;

    //Empty Contructor required for Json.Serializable
    public YokelRoom(){}

    public YokelRoom(String name){
        this.name = name;
        initialize();
    }

    private void initialize(){
        roomId = Util.IDGenerator.getID();
        tables = new Array<YokelTable>();
        players = new Array<YokelPlayer>();
    }

    public String getRoomId() {
        return roomId;
    }

    public Array<YokelTable> getTables(){
        return tables;
    }

    public boolean joinRoom(YokelPlayer player){
        if(player != null){
            players.add(player);
            return true;
        }
        return false;
    }

    public boolean leaveRoom(YokelPlayer player){
            return players.removeValue(player, false);
    }

    public boolean addTable(int num){
        tables.add(new YokelTable(num));
        return true;
    }

    public boolean addTable(int i, OrderedMap<String, Object> attributes){
        tables.add(new YokelTable(i, attributes));
        return true;
    }

    public YokelTable getTable(int t){
        return tables.get(t);
    }

    public boolean removeTable(int index){
        return false;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();

        ret.append("Room : ").append(name).append("\n");
        for(YokelTable yokelTable : Util.toIterable(tables)){
            ret.append(yokelTable.toString()).append("\n");
        }

        //ret.append("Player List = ").append(players.toString());
        return ret.toString();
    }
}
