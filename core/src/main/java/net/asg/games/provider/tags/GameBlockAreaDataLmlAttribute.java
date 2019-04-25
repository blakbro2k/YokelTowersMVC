package net.asg.games.provider.tags;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameBlockArea;

public class GameBlockAreaDataLmlAttribute implements LmlAttribute<GameBlockArea> {
    public GameBlockAreaDataLmlAttribute() {}

    public Class<GameBlockArea> getHandledType() {
        return GameBlockArea.class;
    }

    public void process(LmlParser parser, LmlTag tag, GameBlockArea actor, String rawAttributeData) {
        ActorConsumer<?, Object> var = parser.parseAction(rawAttributeData);
        var.consume(actor);
        System.out.println(parser.parseAction(rawAttributeData));
        //System.out.println(rawAttributeData + "=" + parser.parseInt(rawAttributeData));
        //actor.setImage(parser.parseString(rawAttributeData, actor));
    }
}