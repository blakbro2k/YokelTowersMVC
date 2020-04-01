package net.asg.games.provider.tags;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameBlockArea;

public class GameBlockPreviewLmlAttribute implements LmlAttribute<GameBlockArea> {
    @Override
    public Class<GameBlockArea> getHandledType() {
        return GameBlockArea.class;
    }

    @Override
    public void process(final LmlParser parser, final LmlTag tag, final GameBlockArea actor, final String rawAttributeData) {
        actor.setPreview(parser.parseBoolean(rawAttributeData, actor));
    }
}