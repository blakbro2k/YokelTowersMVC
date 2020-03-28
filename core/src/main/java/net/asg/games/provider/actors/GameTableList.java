package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;

import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.Util;

public class GameTableList extends Table {
    private final static String HEADER_TABLE_STR = "Table";
    private final static String HEADER_ONE_TWO_STR = "Team 1-2";
    private final static String HEADER_THREE_FOUR_STR = "Team 3-4";
    private final static String HEADER_FIVE_SIX_STR = "Team 5-6";
    private final static String HEADER_SEVEN_EIGHT_STR = "Team 7-8";
    private final static String HEADER_WATCHING_STR = "Who is Watching";
    private final static String JOIN_STR = "Join";
    private final static String WATCH_STR = "Watch";

    private Table header;
    private Table tableList;

    public GameTableList(Skin skin){
        super(skin);

        header = new Table(skin);
        tableList = new Table(skin);
        //tableList.setFillParent(true);

        setUpHeader();
        setUpTableList();
    }

    private void setUpTableList() {
        ScrollPane scrollPane = new ScrollPane(tableList);
        scrollPane.setScrollbarsVisible(true);
        scrollPane.setScrollBarPositions(false, true);
        tableList.pad(2);
        add(scrollPane);
    }

    private void setUpHeader() {
        header.pad(2);
        header.add(labelize(HEADER_TABLE_STR)).left();
        header.add(labelize(HEADER_ONE_TWO_STR)).left();
        header.add(labelize(HEADER_THREE_FOUR_STR)).left();
        header.add(labelize(HEADER_FIVE_SIX_STR)).left();
        header.add(labelize(HEADER_SEVEN_EIGHT_STR)).left();
        header.add(labelize(HEADER_WATCHING_STR)).left();
        add(header).row();
    }

    private Label labelize(String text){
        return Util.createLabel(getSkin(), text, 1f);
    }

    private void addTable(YokelTable yTable){
        Table table = new Table(getSkin());
        table.add(labelize("#" + yTable.getTableNumber()));
        table.add(getWatchButton());

        Table table2 = new Table(getSkin());
        table2.add(getJoinButton(yTable.getSeat(0)));
        table2.add(getJoinButton(yTable.getSeat(1)));
        table2.add(getJoinButton(yTable.getSeat(2)));
        table2.add(getJoinButton(yTable.getSeat(3))).row();
        table2.add(getJoinButton(yTable.getSeat(4)));
        table2.add(getJoinButton(yTable.getSeat(5)));
        table2.add(getJoinButton(yTable.getSeat(6)));
        table2.add(getJoinButton(yTable.getSeat(7)));
        //room.getTable()
        //removeActor();
        tableList.add(table);
        tableList.add(table2).row();
    }

    private Button getWatchButton(){
        return new TextButton(WATCH_STR, getSkin());
    }

    private Button getJoinButton(YokelSeat seat){
        TextButton button = new TextButton(JOIN_STR, getSkin());
        int seatNumber = -1;
        if(seat != null){
            seatNumber = seat.getSeatNumber();
            if(seat.isOccupied()){
                button.setName("" + seatNumber);
                button.setText(seat.getSeatedPlayer().getName());
                button.setDisabled(true);
            }
        }
        return button;
    }

    public void updateTableList(Array<YokelTable> tables){
        tableList.clearChildren();
        for(YokelTable table : Util.safeIterable(tables)){
            if(table != null){
                addTable(table);
            }
        }
    }
}