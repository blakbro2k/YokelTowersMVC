package net.asg.games.server;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Queue;
import net.asg.games.utils.Util;

/**
 * Created by Blakbro2k on 1/28/2018.
 */

public class YokelRoom implements Json.Serializable{
    public static final String SOCIAL_GROUP = "Social";
    public static final String BEGINNER_GROUP = "Begginer";
    public static final String INTERMEDIATE_GROUP = "Intermediate";
    public static final String ADVANCED_GROUP = "Advanced";

    private String name;
    private String roomId;
    private String group;
    //private Chat chatRoom;
    private Queue<YokelPlayer> players;
    private Queue<YokelTable> tables;

    public YokelRoom(String name){
        this.name = name;
        initialize();
    }

    private void initialize(){
        roomId = Util.IDGenerator.getID();
        tables = new Queue<YokelTable>();
        players = new Queue<YokelPlayer>();
    }

    @Override
    public void write(Json json) {

    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }

    public String getRoomId() {
        return roomId;
    }

    public Queue<YokelTable> getTables(){
        return tables;
    }

    public boolean joinRoom(YokelPlayer player){
        if(player != null){
            players.addFirst(player);
            return true;
        }
        return false;
    }

    public boolean leaveRoom(YokelPlayer player){
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
    }
}
