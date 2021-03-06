package net.asg.games.provider.attributes;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameBoard;

public class GameBlockPreviewLmlAttribute implements LmlAttribute<GameBoard> {
    public GameBlockPreviewLmlAttribute() {}

    public Class<GameBoard> getHandledType() {
        return GameBoard.class;
    }

    public void process(LmlParser parser, LmlTag tag, GameBoard actor, String rawAttributeData) {
        actor.setPreview(parser.parseBoolean(rawAttributeData));
    }
}