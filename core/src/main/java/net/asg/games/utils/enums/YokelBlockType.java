package net.asg.games.utils.enums;

import com.badlogic.gdx.utils.GdxRuntimeException;

import net.asg.games.game.objects.YokelBlock;


/**
 * Created by Blakbro2k on 12/29/2017.
 */
public enum YokelBlockType {
    NormalY(YokelBlock.NORMAL_Y),
    NormalO(YokelBlock.NORMAL_O),
    NormalK(YokelBlock.NORMAL_K),
    NormalE(YokelBlock.NORMAL_E),
    NormalL(YokelBlock.NORMAL_L),
    NormalEx(YokelBlock.NORMAL_EX),
    AttackY(YokelBlock.ATTACK_Y),
    AttackO(YokelBlock.ATTACK_O),
    AttackK(YokelBlock.ATTACK_K),
    AttackE(YokelBlock.ATTACK_E),
    AttackL(YokelBlock.ATTACK_L),
    AttackEx(YokelBlock.ATTACK_EX),
    DefenseY(YokelBlock.DEFENSE_Y),
    DefenseO(YokelBlock.DEFENSE_O),
    DefenseK(YokelBlock.DEFENSE_K),
    DefenseE(YokelBlock.DEFENSE_E),
    DefenseL(YokelBlock.DEFENSE_L),
    DefenseEx(YokelBlock.DEFENSE_EX),
    BrokenY(YokelBlock.BROKEN_Y),
    BrokenO(YokelBlock.BROKEN_O),
    BrokenK(YokelBlock.BROKEN_K),
    BrokenE(YokelBlock.BROKEN_E),
    BrokenL(YokelBlock.BROKEN_L),
    BrokenEx(YokelBlock.BROKEN_EX),
    Medusa(YokelBlock.MEDUSA),
    Midas(YokelBlock.MIDAS),
    Stone(YokelBlock.STONE),
    Clear(YokelBlock.CLEAR);

    protected final int value;

    YokelBlockType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static YokelBlockType fromValue(int value) throws GdxRuntimeException {
        switch(value){
            case YokelBlock.NORMAL_Y:
                return NormalY;
            case YokelBlock.NORMAL_O:
                return NormalO;
            case YokelBlock.NORMAL_K:
                return NormalK;
            case YokelBlock.NORMAL_E:
                return NormalE;
            case YokelBlock.NORMAL_L:
                return NormalL;
            case YokelBlock.ATTACK_Y:
                return AttackY;
            case YokelBlock.ATTACK_O:
                return AttackO;
            case YokelBlock.ATTACK_K:
                return AttackK;
            case YokelBlock.ATTACK_E:
                return AttackE;
            case YokelBlock.ATTACK_L:
                return AttackL;
            case YokelBlock.DEFENSE_Y:
                return DefenseY;
            case YokelBlock.DEFENSE_O:
                return DefenseO;
            case YokelBlock.DEFENSE_K:
                return DefenseK;
            case YokelBlock.DEFENSE_E:
                return DefenseE;
            case YokelBlock.DEFENSE_L:
                return DefenseE;
            case YokelBlock.NORMAL_EX:
                return NormalEx;
            case YokelBlock.ATTACK_EX:
                return AttackEx;
            case YokelBlock.DEFENSE_EX:
                return DefenseEx;
            case YokelBlock.MEDUSA:
                return Medusa;
            case YokelBlock.MIDAS:
                return Midas;
            case YokelBlock.CLEAR:
                return Clear;
            case YokelBlock.BROKEN_Y:
                return BrokenY;
            case YokelBlock.BROKEN_O:
                return BrokenO;
            case YokelBlock.BROKEN_K:
                return BrokenK;
            case YokelBlock.BROKEN_E:
                return BrokenE;
            case YokelBlock.BROKEN_L:
                return BrokenL;
            case YokelBlock.BROKEN_EX:
                return BrokenEx;
            case YokelBlock.STONE:
                return Stone;
        }
        throw new ArrayIndexOutOfBoundsException("invalid value in YokelBlockType class: value=" + value);
    }
}
