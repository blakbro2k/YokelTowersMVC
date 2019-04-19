package net.asg.games.provider.tags;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.parser.impl.tag.AbstractNonParentalActorLmlTag;
import com.github.czyzby.lml.parser.tag.LmlActorBuilder;
import com.github.czyzby.lml.parser.tag.LmlTag;

import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelObjectFactory;
import net.asg.games.provider.actors.GameBlockArea;
import net.asg.games.utils.UIUtil;

public class GameBlockAreaLmlTag extends AbstractNonParentalActorLmlTag {
    private GameBlockArea gameBlockArea;


    public GameBlockAreaLmlTag(LmlParser parser, LmlTag parentTag, StringBuilder rawTagData) {
        super(parser, parentTag, rawTagData);
    }

    @Override
    protected Actor getNewInstanceOfActor(final LmlActorBuilder builder) {
        return getGameBlockArea();
    }

    @Override
    protected void handlePlainTextLine(final String plainTextLine) {
        System.out.println("gameblockarea text=" + plainTextLine);
    }

    /** @return casted actor. */
    private GameBlockArea getGameBlockArea() {
        if(gameBlockArea == null){
            initGameBoard();
        }
        return gameBlockArea;
    }

    private void initGameBoard(){
        gameBlockArea = new GameBlockArea(1, getSkin(getNewInstanceOfBuilder()), getFactory());
        gameBlockArea.updateGameBoard(getTestBoard());
    }

    private YokelObjectFactory getFactory(){
        return UIUtil.getInstance().getFactory();
    }

    private YokelGameBoard getTestBoard(){

        YokelGameBoard board = new YokelGameBoard();
        board.setCels(0,0,3);
        board.setCels(0,1,4);
        board.setCels(0,2,6);
        board.setCels(0,3,2);
        board.setCels(0,4,9);
        board.setCels(0,5,10);

        board.setCels(1,0,1);
        board.setCels(1,1,1);
        board.setCels(1,2,1);
        board.setCels(1,3,1);
        board.setCels(1,4,1);
        board.setCels(1,5,1);

        board.setCels(2,0,1);
        board.setCels(2,1,1);
        board.setCels(2,2,1);
        board.setCels(2,3,1);
        board.setCels(2,4,1);
        board.setCels(2,5,1);

        board.setCels(3,0,1);
        board.setCels(3,1,1);
        board.setCels(3,2,1);
        board.setCels(3,3,1);
        board.setCels(3,4,1);
        board.setCels(3,5,1);

        board.setCels(4,0,1);
        board.setCels(4,1,1);
        board.setCels(4,2,1);
        board.setCels(4,3,1);
        board.setCels(4,4,1);
        board.setCels(4,5,1);

        board.setCels(5,0,1);
        board.setCels(5,1,1);
        board.setCels(5,2,1);
        board.setCels(5,3,1);
        board.setCels(5,4,1);
        board.setCels(5,5,1);

        board.setCels(6,0,1);
        board.setCels(6,1,1);
        board.setCels(6,2,1);
        board.setCels(6,3,1);
        board.setCels(6,4,1);
        board.setCels(6,5,1);

        board.setCels(7,0,1);
        board.setCels(7,1,1);
        board.setCels(7,2,1);
        board.setCels(7,3,1);
        board.setCels(7,4,1);
        board.setCels(7,5,1);

        board.setCels(8,0,1);
        board.setCels(8,1,1);
        board.setCels(8,2,1);
        board.setCels(8,3,1);
        board.setCels(8,4,1);
        board.setCels(8,5,1);

        board.setCels(9,0,1);
        board.setCels(9,1,1);
        board.setCels(9,2,1);
        board.setCels(9,3,1);
        board.setCels(9,4,1);
        board.setCels(9,5,1);

        board.setCels(10,0,1);
        board.setCels(10,1,1);
        board.setCels(10,2,1);
        board.setCels(10,3,1);
        board.setCels(10,4,1);
        board.setCels(10,5,1);

        board.setCels(11,0,1);
        board.setCels(11,1,1);
        board.setCels(11,2,1);
        board.setCels(11,3,1);
        board.setCels(11,4,1);
        board.setCels(11,5,1);

        board.setCels(12,0,1);
        board.setCels(12,1,1);
        board.setCels(12,2,1);
        board.setCels(12,3,1);
        board.setCels(12,4,1);
        board.setCels(12,5,1);

        board.setCels(13,0,1);
        board.setCels(13,1,1);
        board.setCels(13,2,1);
        board.setCels(13,3,1);
        board.setCels(13,4,1);
        board.setCels(13,5,1);

        board.setCels(14,0,1);
        board.setCels(14,1,1);
        board.setCels(14,2,1);
        board.setCels(14,3,1);
        board.setCels(14,4,1);
        board.setCels(14,5,1);

        board.setCels(15,0,1);
        board.setCels(15,1,1);
        board.setCels(15,2,1);
        board.setCels(15,3,1);
        board.setCels(15,4,1);
        board.setCels(15,5,1);
        return board;
    }
}
