package net.asg.games.provider;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractListenerLmlTag;
import com.github.czyzby.lml.parser.impl.tag.listener.ClickListenerLmlTag;
import com.github.czyzby.lml.parser.tag.LmlTag;

public class TestClickListenerLmlTag extends ClickListenerLmlTag {
    private final ClickListener listener = new ClickListener() {
        public void clicked(InputEvent event, float x, float y) {
            TestClickListenerLmlTag.this.doOnEvent(event.getListenerActor());
        }
    };

    public TestClickListenerLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
        System.out.println("TestClickListenerLmlTag raw=" + rawTagData);
    }

    protected ClickListener getEventListener() {
        return this.listener;
    }
}