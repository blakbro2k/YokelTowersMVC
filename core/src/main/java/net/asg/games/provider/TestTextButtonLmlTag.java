package net.asg.games.provider;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.czyzby.kiwi.util.common.Strings;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.TextButtonLmlTag;
import com.github.czyzby.lml.parser.impl.tag.builder.TextLmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;

public class TestTextButtonLmlTag extends TextButtonLmlTag {
    public TestTextButtonLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    protected TextLmlActorBuilder getNewInstanceOfBuilder() {
        return new TextLmlActorBuilder();
    }

    protected void handlePlainTextLine(String plainTextLine) {
        System.out.println("TestTextButtonLmlTag=" + plainTextLine);
        TextButton button = this.getTextButton();
        String textToAppend = this.getParser().parseString(plainTextLine, this.getActor());
        if (Strings.isEmpty(button.getText())) {
            button.setText(textToAppend);
        } else if (LmlUtilities.isMultiline(button)) {
            button.setText(button.getText().toString() + '\n' + textToAppend);
        } else {
            button.setText(button.getText().toString() + textToAppend);
        }

    }
}
