package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.github.czyzby.autumn.mvc.component.ui.InterfaceService;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.attribute.OnChangeLmlAttribute;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;
import com.kotcrab.vis.ui.widget.VisTextButton;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.provider.actors.GameLounge;
import net.asg.games.utils.Util;

public class GamePlayerLmlTag extends TableLmlTag {
    GamePlayerLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return createPlayerTag(builder);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        final Table playerTag = getPlayerTag();
        playerTag.pad(getDefaultPad(playerTag.getParent()));

        YokelPlayer player = Util.getObjectFromJsonString(YokelPlayer.class, Util.stringToJson(plainTextLine));

        if(player != null){

            playerTag.add(player.getName()).align(Align.left);
            playerTag.add(player.getRating() + "").align(Align.right);
            playerTag.row();
        }
    }

    private float getDefaultPad(Actor parent){
        if(parent instanceof Table){
            return ((Table) parent).getPadX();
        }
        return 0;
    }

    /** @return casted actor. */
    private Table getPlayerTag() {
        return (Table) getActor();
    }

    private Table createPlayerTag(final LmlActorBuilder builder){
        return new Table(getSkin(builder));
    }
}