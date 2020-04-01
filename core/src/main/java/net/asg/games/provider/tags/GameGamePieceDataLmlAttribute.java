package net.asg.games.provider.tags;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GamePiece;

public class GameGamePieceDataLmlAttribute implements LmlAttribute<GamePiece> {
    public GameGamePieceDataLmlAttribute() {}

    public Class<GamePiece> getHandledType() {
        return GamePiece.class;
    }

    public void process(final LmlParser parser, final LmlTag tag, GamePiece actor, final String rawAttributeData) {
        actor.setData(rawAttributeData);
    }
}