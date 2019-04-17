package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GameBoard;
import net.asg.games.provider.actors.GameLounge;
import net.asg.games.service.UserInterfaceService;
import net.asg.games.utils.Util;

public class GameBlockAreaLmlTag extends AbstractNonParentalActorLmlTag {
    @Inject private UserInterfaceService uiService;

    public GameBlockAreaLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        GameBlockArea gameBlockArea = new GameBlockArea(1, getSkin(builder), uiService.getFactory());
        gameBlockArea.updateBlocks(new int[][]{
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1}});
        return gameBlockArea;
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
    }

    /** @return casted actor. */
    private GameBlockArea getGameBlockArea() {
        return (GameBlockArea) getActor();
    }
}
