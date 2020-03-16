package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.utils.Util;

public class GameTableList extends Table {
    private final static String HEADER_TABLE_STR = "Table No.";
    private final static String HEADER_ONE_TWO_STR = "1-2";
    private final static String HEADER_THREE_FOUR_STR = "3-4";
    private final static String HEADER_FIVE_SIX_STR = "5-6";
    private final static String HEADER_SEVEN_EIGHT_STR = "7-8";
    private final static String HEADER_WATCHING_STR = "Who is Watching";

    private Table header;
    private Table tableList;

    public GameTableList(Skin skin){
        super(skin);

        header = new Table(skin);
        tableList = new Table(skin);

        //this.setFillParent(true);
        setUpHeader();
        setUpPlayerList();
    }

    private void setUpPlayerList() {
        ScrollPane scrollPane = new ScrollPane(tableList);
        tableList.pad(2);
        add(scrollPane);
    }

    private void setUpHeader() {
        header.pad(2);
        header.add(HEADER_TABLE_STR);
        header.add(HEADER_ONE_TWO_STR);
        header.add(HEADER_THREE_FOUR_STR);
        header.add(HEADER_FIVE_SIX_STR);
        header.add(HEADER_SEVEN_EIGHT_STR);
        header.add(HEADER_WATCHING_STR);
        add(header).row();
    }

    private void addRoom(YokelRoom room){
        Table table = new Table(getSkin());
        table.add(room.getName());
        //room.getTable()
        // removeActor();
        tableList.add(table).row();
    }

    public void updatePlayerList(Array<YokelRoom> rooms){
        tableList.clearChildren();
        for(YokelRoom room : Util.safeIterable(rooms)){
            if(room != null){
                addRoom(room);
            }
        }
    }
}
