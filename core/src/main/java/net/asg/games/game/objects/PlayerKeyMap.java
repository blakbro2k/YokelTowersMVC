package net.asg.games.game.objects;

import com.badlogic.gdx.Input;

public class PlayerKeyMap {
    private int[] keyMap = {Input.Keys.RIGHT,
            Input.Keys.LEFT,
            Input.Keys.UP,
            Input.Keys.DOWN,
            Input.Keys.P,
            Input.Keys.NUM_1,
            Input.Keys.NUM_2,
            Input.Keys.NUM_3,
            Input.Keys.NUM_4,
            Input.Keys.NUM_5,
            Input.Keys.NUM_6,
            Input.Keys.NUM_7,
            Input.Keys.NUM_8,
            Input.Keys.SPACE};

    public int getRightKey(){
        return keyMap[0];
    }

    public int getLeftKey(){
        return keyMap[1];
    }

    public int getCycleDownKey(){
        return keyMap[2];
    }

    public int getCycleUpKey(){
        return keyMap[4];
    }

    public int getDownKey(){
        return keyMap[3];
    }

    public int getRandomAttackKey(){
        return keyMap[13];
    }

    public int getTarget1(){
        return keyMap[5];
    }

    public int getTarget2(){
        return keyMap[6];
    }

    public int getTarget3(){
        return keyMap[7];
    }

    public int getTarget4(){
        return keyMap[8];
    }

    public int getTarget5(){
        return keyMap[9];
    }

    public int getTarget6(){
        return keyMap[10];
    }

    public int getTarget7(){
        return keyMap[11];
    }

    public int getTarget8(){
        return keyMap[12];
    }
}
