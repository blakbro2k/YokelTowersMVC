package net.asg.games.game.objects;

import com.badlogic.gdx.utils.OrderedMap;

import org.apache.commons.lang.StringUtils;

public class YokelLounge extends AbstractYokelObject {
    public static final String SOCIAL_GROUP = "Social";
    public static final String BEGINNER_GROUP = "Beginner";
    public static final String INTERMEDIATE_GROUP = "Intermediate";
    public static final String ADVANCED_GROUP = "Advanced";
    public static final String DEFAULT_LOUNGE = "Default";

    private String name;
    private OrderedMap<String, YokelRoom> rooms;

    public YokelLounge(){}

    public YokelLounge(String name) {
        if(name == null) throw new IllegalArgumentException("GameLounge name cannot be null.");
        setName(name);
        this.rooms = new OrderedMap<>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addRoom(YokelRoom room){
        if(room == null) throw new IllegalArgumentException("Cannot add room. Room is null");
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
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!this.getClass().equals(o.getClass())) return false;
        YokelLounge lounge = getLounge(o);
        return isNameSame(lounge) && isRoomsSame(lounge);
    }

    private YokelLounge getLounge(Object o){
        return (YokelLounge) o;
    }

    private boolean isNameSame(YokelLounge lounge){
        if(lounge == null) return false;
        return StringUtils.equalsIgnoreCase(name, lounge.getName());
    }

    private boolean isRoomsSame(YokelLounge lounge){
        if(rooms == null) return false;
        return rooms.equals(lounge.getAllRooms());
    }
}
