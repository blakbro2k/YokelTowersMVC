package net.asg.games.provider;

import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.listener.ClickListenerLmlTag;
import com.github.czyzby.lml.parser.impl.tag.listener.provider.ClickListenerLmlTagProvider;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.parser.tag.LmlTagProvider;

public class TestClickListenerLmlTagProvider extends ClickListenerLmlTagProvider {
    public TestClickListenerLmlTagProvider() {
    }

    public LmlTag create(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        return new TestClickListenerLmlTag(parser, parentTag, rawTagData);
    }
}