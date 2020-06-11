package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GamePlayerList;

public class GamePlayerListLmlTag extends TableLmlTag {
    GamePlayerListLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return createGamePlayerListTag(builder);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
    }

    /** @return casted actor. */
    private GamePlayerList getPlayerTag() {
        return (GamePlayerList) getActor();
    }

    private GamePlayerList createGamePlayerListTag(final LmlActorBuilder builder){
        return new GamePlayerList(getSkin(builder));
    }
}