package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameBlock;
import net.asg.games.utils.UIUtil;

public class GameBlockLmlTag extends AbstractNonParentalActorLmlTag {
    public GameBlockLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(LmlActorBuilder builder) {
        return UIUtil.getInstance().getFactory().getGameBlock(0);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        System.out.println("handle GameBlockType=" + plainTextLine );
    }

    /** @return casted actor. */
    private GameBlock getGameBlock() {
        return (GameBlock) getActor();
    }
}
