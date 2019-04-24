package net.asg.games.provider.tags;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameBlock;

public class GameBlockTypeLmlAttribute implements LmlAttribute<GameBlock> {
    public GameBlockTypeLmlAttribute() {
    }

    public Class<GameBlock> getHandledType() {
        return GameBlock.class;
    }

    public void process(LmlParser parser, LmlTag tag, GameBlock actor, String rawAttributeData) {
        actor.setImage(parser.parseString(rawAttributeData, actor));
    }
}