package net.asg.games.utils.enums;

import com.badlogic.gdx.utils.GdxRuntimeException;

import net.asg.games.utils.GlobalConstants;

/**
 * Created by Blakbro2k on 2/5/2018.
 */

public enum YokelAction {
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
    DeleteRow(GlobalConstants.DEFENSE_Y),
    ClumpBlocks(GlobalConstants.DEFENSE_O),
    DropStones(GlobalConstants.DEFENSE_K),
    AddMidas(GlobalConstants.DEFENSE_E),
    TurnPowerL(GlobalConstants.DEFENSE_L),
    RemoveColor(GlobalConstants.DEFENSE_EX),
    AddRow(GlobalConstants.ATTACK_Y),
    DitherBlocks(GlobalConstants.ATTACK_O),
    AddStones(GlobalConstants.ATTACK_K),
    AddMedusa(GlobalConstants.ATTACK_E),
    TurnPowerStone(GlobalConstants.ATTACK_L),
    RemovePowers(GlobalConstants.ATTACK_EX),
    GetRooms(GlobalConstants.GET_ROOMS),
    GetTables(GlobalConstants.GET_TABLES),
    GetSeats(GlobalConstants.GET_SEATS),
    PingServer(GlobalConstants.PING_SERVER),
    RegisterPlayer(GlobalConstants.REGISTER_PLAYER);

    protected final int value;

    YokelAction(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static YokelAction fromValue(int value) throws GdxRuntimeException {
        switch(value){
            case GlobalConstants.DEFENSE_Y:
                return DeleteRow;
            case GlobalConstants.DEFENSE_O:
                return ClumpBlocks;
            case GlobalConstants.DEFENSE_K:
                return DropStones;
            case GlobalConstants.DEFENSE_E:
                return AddMidas;
            case GlobalConstants.DEFENSE_L:
                return TurnPowerL;
            case GlobalConstants.DEFENSE_EX:
                return RemoveColor;
            case GlobalConstants.ATTACK_Y:
                return AddRow;
            case GlobalConstants.ATTACK_O:
                return DitherBlocks;
            case GlobalConstants.ATTACK_K:
                return AddStones;
            case GlobalConstants.ATTACK_E:
                return AddMedusa;
            case GlobalConstants.ATTACK_L:
                return TurnPowerStone;
            case GlobalConstants.ATTACK_EX:
                return RemovePowers;
            case GlobalConstants.PING_SERVER:
                return PingServer;
            case GlobalConstants.GET_ROOMS:
                return GetRooms;
            case GlobalConstants.GET_TABLES:
                return GetTables;
            case GlobalConstants.GET_SEATS:
                return GetSeats;
            case GlobalConstants.REGISTER_PLAYER:
                return RegisterPlayer;        }
        throw new ArrayIndexOutOfBoundsException("invalid value in YokelBlockType class: value=" + value);
    }
}
