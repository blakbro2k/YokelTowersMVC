package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.actor.ButtonLmlTag;
import com.github.czyzby.lml.parser.impl.tag.actor.ImageButtonLmlTag;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.impl.tag.actor.TextButtonLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.provider.actors.GameClock;
import net.asg.games.provider.actors.GameIcon;

public class PlayerIconLmlTag extends ImageButtonLmlTag {
    public PlayerIconLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        final Skin skin = getSkin(builder);
        return new GameIcon(GameIcon.getGameDefaultIconStyle(skin), skin);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
    }
}
