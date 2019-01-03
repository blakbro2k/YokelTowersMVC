package net.asg.games.provider;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

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
        final Lounge lounge = getLounge();
        //table.add(getParser().parseString(plainTextLine, getActor()));

        YokelLounge obj = Util.getObjectFromJsonString(YokelLounge.class, Util.revertJsonString(plainTextLine));

        lounge.add(obj.getName()).row();

        OrderedMap<String, YokelRoom> iter = obj.getAllRooms();
        for(String roomName : iter.orderedKeys()){
            //lounge.setBackground("Background");
            lounge.add(iter.get(roomName).getName());
            lounge.row();
        }

        /*if (LmlUtilities.isOneColumn(lounge)) {
            lounge.row();
        }*/
    }

    /** @return casted actor. */
    private Lounge getLounge() {
        return (Lounge) getActor();
    }
}
