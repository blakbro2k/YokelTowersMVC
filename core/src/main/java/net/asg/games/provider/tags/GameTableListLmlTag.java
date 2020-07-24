package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.impl.attribute.OnChangeLmlAttribute;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;

import net.asg.games.provider.actors.GameTableList;

import static net.asg.games.provider.actors.GameTableList.TABLE_LIST_ATTR;

public class GameTableListLmlTag extends TableLmlTag {
    GameTableListLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return createGameTableListTag(builder);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
    }

    /** @return casted actor. */
    private GameTableList getGameTableList() {
        return (GameTableList) getActor();
    }

    private GameTableList createGameTableListTag(final LmlActorBuilder builder){
        return new GameTableList(getSkin(builder));
    }

    public static void setUpListeners(final LmlParser parser, final Array<Button> buttons){
        for(Button button : buttons){
            if(button != null){
                System.out.println("kkdf=" + button.getClickListener().getButton());
                //final ActorConsumer<?, Actor> action = parser.parseAction(LmlUtilities.toAction("requestJoinTable"), button);
                /*actor.addListener(new ChangeListener() {
                    @Override
                    public void changed(final ChangeEvent event, final Actor widget) {
                        action.consume(actor);
                    }
                });*/
                //TODO: manually consume actor if listener on button does not exist instead of creating new Chagne attributes.
                OnChangeLmlAttribute onChange = new OnChangeLmlAttribute();
                onChange.process(parser, null, button, LmlUtilities.toAction("requestJoinTable"));
            }
        }
    }
}