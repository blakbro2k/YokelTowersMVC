package net.asg.games;

import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelGameBoard;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelPlayer;
import net.asg.games.game.objects.YokelRoom;
import net.asg.games.game.objects.YokelSeat;
import net.asg.games.game.objects.YokelTable;
import net.asg.games.utils.YokelUtilities;

import org.junit.Assert;
import org.junit.Test;

public class YokelBoardTest {

    @Test
    public void YokelLoungeTest() {
        YokelGameBoard board = new YokelGameBoard(1);
        //board.getNewNextPiece();

        //System.out.println(board);

        board.setValueAt(YokelBlock.Y_BLOCK, 0, 0);
        System.out.println(board);
        //board.setValueWithID();
    }
}