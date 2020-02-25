package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewController;
import com.github.czyzby.autumn.mvc.component.ui.controller.ViewDialogController;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.attribute.OnChangeLmlAttribute;
import com.github.czyzby.lml.parser.impl.tag.actor.TableLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;
import com.github.czyzby.lml.util.LmlUtilities;
import com.kotcrab.vis.ui.widget.VisTextButton;

import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.provider.actors.GameLounge;
import net.asg.games.utils.Util;

public class GameLoungeLmlTag extends TableLmlTag {
    public GameLoungeLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return createGameLounge(builder);
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        final GameLounge gameLounge = getLounge();
        System.out.println("plainTextLine=" + plainTextLine);

        YokelLounge lounge = Util.getObjectFromJsonString(YokelLounge.class, Util.stringToJson(plainTextLine));
        gameLounge.setLounge(lounge);
        setUpRooms(gameLounge);


        //registerController(this, viewController);
        if (LmlUtilities.isOneColumn(gameLounge)) {
            gameLounge.row();
        }
    }

    /** @return casted actor. */
    private GameLounge getLounge() {
        return (GameLounge) getActor();
    }

    private GameLounge createGameLounge(final LmlActorBuilder builder){
        GameLounge lounge = new GameLounge(getSkin(builder));
        //lounge.setLounge();
        //lounge.setUpRooms(createNewRoomButton());
        //TODO: Create buttons to add here and add a viewController
        //lounge.addRoomListeners();
        //ViewDialogController vc = new ViewDialogController();
        //ViewController
        return lounge;
    }

    public void setUpRooms(GameLounge lounge){
        for(YokelRoom room : lounge.getAllRooms().values()){
            System.out.println("Setting up : " + room.getName());
            lounge.add(new Label(room.getName(), lounge.getSkin()));
            lounge.add(createNewRoomButton()).row();
        }
    }
    private VisTextButton createNewRoomButton(){
        return new VisTextButton("Enter Room", "default");
    }

    private String getRoomName(YokelRoom room){
        String roomName = "NotFound";
        if(room != null){
            roomName = room.getName();
        }
        return roomName;
    }
}