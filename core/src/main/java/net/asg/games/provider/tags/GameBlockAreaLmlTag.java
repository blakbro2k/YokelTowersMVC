package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.autumn.annotation.Inject;
import com.github.czyzby.autumn.mvc.component.asset.AssetService;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.service.UserInterfaceService;
import net.asg.games.utils.UIUtil;

public class GameBlockAreaLmlTag extends AbstractNonParentalActorLmlTag {
    GameBlockArea gameBlockArea;

    public GameBlockAreaLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        if(gameBlockArea == null){
            initGameBoard();
        }
        return gameBlockArea;
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
    }

    /** @return casted actor. */
    private GameBlockArea getGameBlockArea() {
        if(gameBlockArea == null){
            initGameBoard();
        }
        return (GameBlockArea) getActor();
    }

    private void initGameBoard(){
        System.out.println("initGameBoard()=" + UIUtil.getInstance().getFactory());
        Object uiService= null;
        YokelObjectFactory factory = UIUtil.getInstance().getFactory();

        if(uiService != null){
            gameBlockArea = new GameBlockArea(1, getSkin(getNewInstanceOfBuilder()), factory);
            YokelGameBoard board = new YokelGameBoard();
            board.setCelles(0,0,1);
            board.setCelles(0,1,1);
            board.setCelles(0,2,1);
            board.setCelles(0,3,1);
            board.setCelles(0,4,1);
            board.setCelles(0,5,1);
            gameBlockArea.updateGameBoard(board);
        }
    }
}
