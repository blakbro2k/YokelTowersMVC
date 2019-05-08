package net.asg.games.utils;

import com.badlogic.gdx.utils.Queue;

import net.asg.games.utils.enums.YokelBlockType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

//import net.asg.games.yokel.core.enums.YokelBlockType;

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

    public static Queue<YokelBlockType> getRandomBlockArray() {
        return getRandomBlockArray(1024);
    }

    private static Queue<YokelBlockType> getRandomBlockArray(int maxIndex) {
        Queue<YokelBlockType> ret = new Queue<YokelBlockType>(maxIndex);
        for (int i = 0; i < maxIndex; i++) {
            ret.addFirst(getRandomNormalYokelBlockType());
        }
        return ret;
    }

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

    public static final class RandomNumber {
        private long seed;
        //private boolean c = false;
        //private static final long f = (1L << 48) - 1L;

        public RandomNumber() {
            this(System.currentTimeMillis());
        }

        public RandomNumber(long l) {
            generateSeedByKey(l);
        }

        public synchronized void generateSeedByKey(long l) {
            seed = (l ^ 0x5deece66dL) & (1L << 48) - 1L;
            //c = false;
        }

        protected synchronized int moveSeed(int i) {
            long l = seed * 25214903917L + 11L & (1L << 48) - 1L;
            seed = l;
            return (int) (l >>> 48 - i);
        }

        public int generateCappedNumber() {
            return moveSeed(32);
        }

        public int next(int i) {
            return (generateCappedNumber() & 0x7fffffff) % i;
        }

        public void printOut(DataOutputStream dataoutputstream) throws IOException {
            dataoutputstream.writeLong(seed);
        }

        public void readIn(DataInputStream datainputstream) throws IOException {
            seed = datainputstream.readLong();
        }
    }

    public static class RandomNumberArray {
        int[] randomNumbers;

        public RandomNumberArray(int byteLength, long seed, int maxValue) {
            randomNumbers = new int[byteLength];
            RandomNumber numberGenerator = new RandomNumber(seed);
            for (int i = 0; i < byteLength; i++)
                randomNumbers[i] = numberGenerator.next(maxValue);
        }

        public int getRandomNumberAt(int index) {
            if (index < 0)
                System.out.println("Assertion failure: invalid random index " + index);
            return randomNumbers[index % randomNumbers.length];
        }
    }
}
