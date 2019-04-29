package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.utils.UIUtil;

public class GameBlockAreaLmlTag extends AbstractNonParentalActorLmlTag {
    public GameBlockAreaLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return getGameBlockArea();
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
    }

    /** @return casted actor. */
    private GameBlockArea getGameBlockArea() {
        return new GameBlockArea(getFactory());
    }

    private YokelObjectFactory getFactory(){
        return UIUtil.getInstance().getFactory();
    }
}