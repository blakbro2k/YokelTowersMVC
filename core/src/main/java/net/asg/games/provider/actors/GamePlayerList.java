package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.utils.Util;

public class GamePlayerList extends Table {
    private final static String HEADER_NAME_STR = "Name";
    private final static String HEADER_RATING_STR = "Rtng";

    private Table header;
    private Table playerList;

    public GamePlayerList(Skin skin){
        super(skin);

        header = new Table(skin);
        playerList = new Table(skin);

        this.setFillParent(true);
        setUpHeader();
        setUpPlayerList();
    }

    private void setUpPlayerList() {
        ScrollPane scrollPane = new ScrollPane(playerList);
        playerList.pad(2);
        add(scrollPane);
    }

    private void setUpHeader() {
        header.pad(2);
        header.add(HEADER_NAME_STR);
        header.add(HEADER_RATING_STR);
        //header.row();
        add(header).row();
    }

    private void addPlayer(YokelPlayer player){
        Table table = new Table(getSkin());
        table.add(player.getName());
        table.add(player.getRating() + "");
        playerList.add(table).row();
    }

    public void updatePlayerList(Array<YokelPlayer> players){
        for(YokelPlayer player : Util.safeIterable(players)){
            if(player != null){
                System.out.println("Adding new player" + player);
                addPlayer(player);
            }
        }
    }
}
