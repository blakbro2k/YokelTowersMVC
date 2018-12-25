package net.asg.games.utils;

import com.badlogic.gdx.utils.Queue;

//import net.asg.games.yokel.core.enums.YokelBlockType;

import java.util.Random;

/**
 * Created by Blakbro2k on 12/29/2017.
 */

public class RandomUtil {
    private static final int ATTACK_SECTION = 6;
    private static final int SECTION_GROUP_NUM = 6;
    private static final int DEFENSE_SECTION = 12;

    /**
     * @return a random {@link YokelBlockType}
     */
    /*
    public static YokelBlockType getRandomYokelBlockType() {
        RandomEnum<YokelBlockType> randomEnum = new RandomEnum<YokelBlockType>(YokelBlockType.class);
        return randomEnum.random();
    }

    public static YokelBlockType getRandomNormalYokelBlockType() {
        RandomEnum<YokelBlockType> randomEnum = new RandomEnum<YokelBlockType>(YokelBlockType.class);
        return randomEnum.random(0);
    }

    public static YokelBlockType getRandomOffensiveYokelBlockType() {
        RandomEnum<YokelBlockType> randomEnum = new RandomEnum<YokelBlockType>(YokelBlockType.class);
        return randomEnum.random(ATTACK_SECTION);
    }

    public static YokelBlockType getRandomDefensiveYokelBlockType() {
        RandomEnum<YokelBlockType> randomEnum = new RandomEnum<YokelBlockType>(YokelBlockType.class);
        return randomEnum.random(DEFENSE_SECTION);
    }

    public static Queue<YokelBlockType> getRandomBlockArray(){
        return getRandomBlockArray(1024);
    }

    private static Queue<YokelBlockType> getRandomBlockArray(int maxIndex){
        Queue<YokelBlockType> ret = new Queue<YokelBlockType>(maxIndex);
        for(int i = 0; i < maxIndex; i++){
            ret.addFirst(getRandomNormalYokelBlockType());
        }
        return ret;
    }*/

    /**
     * @see <a href="http://stackoverflow.com/a/1973018">Stack Overflow</a>
     */
    private static class RandomEnum<E extends Enum> {

        private static final Random RND = new Random();
        private final E[] values;

        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }

         /**
         * @return a random value for the given enums
         */
        public E random() {
            return values[RND.nextInt(values.length)];
        }

        /**
         * @return a random value for the given enums and a min and max range
         */
        public E random(int min) {
            return values[min + RND.nextInt(SECTION_GROUP_NUM)];
        }
    }
}
