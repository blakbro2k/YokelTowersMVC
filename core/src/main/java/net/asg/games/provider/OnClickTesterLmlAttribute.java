package net.asg.games.provider;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.czyzby.autumn.mvc.component.ui.action.ScreenTransitionAction;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.action.ActorConsumer;
import com.github.czyzby.lml.parser.tag.LmlAttribute;
import com.github.czyzby.lml.parser.tag.LmlTag;

public class OnClickTesterLmlAttribute implements LmlAttribute<Actor> {
    public OnClickTesterLmlAttribute() {
    }

    public Class<Actor> getHandledType() {
        return Actor.class;
    }

    public void process(LmlParser parser, LmlTag tag, final Actor actor, String rawAttributeData) {
        System.out.println("OnClickTesterLmlAttribute(rawAttributeData)=" + rawAttributeData);
        System.out.println("OnClickTesterLmlAttribute(tag)=" + tag);
        System.out.println("OnClickTesterLmlAttribute(parser)=" + parser.getClass());

        final ActorConsumer<?, Actor> action = parser.parseAction(rawAttributeData, actor);
        if (action == null) {
            parser.throwError("Could not find action for: " + rawAttributeData + " with actor: " + actor);
        }
        ScreenTransitionAction a;
        System.out.println("OnClickTesterLmlAttribute(action)=" + action);

        actor.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                action.consume(actor);
            }
        });
    }
}
