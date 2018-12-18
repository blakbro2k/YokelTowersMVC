package net.asg.games.server;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Queue;
import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class Room implements Json.Serializable{
    private String name;
    private String roomId;
    //private Chat chatRoom;
    private Queue<Player> players;
    //private Queue<YokelTable> tables;

    public Room(String name){
        this(name, 8);
    }

    public Room(String name, int numTables){
        this.name = name;
        //initialize();
        //createTables(numTables);
    }

    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
/*
    private void createTables(int numTables) {
        if(numTables < 0){
            numTables = 0;
        }

        for(int i = 0; i < numTables; i ++){
            tables.addFirst(new YokelTable());
        }
    }

    private void initialize(){
        roomId = Util.IDGenerator.getID();
        tables = new Queue<YokelTable>();
        players = new Queue<Player>();
    }

    public String getRoomId() {
        return roomId;
    }

    public Queue<YokelTable> getTables(){
        return tables;
    }

    public boolean joinRoom(Player player){
        if(player != null){
            players.addFirst(player);
            return true;
        }
        return false;
    }

    public boolean leaveRoom(Player player){
            return players.removeValue(player, false);
    }

    public boolean addTable(){
        tables.addFirst(new YokelTable());
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
        for(YokelTable yokelTable : tables){
            ret.append(yokelTable.toString()).append("\n");
        }

        ret.append("Player List = ").append(players.toString());
        return ret.toString();
    }*/
}
