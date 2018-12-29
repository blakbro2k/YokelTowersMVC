package net.asg.games.server;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.OrderedMap;

import net.asg.games.utils.Util;

import org.apache.commons.lang.StringUtils;

public class YokelLounge implements Disposable {
    private String name;
    private OrderedMap<String, YokelRoom> rooms;

    public YokelLounge(){}

    public YokelLounge(String name) {
        setName(name);
        this.rooms = new OrderedMap<String, YokelRoom>();
    }

    public void setName(String name){
        this.name = name;
    }

    //Empty Contructor required for Json.Serializable
    public String getName(){
        return name;
    }

    public void addRoom(YokelRoom room){
        if(room == null) throw new NullPointerException("Cannot add room. Room is null");
        rooms.put(room.getName(), room);
    }

    public OrderedMap<String, YokelRoom> getAllRooms(){
        return rooms;
    }

    public YokelRoom getRoom(String roomName){
        if(roomName == null) return null;
        return rooms.get(roomName);
    }

    public void removeRoom(String roomName){
        if(roomName == null) return;
        rooms.remove(roomName);
    }

    @Override
    public void dispose() {
        if(rooms != null){
            rooms.clear();
        }
        name = null;
    }

    @Override
    public String toString(){
        StringBuilder loungeString = new StringBuilder();
        loungeString.append("[").append(getName()).append("]").append("\n");

        for(String roomName : Util.toIterable(rooms.orderedKeys())){
            if(roomName != null){
                loungeString.append("-").append(roomName).append("\n");
            }
        }
        String loungeStr = loungeString.toString();
        return StringUtils.substring(loungeStr, 0, loungeStr.length() - 1);
    }
}
