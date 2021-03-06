package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.utils.YokelUtilities;

public class GameBlockLmlTag extends AbstractNonParentalActorLmlTag {
    public GameBlockLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(LmlActorBuilder builder) {
        return YokelUtilities.getBlock(YokelBlock.CLEAR_BLOCK);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
    }

    /** @return casted actor. */
    private GameBlock getGameBlock() {
        return (GameBlock) getActor();
    }
}
