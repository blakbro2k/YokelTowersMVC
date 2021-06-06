package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameNameLabel;

public class GameNameLabelLmlTag extends AbstractNonParentalActorLmlTag {
    public GameNameLabelLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return new GameNameLabel(getSkin(builder));
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
    }

    /** @return casted actor. */
    private GameNameLabel getGameBlockArea() {
        return (GameNameLabel) getActor();
    }
}