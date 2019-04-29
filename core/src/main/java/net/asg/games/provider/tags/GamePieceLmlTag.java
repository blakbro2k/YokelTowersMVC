package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.provider.actors.GamePiece;
import net.asg.games.utils.UIUtil;

public class GamePieceLmlTag extends AbstractNonParentalActorLmlTag {
    public GamePieceLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor ( final LmlActorBuilder builder){
        return new GamePiece(getSkin(builder), getClearBlock(),
                getClearBlock(),
                getClearBlock());
    }

    @Override
    protected void handlePlainTextLine ( final String plainTextLine){
    }

    /** @return casted actor. */
    private GamePiece getGamePiece() {
        return (GamePiece) getActor();
    }

    private GameBlock getClearBlock(){
        return UIUtil.getInstance().getFactory().getGameBlock(YokelBlock.CLEAR);
    }
}