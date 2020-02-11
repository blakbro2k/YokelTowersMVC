package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.attribute.OnChangeLmlAttribute;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.VisTextButton;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.provider.actors.GameLounge;
import net.asg.games.utils.Util;

public class GameLoungeLmlTag extends TableLmlTag {
    private final static OnChangeLmlAttribute onChange = new OnChangeLmlAttribute();

    public GameLoungeLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new GameLounge(getSkin(builder));
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        final GameLounge gameLounge = getLounge();
        YokelLounge obj = Util.getObjectFromJsonString(YokelLounge.class, Util.stringToJson(plainTextLine));

        gameLounge.add(obj.getName()).row();

        OrderedMap<String, YokelRoom> iter = obj.getAllRooms();
        if(iter != null){
            for(String roomName : iter.orderedKeys()){
                addChild(buildRoomButton(iter.get(roomName)));
                gameLounge.row();
            }
        }

        /*if (LmlUtilities.isOneColumn(gameLounge)) {
            gameLounge.row();
        }*/
    }

    /** @return casted actor. */
    private GameLounge getLounge() {
        return (GameLounge) getActor();
    }

    private String getRoomName(YokelRoom room){
        String roomName = "NotFound";
        if(room != null){
            roomName = room.getName();
        }
        return roomName;
    }

    private VisTextButton buildRoomButton(YokelRoom room){
        VisTextButton button = new VisTextButton(getRoomName(room));
        onChange.process(getParser(), this, button, "goto:room");
        return button;
    }
}