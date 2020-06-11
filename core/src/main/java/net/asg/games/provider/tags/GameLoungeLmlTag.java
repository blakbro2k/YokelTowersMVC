package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.attribute.OnChangeLmlAttribute;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;
import com.kotcrab.vis.ui.widget.VisTextButton;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.provider.actors.GameLounge;
import net.asg.games.utils.YokelUtilities;

public class GameLoungeLmlTag extends TableLmlTag {
    GameLoungeLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return createGameLounge(builder);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        final GameLounge gameLounge = getLounge();

        YokelLounge lounge = YokelUtilities.getObjectFromJsonString(YokelLounge.class, YokelUtilities.stringToJson(plainTextLine));
        if(lounge != null){
            gameLounge.setLounge(lounge);
            LmlUtilities.setActorId(gameLounge, lounge.getName());
            setUpRooms(gameLounge);
        }

        if (LmlUtilities.isOneColumn(gameLounge)) {
            gameLounge.row();
        }
    }

    /** @return casted actor. */
    private GameLounge getLounge() {
        return (GameLounge) getActor();
    }

    private GameLounge createGameLounge(final LmlActorBuilder builder){
        return new GameLounge(getSkin(builder));
    }

    public void setUpRooms(GameLounge lounge){
        for(YokelRoom room : lounge.getAllRooms().values()){
            if(room != null){
                String roomName = getRoomName(room);
                String loungeName = lounge.getName();
                lounge.add(createNewRoomButton(loungeName, roomName)).row();
            }
        }
    }

    private String getRoomName(YokelRoom room){
        String roomName = "NotFound";
        if(room != null){
            roomName = room.getName();
        }
        return roomName;
    }

    private VisTextButton createNewRoomButton(String loungeName, String roomName){
        VisTextButton button = new VisTextButton(roomName);
        OnChangeLmlAttribute onChange = new OnChangeLmlAttribute();
        LmlUtilities.setActorId(button, loungeName);
        onChange.process(getParser(), this, button, LmlUtilities.toAction("selectLounge"));
        return button;
    }
}