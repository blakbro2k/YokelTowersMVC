package net.asg.games.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.SnapshotArray;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.AbstractYokelObject;
import net.asg.games.game.objects.YokelBlock;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelObject;
import net.asg.games.provider.actors.GameBlock;

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
    private final static String LEFT_CURLY_BRACET_HTML = "&#123;";
    private final static String RIGHTT_CURLY_BRACET_HTML = "&#125;";
    private final static Json json = new Json();

    public static void setSizeFromDrawable(Actor actor, Drawable drawable){
        if(actor != null && drawable != null){
            actor.setWidth(drawable.getMinWidth());
            actor.setHeight(drawable.getMinHeight());
        }
    }

    public static Array<String> arrayToList(String[] o) {
        int size = o.length;
        Array<String> array = new Array<>(size);

        for(int i = 0; i < size; i++){
            array.add(o[i]);
        }
        return array;
    }

    public static <T> Array.ArrayIterator<T> safeIterable(Array<T> collection){
        if(collection != null){
            return new Array.ArrayIterator<>(collection);
        } else {
            return new Array.ArrayIterator<>(GdxArrays.newArray());
        }
    }

    public static String printYokelObject(YokelObject yokelObject) {
        return json.prettyPrint(yokelObject);
    }

    public static String printYokelObjects(Array<? extends YokelObject> yokelObjects){
        StringBuilder sb = new StringBuilder();
        for(YokelObject yObject : yokelObjects){
            sb.append(printYokelObject(yObject)).append('\n');
        }
        return sb.toString();
    }

    public static Array<String> toPlainTextArray(Array<? extends AbstractYokelObject> objects) {
        Array<String> plainTexts = GdxArrays.newArray();
        for(AbstractYokelObject yokelObject : objects){
            plainTexts.add(jsonToString(yokelObject.toString()));
        }
        return plainTexts;
    }

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
            for(Object o : c2){
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
        return collection != null && collection.isEmpty();
    }

    public static <T> boolean isQueueEmpty(Queue<T> collection){
        return collection != null && collection.isEmpty();
    }

    public static boolean isStaticArrayEmpty(Object[] array){
        return array != null && array.length < 1;
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
        return new String[1];
    }

    public static <T> String[] toStringArray(ObjectMap.Values<T> values) {
        Array<T> array = new Array<>();
        if(values != null){
            while(values.hasNext){
                array.add(values.next());
            }
        }
        return toStringArray(array);
    }

    /**
     * Creates a new Iterable Array object.
     * @param //array
     * @param //<T>
     * @return
     */
    //@NotNull
   /* public static <T> Array.ArrayIterable<T> toIterable(Array<T> array){
        if(isArrayEmpty(array)){
            return new Array.ArrayIterable<>(array);
        }
        return new Array.ArrayIterable<>(GdxArrays.newArray());
    }*/

    public static String getJsonString(Object o){
        return json.toJson(o);
    }

    public static <T> T getObjectFromJsonString(Class<T> type, String jsonStr){
        return json.fromJson(type, jsonStr);
    }

    public static String jsonToString(String str){
        return StringUtils.replace(StringUtils.replace(str, "{",LEFT_CURLY_BRACET_HTML),"}", RIGHTT_CURLY_BRACET_HTML);
    }

    public static String stringToJson(String str){
        return StringUtils.replace(StringUtils.replace(str, LEFT_CURLY_BRACET_HTML,"{"), RIGHTT_CURLY_BRACET_HTML,"}");
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

    /*public static<T> Array<T> getValuesArray(ObjectMap.Values<T> values) {
        if(values != null){
            return values.toArray();
        }
        return GdxArrays.newArray();
    }*/

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
            return Boolean.parseBoolean(otos(o));
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
            return Integer.parseInt(otos(o));
        }
        return -1;
    }

    public static long otol(Object o){
        if(o != null){
            return Long.parseLong(otos(o));
        }
        return -1;
    }

    public static float otof(Object o){
        if(o != null){
            return Float.parseFloat(otos(o));
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

    public static String printBounds(Actor actor){
        if(actor != null){
            String name = actor.getClass().getSimpleName() + "@" + Integer.toHexString(actor.hashCode());
            String cname = actor.getName();
            if(cname != null){
                name = cname;
            }
            return name + "(" + actor.getX() + "," + actor.getY() + ")[w:" + actor.getWidth() + " h:" + actor.getHeight() + "]";
        } else {
            return "";
        }
    }

    public static void setDebug(boolean b, Actor... actors){
        if(actors != null){
            for(Actor actor : actors){
                if(actor != null){
                    actor.setDebug(b);
                }
            }
        }
    }


    public static Array<Drawable> getAniImageFrames(AnimatedImage image){
        Array<Drawable> drawables = new Array<>();
        if(image != null){
            for(Drawable frame : image.getFrames()){
                if(frame != null){
                    drawables.add(frame);
                }
            }
        }
        return drawables;
    }
}
