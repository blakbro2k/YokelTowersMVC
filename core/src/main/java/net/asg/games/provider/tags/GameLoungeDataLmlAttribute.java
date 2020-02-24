package net.asg.games.provider.tags;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameLounge;
import net.asg.games.provider.actors.GamePiece;

public class GameLoungeDataLmlAttribute implements LmlAttribute<GameLounge> {
    public GameLoungeDataLmlAttribute() {}

    public Class<GameLounge> getHandledType() {
        return GameLounge.class;
    }

    public void process(final LmlParser parser, final LmlTag tag, GameLounge actor, final String rawAttributeData) {
        System.out.println(rawAttributeData);
        actor.setData(parser.parseString(rawAttributeData));
    }
}