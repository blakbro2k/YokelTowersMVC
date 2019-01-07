package net.asg.games.provider;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.kotcrab.vis.ui.widget.VisTextButton;

import net.asg.games.server.YokelLounge;
import net.asg.games.server.YokelRoom;
import net.asg.games.utils.Util;

public class LoungeLmlTag extends TableLmlTag {
    public LoungeLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new Lounge(getSkin(builder));
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        final Lounge lounge = getLounge();lounge.setDebug(true);
        //table.add(getParser().parseString(plainTextLine, getActor()));

        YokelLounge obj = Util.getObjectFromJsonString(YokelLounge.class, Util.revertJsonString(plainTextLine));

        lounge.add(obj.getName()).row();

        OrderedMap<String, YokelRoom> iter = obj.getAllRooms();
        if(iter != null){
            for(String roomName : Util.toIterable(iter.orderedKeys())){
                //lounge.setBackground("Background");
                lounge.add(buildRoomButton(iter.get(roomName))).fillX();
                lounge.row();
            }
        }

        /*if (LmlUtilities.isOneColumn(lounge)) {
            lounge.row();
        }*/
    }

    /** @return casted actor. */
    private Lounge getLounge() {
        return (Lounge) getActor();
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
        //button.addListener();

        return button;
    }
}
