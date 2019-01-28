package net.asg.games.game.objects;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import net.asg.games.utils.RandomUtil;
import net.asg.games.utils.Util;
import net.asg.games.utils.enums.YokelBlockType;

/**
 * Created by Blakbro2k on 12/29/2017.
 */

public class YokelBlock extends YokelObject implements Json.Serializable {
    public enum INIT_TYPE {DEFENSE, POWER, ANY, NORMAL}

    private YokelBlockType blockType;
    private boolean isBroken;

    /**
     * Creates a random {@link YokelBlock}
     */
    public YokelBlock() {
        this(INIT_TYPE.ANY);
    }

    /**
     * Creates a random specified {@link YokelBlock}
     *
     * @param initType initializes a specified type group
     */
    public YokelBlock(INIT_TYPE initType) {
        blockType = initializeType(initType);
    }

    /**
     * Creates a specified {@link YokelBlock}
     *
     * @param type specified type
     */
    public YokelBlock(YokelBlockType type){
        blockType = type;
        isBroken = false;
    }

    private YokelBlockType initializeType(INIT_TYPE initType) {
        YokelBlockType type;

        try{
            switch (initType){
                case DEFENSE:
                    type = RandomUtil.getRandomDefensiveYokelBlockType();
                    break;
                case ANY:
                    type = RandomUtil.getRandomYokelBlockType();
                    break;
                case POWER:
                    type = RandomUtil.getRandomOffensiveYokelBlockType();
                    break;
                case NORMAL:
                    type = RandomUtil.getRandomNormalYokelBlockType();
                    break;
                default:
                    throw new GdxRuntimeException("Invalid default YokelBlock Type");
            }
        } catch (Exception e){
            throw new GdxRuntimeException(e);
        }
        return type;
    }

    public void setBroken(boolean b){
        isBroken = b;
    }

    public boolean isBroken(){
        return isBroken;
    }

    @Override
    public void write(Json json) {
        json.writeValue("blockType", blockType.getValue());
        json.writeValue("isBroken", isBroken);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        int val = json.readValue("blockType", Integer.class, jsonData);
        blockType = YokelBlockType.fromValue(val);
        isBroken = json.readValue("isBroken", Boolean.class, jsonData);
    }

    public YokelBlockType getType() {
        return blockType;
    }

    public boolean matchesType(Object o){
        if(o instanceof YokelBlock){
            return blockType.equals(((YokelBlock) o).getType());
        } else if(o instanceof YokelBlockType){
            return blockType.equals(o);
        } else {
            return false;
        }
    }

    public boolean matchesGenericType(YokelBlock block){
        if(Util.isYBlock(this)){
            return Util.isYBlock(block);
        }
        if(Util.isOBlock(this)){
            return Util.isOBlock(block);
        }
        if(Util.isKBlock(this)){
            return Util.isKBlock(block);
        }
        if(Util.isEBlock(this)){
            return Util.isEBlock(block);
        }
        if(Util.isLBlock(this)){
            return Util.isLBlock(block);
        }
        if(Util.isExclamationBlock(this)){
            return Util.isExclamationBlock(block);
        }
        if(Util.isMedusa(this)){
            return Util.isMedusa(block);
        }
        if(Util.isMidas(this)){
            return Util.isMidas(block);
        }
        if(Util.isClearBlock(this)){
            return Util.isClearBlock(block);
        }
        if(Util.isStone(this)){
            return Util.isStone(block);
        }
        return false;
    }

    public YokelBlock promote(boolean isPower){
        if(isPower){
            //change to Power Type
            blockType = Util.getPowerType(this);
        } else {
            //change to Defense Type
            blockType = Util.getDefenseType(this);
        }
        return this;
    }

    public YokelBlock demote(){
        blockType = Util.getNormalType(this);
        return this;
    }

    public YokelBlock breakBlock(){
        YokelBlock block = null;
        if(Util.isPowerBlock(this) || Util.isDefenseBlock(this)){
            block = new YokelBlock(blockType);
        }

        blockType = Util.getBrokenType(this);
        return block;
    }

    @Override
    public boolean equals (Object o){
        if(o instanceof YokelBlock){
            return matchesType(o);
        }
        return false;
    }

    @Override
    public void dispose() {}

    public YokelBlock copy() {
        return new YokelBlock(blockType);
    }
}
