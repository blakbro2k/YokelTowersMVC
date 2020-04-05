package net.asg.games.provider.attributes;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameBlockArea;

public class GameBlockPreviewLmlAttribute implements LmlAttribute<GameBlockArea> {
    public GameBlockPreviewLmlAttribute() {}

    public Class<GameBlockArea> getHandledType() {
        return GameBlockArea.class;
    }

    public void process(LmlParser parser, LmlTag tag, GameBlockArea actor, String rawAttributeData) {
        actor.setPreview(parser.parseBoolean(rawAttributeData));
    }
}