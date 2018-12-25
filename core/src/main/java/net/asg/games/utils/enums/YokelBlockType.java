package net.asg.games.utils.enums;

import com.badlogic.gdx.utils.GdxRuntimeException;

import net.asg.games.utils.GlobalConstants;


/**
 * Created by Blakbro2k on 12/29/2017.
 */
public enum YokelBlockType {
    NormalY(GlobalConstants.NORMAL_Y),
    NormalO(GlobalConstants.NORMAL_O),
    NormalK(GlobalConstants.NORMAL_K),
    NormalE(GlobalConstants.NORMAL_E),
    NormalL(GlobalConstants.NORMAL_L),
    NormalEx(GlobalConstants.NORMAL_EX),
    AttackY(GlobalConstants.ATTACK_Y),
    AttackO(GlobalConstants.ATTACK_O),
    AttackK(GlobalConstants.ATTACK_K),
    AttackE(GlobalConstants.ATTACK_E),
    AttackL(GlobalConstants.ATTACK_L),
    AttackEx(GlobalConstants.ATTACK_EX),
    DefenseY(GlobalConstants.DEFENSE_Y),
    DefenseO(GlobalConstants.DEFENSE_O),
    DefenseK(GlobalConstants.DEFENSE_K),
    DefenseE(GlobalConstants.DEFENSE_E),
    DefenseL(GlobalConstants.DEFENSE_L),
    DefenseEx(GlobalConstants.DEFENSE_EX),
    BrokenY(GlobalConstants.BROKEN_Y),
    BrokenO(GlobalConstants.BROKEN_O),
    BrokenK(GlobalConstants.BROKEN_K),
    BrokenE(GlobalConstants.BROKEN_E),
    BrokenL(GlobalConstants.BROKEN_L),
    BrokenEx(GlobalConstants.BROKEN_EX),
    Medusa(GlobalConstants.MEDUSA),
    Midas(GlobalConstants.MIDAS),
    Stone(GlobalConstants.STONE),
    Clear(GlobalConstants.CLEAR_BLOCK);

    protected final int value;

    YokelBlockType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static YokelBlockType fromValue(int value) throws GdxRuntimeException {
        switch(value){
            case GlobalConstants.NORMAL_Y:
                return NormalY;
            case GlobalConstants.NORMAL_O:
                return NormalO;
            case GlobalConstants.NORMAL_K:
                return NormalK;
            case GlobalConstants.NORMAL_E:
                return NormalE;
            case GlobalConstants.NORMAL_L:
                return NormalL;
            case GlobalConstants.ATTACK_Y:
                return AttackY;
            case GlobalConstants.ATTACK_O:
                return AttackO;
            case GlobalConstants.ATTACK_K:
                return AttackK;
            case GlobalConstants.ATTACK_E:
                return AttackE;
            case GlobalConstants.ATTACK_L:
                return AttackL;
            case GlobalConstants.DEFENSE_Y:
                return DefenseY;
            case GlobalConstants.DEFENSE_O:
                return DefenseO;
            case GlobalConstants.DEFENSE_K:
                return DefenseK;
            case GlobalConstants.DEFENSE_E:
                return DefenseE;
            case GlobalConstants.DEFENSE_L:
                return DefenseE;
            case GlobalConstants.NORMAL_EX:
                return NormalEx;
            case GlobalConstants.ATTACK_EX:
                return AttackEx;
            case GlobalConstants.DEFENSE_EX:
                return DefenseEx;
            case GlobalConstants.MEDUSA:
                return Medusa;
            case GlobalConstants.MIDAS:
                return Midas;
            case GlobalConstants.CLEAR_BLOCK:
                return Clear;
            case GlobalConstants.BROKEN_Y:
                return BrokenY;
            case GlobalConstants.BROKEN_O:
                return BrokenO;
            case GlobalConstants.BROKEN_K:
                return BrokenK;
            case GlobalConstants.BROKEN_E:
                return BrokenE;
            case GlobalConstants.BROKEN_L:
                return BrokenL;
            case GlobalConstants.BROKEN_EX:
                return BrokenEx;
            case GlobalConstants.STONE:
                return Stone;
        }
        throw new ArrayIndexOutOfBoundsException("invalid value in YokelBlockType class: value=" + value);
    }
}
