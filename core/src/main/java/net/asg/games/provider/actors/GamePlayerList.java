package net.asg.games.provider.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import net.asg.games.game.objects.YokelPlayer;

public class GamePlayerList extends Table {
    private final static String HEADER_NAME_STR = "Name";
    private final static String HEADER_RATING_STR = "Rtng";

    private Table header;
    private Table playerList;

    public GamePlayerList(Skin skin){
        super(skin);

        header = new Table(skin);
        playerList = new Table(skin);

        setUpHeader();
        setUpPlayerList();
    }

    private void setUpPlayerList() {
        playerList.pad(2);
        add(playerList);
    }

    private void setUpHeader() {
        header.pad(2);
        header.add(HEADER_NAME_STR);
        header.add(HEADER_RATING_STR);
        header.row();
        add(header);
    }

    public void updatePlayerList(Array<YokelPlayer> players){

    }
}
