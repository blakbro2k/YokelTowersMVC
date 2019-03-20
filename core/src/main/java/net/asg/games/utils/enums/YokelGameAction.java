package net.asg.games.utils.enums;

import com.badlogic.gdx.utils.GdxRuntimeException;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.utils.GlobalConstants;

/**
 * Created by Blakbro2k on 2/5/2018.
 */

public enum YokelGameAction {
    /**
     * Defensive Powers

     Y This block removes a row from your board
     O This block moves around ur blocks, making it easier to break them.
     K This block drops a stone to the bottom of ur board
     E This block sends a midas piece.
     L This block turns the blocks around spinning or electrified purple pieces into purple pieces.
     ! This removes a color from ur board. Good for yahooing
     */
    /**
     Offensive Powers

     Y This block adds a row to ur opponents board
     O This block moves around ur opponent's blocks, making it harder to break them.
     K This block sends a stone
     E This block sends a medusa pices
     L This block turns a special purple pices into a stone
     ! This removes powers.
     */
    DeleteRow(YokelBlock.DEFENSE_Y),
    ClumpBlocks(YokelBlock.DEFENSE_O),
    DropStones(YokelBlock.DEFENSE_K),
    AddMidas(YokelBlock.DEFENSE_E),
    TurnPowerL(YokelBlock.DEFENSE_L),
    RemoveColor(YokelBlock.DEFENSE_EX),
    AddRow(YokelBlock.ATTACK_Y),
    DitherBlocks(YokelBlock.ATTACK_O),
    AddStones(YokelBlock.ATTACK_K),
    AddMedusa(YokelBlock.ATTACK_E),
    TurnPowerStone(YokelBlock.ATTACK_L),
    RemovePowers(YokelBlock.ATTACK_EX);

    protected final int value;

    YokelGameAction(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static YokelGameAction fromValue(int value) throws GdxRuntimeException {
        switch(value){
            case YokelBlock.DEFENSE_Y:
                return DeleteRow;
            case YokelBlock.DEFENSE_O:
                return ClumpBlocks;
            case YokelBlock.DEFENSE_K:
                return DropStones;
            case YokelBlock.DEFENSE_E:
                return AddMidas;
            case YokelBlock.DEFENSE_L:
                return TurnPowerL;
            case YokelBlock.DEFENSE_EX:
                return RemoveColor;
            case YokelBlock.ATTACK_Y:
                return AddRow;
            case YokelBlock.ATTACK_O:
                return DitherBlocks;
            case YokelBlock.ATTACK_K:
                return AddStones;
            case YokelBlock.ATTACK_E:
                return AddMedusa;
            case YokelBlock.ATTACK_L:
                return TurnPowerStone;
            case YokelBlock.ATTACK_EX:
                return RemovePowers;
        }
        throw new ArrayIndexOutOfBoundsException("invalid value in YokelGameAction class: value=" + value);
    }
}
