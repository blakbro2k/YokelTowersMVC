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
    public static final int A_BLOCK = 1;
    public static final int H_BLOCK = 2;
    public static final int Op_BLOCK = 3;
    public static final int Oy_BLOCK = 4;
    public static final int EX_BLOCK = 5;
    public static final int CLEAR_BLOCK = 6;
    public static final int STONE = 7;
    public static final int MEDUSA = 9;
    public static final int ACTIVE_MEDUSA = 2105;
    public static final int TOP_MIDAS = 8;
    public static final int ACTIVE_TOP_MIDAS = 2088;
    public static final int MID_MIDAS = 10;
    public static final int ACTIVE_MID_MIDAS = 2090;
    public static final int BOT_MIDAS = 11;
    public static final int ACTIVE_BOT_MIDAS = 2091;
    public static final int OFFENSIVE_MINOR = 3;
    public static final int OFFENSIVE_REGULAR = 5;
    public static final int OFFENSIVE_MEGA = 7;
    public static final int DEFENSIVE_MINOR = 2;
    public static final int DEFENSIVE_REGULAR = 4;
    public static final int DEFENSIVE_MEGA = 6;
    public static final int SPECIAL_BLOCK_1 = 1024;
    public static final int SPECIAL_BLOCK_2 = 1026;
    public static final int SPECIAL_BLOCK_3 = 1028;
    public static final int OFFENSIVE_Y_BLOCK_MINOR = 2096;
    public static final int OFFENSIVE_Y_BLOCK_REGULAR = 2128;
    public static final int OFFENSIVE_Y_BLOCK_MEGA = 2160;
    public static final int DEFENSIVE_Y_BLOCK_MINOR = 2080;
    public static final int DEFENSIVE_Y_BLOCK_REGULAR = 2112;
    public static final int DEFENSIVE_Y_BLOCK_MEGA = 2144;
    public static final int OFFENSIVE_O_BLOCK_MINOR = 2097;
    public static final int OFFENSIVE_O_BLOCK_REGULAR = 2129;
    public static final int OFFENSIVE_O_BLOCK_MEGA = 2161;
    public static final int DEFENSIVE_O_BLOCK_MINOR = 2081;
    public static final int DEFENSIVE_O_BLOCK_REGULAR = 2113;
    public static final int DEFENSIVE_O_BLOCK_MEGA = 2145;
    public static final int OFFENSIVE_K_BLOCK_MINOR = 2098;
    public static final int OFFENSIVE_K_BLOCK_REGULAR = 2030;
    public static final int OFFENSIVE_K_BLOCK_MEGA = 2130;
    public static final int DEFENSIVE_K_BLOCK_MINOR = 2082;
    public static final int DEFENSIVE_K_BLOCK_REGULAR = 2114;
    public static final int DEFENSIVE_K_BLOCK_MEGA = 2146;
    public static final int OFFENSIVE_E_BLOCK_MINOR = 2099;
    public static final int OFFENSIVE_E_BLOCK_REGULAR = 2131;
    public static final int OFFENSIVE_E_BLOCK_MEGA = 2163;
    public static final int DEFENSIVE_E_BLOCK_MINOR = 2083;
    public static final int DEFENSIVE_E_BLOCK_REGULAR = 2115;
    public static final int DEFENSIVE_E_BLOCK_MEGA = 2147;
    public static final int OFFENSIVE_L_BLOCK_MINOR = 2100;
    public static final int OFFENSIVE_L_BLOCK_REGULAR = 2132;
    public static final int OFFENSIVE_L_BLOCK_MEGA = 2164;
    public static final int DEFENSIVE_L_BLOCK_MINOR = 2084;
    public static final int DEFENSIVE_L_BLOCK_REGULAR = 2116;
    public static final int DEFENSIVE_L_BLOCK_MEGA = 2148;
    public static final int OFFENSIVE_BASH_BLOCK_MINOR = 2101;
    public static final int OFFENSIVE_BASH_BLOCK_REGULAR = 2133;
    public static final int OFFENSIVE_BASH_BLOCK_MEGA = 2165;
    public static final int DEFENSIVE_BASH_BLOCK_MINOR = 2085;
    public static final int DEFENSIVE_BASH_BLOCK_REGULAR = 2117;
    public static final int DEFENSIVE_BASH_BLOCK_MEGA = 2149;

    public int x;
    public int y;
    private int blockType;
    private int powerIntensity = 0;

    //Empty Constructor required for Json.Serializable
    public YokelBlock() {}

    public YokelBlock(int x, int y, int blockType) {
        reset();
        this.x = x;
        this.y = y;
        this.blockType = blockType;
    }

    public YokelBlock(int x, int y) {
        this(x, y, CLEAR_BLOCK);
    }

    public int getBlockType() {
        return blockType;
    }

    public void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    @Override
    public void reset() {
        this.x = 0;
        this.y = 0;
        this.blockType = CLEAR_BLOCK;
    }

    @Override
    public void dispose() {
        reset();
    }

    public void setPower(int intensity) {
        this.powerIntensity = intensity;
    }

    public int getPower() {
        if(powerIntensity == 1) {
            powerIntensity = 3;
        }
        if(powerIntensity > 7) {
            powerIntensity = 7;
        }
        return powerIntensity;
    }

    public boolean hasPower(){
        return powerIntensity > 0;
    }
}