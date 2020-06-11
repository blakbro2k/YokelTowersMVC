package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.util.LmlUtilities;

import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.YokelUtilities;

public class GameTableList extends Table {
    private final static String HEADER_TABLE_STR = "Table";
    private final static String HEADER_ONE_TWO_STR = "Team 1-2";
    private final static String HEADER_THREE_FOUR_STR = "Team 3-4";
    private final static String HEADER_FIVE_SIX_STR = "Team 5-6";
    private final static String HEADER_SEVEN_EIGHT_STR = "Team 7-8";
    private final static String HEADER_WATCHING_STR = "Who is Watching";
    private final static String JOIN_STR = "Join";
    private final static String WATCH_STR = "Watch";

    public final static String TABLE_SEPARATOR = ":";
    public final static String TABLE_LIST_ATTR = "tableList";

    private Table header;
    private Table tableList;

    public GameTableList(Skin skin){
        super(skin);

        header = new Table(skin);
        tableList = new Table(skin);

        tableList.left().align(Align.top);

        setUpHeader();
        setUpTableList();
    }

    private void setUpTableList() {
        ScrollPane scrollPane = new ScrollPane(tableList);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setScrollbarsVisible(true);

        tableList.pad(2);
        add(scrollPane).growX().align(Align.top);
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
        return YokelUtilities.createLabel(getSkin(), text, 1f);
    }

    private void addTable(YokelTable yTable){
        Table table = new Table(getSkin());
        int tableNumber = yTable.getTableNumber();

        table.add(labelize("#" + tableNumber));
        table.add(getWatchButton());

        Table table2 = new Table(getSkin());
        LmlUtilities.setActorId(table2, TABLE_LIST_ATTR);
        table2.add(getJoinButton(tableNumber, yTable.getSeat(0)));
        table2.add(getJoinButton(tableNumber, yTable.getSeat(2)));
        table2.add(getJoinButton(tableNumber, yTable.getSeat(4)));
        table2.add(getJoinButton(tableNumber, yTable.getSeat(6))).row();
        table2.add(getJoinButton(tableNumber, yTable.getSeat(1)));
        table2.add(getJoinButton(tableNumber, yTable.getSeat(3)));
        table2.add(getJoinButton(tableNumber, yTable.getSeat(5)));
        table2.add(getJoinButton(tableNumber, yTable.getSeat(7)));

        tableList.add(table);
        tableList.add(table2).row();
    }

    private Button getWatchButton(){
        return new TextButton(WATCH_STR, getSkin());
    }

    private Button getJoinButton(int tableNumber, YokelSeat seat){
        TextButton button = new TextButton(JOIN_STR, getSkin());

        int seatNumber;
        if(seat != null) {
            seatNumber = seat.getSeatNumber();
            button.setName(tableNumber + TABLE_SEPARATOR + seatNumber);

            if(seat.isOccupied()){
                button.setText(seat.getSeatedPlayer().getName());
                button.setDisabled(true);
            }
        }
        return button;
    }

    public void updateTableList(Array<YokelTable> tables){
        tableList.clearChildren();
        for(YokelTable table : YokelUtilities.safeIterable(tables)){
            if(table != null){
                addTable(table);
            }
        }
    }

    public Array<Button> getRoomsButtons(){
        Array<Button> buttons = GdxArrays.newArray();

            for(Cell cell : tableList.getCells()){
                Table actor = YokelUtilities.getActorFromCell(Table.class, cell);
                if(TABLE_LIST_ATTR.equalsIgnoreCase(YokelUtilities.getActorId(actor))){
                    Array<Cell> cells = actor.getCells();
                    for(Cell cell2 : cells){
                        Button button = YokelUtilities.getActorFromCell(Button.class, cell2);
                        if(button != null){
                            buttons.add(button);
                        }
                    }
                }
            }
        return buttons;
    }

    public static int getTableNumberFromButton(Button button) throws GdxRuntimeException {
        int tableNumber;
        String buttonName = YokelUtilities.getActorId(button);

        try {
            tableNumber = Integer.parseInt(buttonName.substring(0, buttonName.indexOf(TABLE_SEPARATOR)));
        } catch (Exception e) {
            throw new GdxRuntimeException("There was an error getting table number from button :" + buttonName, e);
        }

        return tableNumber;
    }

    public static int getSeatNumberFromButton(Button button) throws GdxRuntimeException {
        int seatNumber;
        String buttonName = YokelUtilities.getActorId(button);

        try {
            seatNumber = Integer.parseInt(buttonName.substring(buttonName.indexOf(TABLE_SEPARATOR) + 1));
        } catch (Exception e) {
            throw new GdxRuntimeException("There was an error getting seat number from button :" + buttonName, e);
        }

        return seatNumber;
    }
}