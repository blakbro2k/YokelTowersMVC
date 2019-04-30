package net.asg.games.provider.tags;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.provider.actors.GamePiece;

public class GameBlockAreaDataLmlAttribute implements LmlAttribute<GameBlockArea> {
    public GameBlockAreaDataLmlAttribute() {}

    public Class<GameBlockArea> getHandledType() {
        return GameBlockArea.class;
    }

    public void process(final LmlParser parser, final LmlTag tag, GameBlockArea actor, final String rawAttributeData) {
        final ActorConsumer<?, GameBlockArea> action = parser.parseAction(rawAttributeData, actor);
        if (action == null) {
            parser.throwError("Could not find action for: " + rawAttributeData + " with actor: " + actor);
        } else {
            GamePiece gp = new GamePiece(actor.getSkin(),null,null,null);
            gp.setData(new String[]{"12","16","3"});
            actor.setGamePiece(gp);
            actor.updateData((YokelGameBoard) action.consume(actor));
        }
    }
}