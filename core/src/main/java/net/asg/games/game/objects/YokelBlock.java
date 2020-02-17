package net.asg.games.game.objects;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Blakbro2k on 12/29/2017.
 */

public class YokelBlock extends AbstractYokelObject implements Pool.Poolable{
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
    public static final int MINOR_POWER_LEVEL = 0;
    public static final int NORMAL_POWER_LEVEL = 1;
    public static final int MEGA_POWER_LEVEL = 2;
    public static final int SPECIAL_BLOCK_1 = 1024;
    public static final int SPECIAL_BLOCK_2 = 1026;
    public static final int SPECIAL_BLOCK_3 = 1024;

    public int x;
    public int y;
    private int type;

    public YokelBlock(int x, int y, int type){
        reset();
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public YokelBlock(int x, int y){
        this(x,y,CLEAR_BLOCK);
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

    /*
    @Override
    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("x", x);
        json.writeValue("y", y);
        json.writeValue("type", type);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.id = json.readValue("id", String.class, jsonData);
        this.x = json.readValue("x", Integer.class, jsonData);
        this.y = json.readValue("y", Integer.class, jsonData);
        this.type = json.readValue("type", Integer.class, jsonData);
    }*/
}
