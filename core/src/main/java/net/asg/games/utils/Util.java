package net.asg.games.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SnapshotArray;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.provider.actors.GameBlock;
import net.asg.games.utils.enums.YokelBlockType;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.pmw.tinylog.Level;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Util {
    private final static Json json = new Json();


    public static class IDGenerator {
        private IDGenerator(){}

        public static String getID(){
            return StringUtils.replace(UUID.randomUUID() + "", "-","") ;
        }

        public static Array<String> getGroupOfIDs(int num) throws IllegalArgumentException{
            if(num > 0) {
                Array<String> ids = new Array<>();
                for(int i = 0; i < num; i++){
                    ids.add(getID());
                }
                return ids;
            }
            throw new IllegalArgumentException("Cannot create Group of ID's for number less than 1.");
        }
    }

    /**
     * @param numMonth Number value of Month requested
     * @return String with three letter consideration
     */
    public static String getThreeLetterMonth(int numMonth) throws GdxRuntimeException {
        String ret;
        switch (numMonth) {
            case 0:
                ret = "JAN";
                break;
            case 1:
                ret = "FEB";
                break;
            case 2:
                ret = "MAR";
                break;
            case 3:
                ret = "APR";
                break;
            case 4:
                ret = "MAY";
                break;
            case 5:
                ret = "JUN";
                break;
            case 6:
                ret = "JUL";
                break;
            case 7:
                ret = "AUG";
                break;
            case 8:
                ret = "SEP";
                break;
            case 9:
                ret = "OCT";
                break;
            case 10:
                ret = "NOV";
                break;
            case 11:
                ret = "DEC";
                break;
            default:
                ret = "";
                break;
        }
        return ret;
    }

    public static boolean containsAny(Array<Object> c1, Array<Object> c2, boolean identity){
        boolean containsAny = false;
        if(null != c1 && null != c2){
            for(Object o : toIterable(c2)){
                if (!c1.contains(o, identity)) {
                    continue;
                }
                containsAny = true;
                break;
            }
        }
        return containsAny;
    }

    /**
     * @param title
     * @return
     */
    public static String cleanTitle(String title) throws GdxRuntimeException{
        String ret = "";
        if (title != null) {
            ret = title.replace("#8211", "-")
                    .replace("#8217", "'")
                    .replace("#8220", "\"")
                    .replace("#8221", "\"")
                    .replace("#8230", "...")
                    .replace("#038", "&");
        }
        return ret;
    }

    public static <T> boolean isArrayEmpty(Array<T> collection){
        return collection == null || collection.size < 1;
    }

    public static boolean isStaticArrayEmpty(Object[] array){
        return array == null || array.length < 1;
    }

    public static <T> String[] toStringArray(Array<T> collection) {
        if(collection != null){
            int size = collection.size;
            String[] c2 = new String[size];

            for(int c = 0; c < size; c++){
                Object o = collection.get(c);
                if(o != null){
                    c2[c] = o.toString();
                }
            }
            return c2;
        }
        return null;
    }

    /**
     * Creates a new Iterable Array object.
     * @param array
     * @param <T>
     * @return
     */
    @NotNull
    public static <T> Array.ArrayIterable<T> toIterable(Array<T> array){
        if(!isArrayEmpty(array)){
            return new Array.ArrayIterable<>(array);
        }
        return new Array.ArrayIterable<>(GdxArrays.newArray());
    }

    public static String getJsonString(Object o){
        return json.toJson(o);
    }

    public static <T> T getObjectFromJsonString(Class<T> type, String jsonStr){
        return json.fromJson(type, jsonStr);
    }

    public static String convertJsonString(String str){
        return StringUtils.replace(StringUtils.replace(str, "{","["),"}","]");
    }

    public static String revertJsonString(String str){
        return StringUtils.replace(StringUtils.replace(str, "[","{"),"]","}");
    }

    public static String getStringValue(String[] objects, int index) {
        if(objects != null && index < objects.length){
            return objects[index];
        }
        return null;
    }

    public static boolean getBooleanValue(String[] objects, int index) {
        if(objects != null && index < objects.length){
            return otob(objects[index]);
        }
        return false;
    }

    public static<T> Array<T> getValuesArray(ObjectMap.Values<T> values) {
        if(values != null){
            return values.toArray();
        }
        return GdxArrays.newArray();
    }

    public static String getLoungeName(String key) {
        if(StringUtils.equalsIgnoreCase(key, "Beginner")){
            return YokelLounge.BEGINNER_GROUP;
        } else if(StringUtils.equalsIgnoreCase(key, "Intermediate")){
            return YokelLounge.INTERMEDIATE_GROUP;
        } else if(StringUtils.equalsIgnoreCase(key, "Advanced")){
            return YokelLounge.ADVANCED_GROUP;
        } else {
            return YokelLounge.SOCIAL_GROUP;
        }
    }


    public static boolean otob(Object o){
        if(o != null){
            return Boolean.valueOf(otos(o));
        }
        return false;
    }

    public static String otos(Object o){
        if(o != null){
            return o.toString();
        }
        return "";
    }

    public static int otoi(Object o){
        if(o != null){
            return Integer.valueOf(otos(o));
        }
        return -1;
    }

    public static long otol(Object o){
        if(o != null){
            return Long.valueOf(otos(o));
        }
        return -1;
    }

    public static float otof(Object o){
        if(o != null){
            return Float.valueOf(otos(o));
        }
        return -1;
    }

    public static Level getTinyLogLevel(String logLevel){
        if(StringUtils.equalsIgnoreCase("trace", logLevel)){
            return Level.TRACE;
        } else if(StringUtils.equalsIgnoreCase("debug", logLevel)){
            return Level.DEBUG;
        } else if(StringUtils.equalsIgnoreCase("warn", logLevel)){
            return Level.WARNING;
        } else if(StringUtils.equalsIgnoreCase("error", logLevel)){
            return Level.ERROR;
        } else {
            return Level.INFO;
        }
    }

    public static List<String> getFiles(File folder){
        List<String> retFileNames = new ArrayList<>();
        if(folder != null){
            File[] fileNames = folder.listFiles();

            if(fileNames != null){
                for(File file : fileNames){
                    if(file != null){
                        if(file.isDirectory()){
                            getFiles(file);
                        } else {
                            retFileNames.add(file.getName());
                        }
                    }
                }
            }
        }
        return retFileNames;
    }

    public static List<String> getFiles(String path){
        List<String> fileNames = new ArrayList<>();
        if(path != null){
            fileNames.addAll(getFiles(new File(path)));
        }
        return fileNames;
    }

    public static boolean isPowerBlock(YokelBlock block) throws GdxRuntimeException {
        return block.matchesType(YokelBlockType.AttackY)
                || block.matchesType(YokelBlockType.AttackO)
                || block.matchesType(YokelBlockType.AttackK)
                || block.matchesType(YokelBlockType.AttackE)
                || block.matchesType(YokelBlockType.AttackL)
                || block.matchesType(YokelBlockType.AttackEx);
    }

    public static boolean isDefenseBlock(YokelBlock block) throws GdxRuntimeException {
        return block.matchesType(YokelBlockType.DefenseY)
                || block.matchesType(YokelBlockType.DefenseO)
                || block.matchesType(YokelBlockType.DefenseK)
                || block.matchesType(YokelBlockType.DefenseE)
                || block.matchesType(YokelBlockType.DefenseL)
                || block.matchesType(YokelBlockType.DefenseEx);
    }

    public static boolean isBrokenBlock(YokelBlock block) throws GdxRuntimeException {
        return block.matchesType(YokelBlockType.BrokenY)
                || block.matchesType(YokelBlockType.BrokenO)
                || block.matchesType(YokelBlockType.BrokenK)
                || block.matchesType(YokelBlockType.BrokenE)
                || block.matchesType(YokelBlockType.BrokenL)
                || block.matchesType(YokelBlockType.BrokenEx);
    }

    public static boolean isPowerUp(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return isDefenseBlock(block) || isPowerBlock(block);
    }

    public static boolean isYBlock(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.AttackY) ||
                block.matchesType(YokelBlockType.DefenseY) ||
                block.matchesType(YokelBlockType.BrokenY) ||
                block.matchesType(YokelBlockType.NormalY);
    }

    public static boolean isOBlock(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.AttackO) ||
                block.matchesType(YokelBlockType.DefenseO) ||
                block.matchesType(YokelBlockType.BrokenO) ||
                block.matchesType(YokelBlockType.NormalO);
    }

    public static boolean isKBlock(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.AttackK) ||
                block.matchesType(YokelBlockType.DefenseK) ||
                block.matchesType(YokelBlockType.BrokenK) ||
                block.matchesType(YokelBlockType.NormalK);
    }

    public static boolean isEBlock(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.AttackE) ||
                block.matchesType(YokelBlockType.DefenseE) ||
                block.matchesType(YokelBlockType.BrokenE) ||
                block.matchesType(YokelBlockType.NormalE);
    }

    public static boolean isLBlock(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.AttackL) ||
                block.matchesType(YokelBlockType.DefenseL) ||
                block.matchesType(YokelBlockType.BrokenL) ||
                block.matchesType(YokelBlockType.NormalL);
    }

    public static boolean isExclamationBlock(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.AttackEx) ||
                block.matchesType(YokelBlockType.DefenseEx) ||
                block.matchesType(YokelBlockType.BrokenEx) ||
                block.matchesType(YokelBlockType.NormalEx);
    }

    public static boolean isClearBlock(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.Clear);
    }

    public static boolean isStone(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.Stone);
    }

    public static boolean isMedusa(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.Medusa);
    }

    public static boolean isMidas(YokelBlock block) throws GdxRuntimeException {
        if(block == null){
            return false;
        }
        return block.matchesType(YokelBlockType.Midas);
    }

    public static YokelBlockType getPowerType(YokelBlock block) throws GdxRuntimeException{
        if(isYBlock(block)){
            return YokelBlockType.AttackY;
        }
        if(isOBlock(block)){
            return YokelBlockType.AttackO;
        }
        if(isKBlock(block)){
            return YokelBlockType.AttackK;
        }
        if(isEBlock(block)){
            return YokelBlockType.AttackE;
        }
        if(isLBlock(block)){
            return YokelBlockType.AttackL;
        }
        if(isExclamationBlock(block)){
            return YokelBlockType.AttackEx;
        }
        throw new GdxRuntimeException("Cannot get Power Block Type, block is unknown");
    }

    public static YokelBlockType getDefenseType(YokelBlock block) throws GdxRuntimeException {
        if(isYBlock(block)){
            return YokelBlockType.DefenseY;
        }
        if(isOBlock(block)){
            return YokelBlockType.DefenseO;
        }
        if(isKBlock(block)){
            return YokelBlockType.DefenseK;
        }
        if(isEBlock(block)){
            return YokelBlockType.DefenseE;
        }
        if(isLBlock(block)){
            return YokelBlockType.DefenseL;
        }
        if(isExclamationBlock(block)){
            return YokelBlockType.DefenseEx;
        }
        throw new GdxRuntimeException("Cannot get Power Block Type, block is unknown");
    }

    public static YokelBlockType getNormalType(YokelBlock block) throws GdxRuntimeException {
        if(isYBlock(block)){
            return YokelBlockType.NormalY;
        }
        if(isOBlock(block)){
            return YokelBlockType.NormalO;
        }
        if(isKBlock(block)){
            return YokelBlockType.NormalK;
        }
        if(isEBlock(block)){
            return YokelBlockType.NormalE;
        }
        if(isLBlock(block)){
            return YokelBlockType.NormalL;
        }
        if(isExclamationBlock(block)){
            return YokelBlockType.NormalEx;
        }
        throw new GdxRuntimeException("Cannot get Power Block Type, block is unknown");
    }

    public static YokelBlockType getBrokenType(YokelBlock block) throws GdxRuntimeException {
        if(isYBlock(block)){
            return YokelBlockType.BrokenY;
        }
        if(isOBlock(block)){
            return YokelBlockType.BrokenO;
        }
        if(isKBlock(block)){
            return YokelBlockType.BrokenK;
        }
        if(isEBlock(block)){
            return YokelBlockType.BrokenE;
        }
        if(isLBlock(block)){
            return YokelBlockType.BrokenL;
        }
        if(isExclamationBlock(block)){
            return YokelBlockType.BrokenEx;
        }
        throw new GdxRuntimeException("Cannot get Power Block Type, block is unknown");
    }

    public static TextureRegion get2DAnimationFrame(Animation animation, int keyFrame) throws GdxRuntimeException{
        if(animation == null){
            throw new GdxRuntimeException("Animation cannot be null.");
        }
        if(keyFrame < 0){
            throw new GdxRuntimeException("keyFrame must be greater than 0.");
        }

        Object frame = animation.getKeyFrame(keyFrame);
        if(frame == null){
            return null;
        }

        if(frame instanceof TextureRegion){
            return (TextureRegion) frame;
        }
        throw new GdxRuntimeException("Frame is not an instance of " + TextureRegion.class + ". frame=" + frame.getClass());
    }

    public static void drawBackgroundRect(Batch batch, Rectangle rectangle, Color color) {
        if(batch != null && rectangle != null && color != null){
            batch.end();

            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(color);
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            shapeRenderer.end();
            shapeRenderer.dispose();

            batch.begin();
        }
    }

    public static void resetGameBlockActors(SnapshotArray<Actor> children) {
        if(children != null){
            Actor[] actors = children.begin();
            for (Actor actor : actors) {
                resetGameBlock(actor);
            }
            children.end();
        }
    }

    private static void resetGameBlock(Actor actor){
        if(actor instanceof GameBlock){
            ((GameBlock) actor).reset();
        }
    }

    public static boolean isValidPayload(String[] payload, int size){
        if(payload != null){
            return payload.length == size;
        }
        return false;
    }

    public static void invokeMethodOnMatrix(int maxWidth, int maxHeight, Object o,
                                    String methodName, Class<?>[] paramClasses, Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for(int r = 0; r < maxWidth; r++){
            for(int c = 0; c < maxHeight; c++){
                Class<?> clazz = o.getClass();
                Method m = clazz.getDeclaredMethod(methodName, paramClasses);

                if(m != null){
                    m.invoke(o, args);
                }
            }
        }
    }
}
