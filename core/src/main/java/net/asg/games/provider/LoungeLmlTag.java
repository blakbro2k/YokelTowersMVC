package net.asg.games.provider;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;

public class LoungeLmlTag extends TableLmlTag {
    public LoungeLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
        System.out.println("LoungeLmlTag");
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        System.out.println("getNewInstanceOfActor:" + this);
        return new Lounge(getSkin(builder));
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        final Table table = getTable();
        System.out.println("handlePlainTextLine:" + getActor().getClass());

        table.add(getParser().parseString(plainTextLine, getActor()));
        if (LmlUtilities.isOneColumn(table)) {
            table.row();
        }
    }
}
