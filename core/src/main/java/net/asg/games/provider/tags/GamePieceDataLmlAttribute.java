package net.asg.games.provider.tags;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GamePiece;

public class GamePieceDataLmlAttribute implements LmlAttribute<GamePiece> {
    public GamePieceDataLmlAttribute() {}

    public Class<GamePiece> getHandledType() {
        return GamePiece.class;
    }

    public void process(final LmlParser parser, final LmlTag tag, GamePiece actor, final String rawAttributeData) {
        actor.setData(parser.parseArray(rawAttributeData));
    }
}