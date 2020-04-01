package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Pool;

/**
 * Created by Blakbro2k on 12/29/2017.
 */

public class YokelBlock extends AbstractYokelObject implements Pool.Poolable {
    /* Retrieves power "level"
     * - Even represents defensive powers ( 2, 4, 6 )
     * - Odd represents attack powers ( 3, 5, 7 )
     * ( MINOR, REGULAR, MEGA )*/

    public static final int Y_BLOCK = 0;
    public static final int O_BLOCK = 1;
    public static final int K_BLOCK = 2;
    public static final int E_BLOCK = 3;
    public static final int L_BLOCK = 4;
    public static final int EX_BLOCK = 5;
    public static final int CLEAR_BLOCK = 6;
    public static final int STONE = 7;
    public static final int MIDAS = 8;
    public static final int MEDUSA = 9;
    public static final int OFFENSIVE_MINOR = 3;
    public static final int OFFENSIVE_REGULAR = 5;
    public static final int OFFENSIVE_MEGA = 7;
    public static final int DEFENSIVE_MINOR = 2;
    public static final int DEFENSIVE_REGULAR = 4;
    public static final int DEFENSIVE_MEGA = 6;
    public static final int SPECIAL_BLOCK_1 = 1024;
    public static final int SPECIAL_BLOCK_2 = 1026;
    public static final int SPECIAL_BLOCK_3 = 1024;
    public static final int OFFENSIVE_Y_BLOCK_MINOR = 48;
    public static final int OFFENSIVE_Y_BLOCK_REGULAR = 80;
    public static final int OFFENSIVE_Y_BLOCK_MEGA = 112;
    public static final int DEFENSIVE_Y_BLOCK_MINOR = 32;
    public static final int DEFENSIVE_Y_BLOCK_REGULAR = 64;
    public static final int DEFENSIVE_Y_BLOCK_MEGA = 96;
    public static final int OFFENSIVE_O_BLOCK_MINOR = 49;
    public static final int OFFENSIVE_O_BLOCK_REGULAR = 81;
    public static final int OFFENSIVE_O_BLOCK_MEGA = 113;
    public static final int DEFENSIVE_O_BLOCK_MINOR = 33;
    public static final int DEFENSIVE_O_BLOCK_REGULAR = 65;
    public static final int DEFENSIVE_O_BLOCK_MEGA = 97;
    public static final int OFFENSIVE_K_BLOCK_MINOR = 50;
    public static final int OFFENSIVE_K_BLOCK_REGULAR = 82;
    public static final int OFFENSIVE_K_BLOCK_MEGA = 114;
    public static final int DEFENSIVE_K_BLOCK_MINOR = 34;
    public static final int DEFENSIVE_K_BLOCK_REGULAR = 66;
    public static final int DEFENSIVE_K_BLOCK_MEGA = 98;
    public static final int OFFENSIVE_E_BLOCK_MINOR = 51;
    public static final int OFFENSIVE_E_BLOCK_REGULAR = 83;
    public static final int OFFENSIVE_E_BLOCK_MEGA = 115;
    public static final int DEFENSIVE_E_BLOCK_MINOR = 35;
    public static final int DEFENSIVE_E_BLOCK_REGULAR = 67;
    public static final int DEFENSIVE_E_BLOCK_MEGA = 99;
    public static final int OFFENSIVE_L_BLOCK_MINOR = 52;
    public static final int OFFENSIVE_L_BLOCK_REGULAR = 84;
    public static final int OFFENSIVE_L_BLOCK_MEGA = 116;
    public static final int DEFENSIVE_L_BLOCK_MINOR = 36;
    public static final int DEFENSIVE_L_BLOCK_REGULAR = 68;
    public static final int DEFENSIVE_L_BLOCK_MEGA = 100;
    public static final int OFFENSIVE_BASH_BLOCK_MINOR = 53;
    public static final int OFFENSIVE_BASH_BLOCK_REGULAR = 85;
    public static final int OFFENSIVE_BASH_BLOCK_MEGA = 117;
    public static final int DEFENSIVE_BASH_BLOCK_MINOR = 37;
    public static final int DEFENSIVE_BASH_BLOCK_REGULAR = 69;
    public static final int DEFENSIVE_BASH_BLOCK_MEGA = 101;

    public int x;
    public int y;
    private int type;

    //Empty Contructor required for Json.Serializable
    public YokelBlock() {}

    public YokelBlock(int x, int y, int type) {
        reset();
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public YokelBlock(int x, int y) {
        this(x, y, CLEAR_BLOCK);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void reset() {
        this.x = 0;
        this.y = 0;
        this.type = CLEAR_BLOCK;
    }

    @Override
    public void dispose() {
        reset();
    }
}