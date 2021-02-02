package net.asg.games.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.SnapshotArray;
import com.github.czyzby.kiwi.log.Logger;
import com.github.czyzby.kiwi.log.LoggerService;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;
import com.github.czyzby.lml.scene2d.ui.reflected.AnimatedImage;

import net.asg.games.game.objects.YokelBlockEval;
import net.asg.games.game.objects.YokelLounge;
import net.asg.games.game.objects.YokelObject;
import net.asg.games.provider.actors.GameBlock;

import org.apache.commons.lang.StringUtils;
import org.pmw.tinylog.Level;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class YokelUtilities {
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

        for (String s : o) {
            array.add(s);
        }
        return array;
    }

    public static <T> Iterable<T> safeIterable(Iterable<T> collection){
        if(collection != null){
            return (Iterable) collection;
        } else {
            return (Iterable) GdxArrays.newArray();
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

    public static Array<String> toPlainTextArray(Array<? extends YokelObject> objects) {
        Array<String> plainTexts = GdxArrays.newArray();
        for(YokelObject yokelObject : objects){
            plainTexts.add(jsonToString(yokelObject.toString()));
        }
        return plainTexts;
    }

    public static Label createLabel(Skin skin, String text, float size){
        Label label = new Label("", skin);
        if(!YokelUtilities.isEmpty(text)){
            label.setText(text);
        }
        label.setFontScale(size);
        return label;
    }

    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }

    /** @param actor might have an ID attached using name setter.
     * @return actor's ID or null. */
    public static String getActorId(Actor actor) {
        String id = "";
        if(actor != null){
            id = actor.getName();
        }
        return id;
    }

    public static <T extends Actor> T getActorFromCell(Class<T> tableClass, Cell cell) {
        if(cell != null && tableClass != null && tableClass.isInstance(cell.getActor())){
            return (T) cell.getActor();
        }
        return null;
    }

    public static float maxFloat(Float... floats) {
        float max = 0;
        if(floats != null){
            for(float f : floats){
                max = Math.max(max, f);
            }
        }
        return max;
    }

    public static GameBlock getBlock(int block){
        return UIUtil.getInstance().getGameBlock(block, false);
    }

    public static GameBlock getBlock(int block, boolean isPreview){
        return UIUtil.getInstance().getGameBlock(block, isPreview);
    }

    public static void setActorName(Actor actor, Actor actorToName) {
        if(actor != null && actorToName != null){
            actor.setName(actorToName.getName());
        }
    }

    public static void freeBlock(GameBlock uiCell) {
        UIUtil.getInstance().freeObject(uiCell);
    }

    public static void updateGameBlock(GameBlock original, int block, boolean isPreview) {
        GameBlock incoming = YokelUtilities.getBlock(block, isPreview);
        //System.out.println("original=" + original);
        //System.out.println("incoming=" + incoming);
        if(original != null && !original.equals(incoming)){
            //System.out.println("equals?=" + original.equals(incoming));

            YokelUtilities.freeBlock(original);
            original = incoming;
        } else {
            YokelUtilities.freeBlock(incoming);
        }
    }

    public static void flushIterator(Iterator<?> iter) {
        while(iter != null && iter.hasNext()){
            iter.remove();
        }
    }

    public static int getTrueBlock(int block) {
        if(YokelBlockEval.hasAddedByYahooFlag(block) || YokelBlockEval.hasBrokenFlag(block)){
            return YokelBlockEval.getCellFlag(block);
        } else {
            return YokelBlockEval.getIDFlag(YokelBlockEval.getID(block), block);
        }
    }

    public static Iterable<? extends String> iterateObjectMapKeys(ObjectMap objectMap) {
        ObjectMap.Keys<?> keys = objectMap.keys();
        return new Array.ArrayIterator(keys.toArray());
    }

    public static String[] getObjectMapKeys(ObjectMap<String, Object> arguments) {
        Array<String> keys = GdxArrays.newArray(arguments.keys());
        return keys.toArray();
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

    public static boolean isEmpty(Iterable<?> collection) {
        return collection != null && !collection.iterator().hasNext();
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

    public static TextureRegion get2DAnimationFrame(Animation<Object> animation, int keyFrame) throws GdxRuntimeException{
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

    public static boolean setDebug(boolean b, Actor... actors){
        if(actors != null){
            for(Actor actor : actors){
                if(actor != null){
                    actor.setDebug(b);
                }
            }
        }
        return b;
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

    //Logging Utils
    private static Log4LibGDXLogger validateLogger(Logger logger){
        if(logger instanceof Log4LibGDXLogger){
            return (Log4LibGDXLogger) logger;
        }
        System.out.println("logger logger: " + logger);
        System.out.println("logger logger Class: " + logger.getClass());
        System.out.println("logger factory: " + LoggerService.INSTANCE.getFactory());

        Log4LibGDXLogger.Log4LibGDXLoggerFactory logFactory = new Log4LibGDXLogger.Log4LibGDXLoggerFactory();

        //LoggerService
        LoggerService.INSTANCE.clearLoggersCache();
        LoggerService.INSTANCE.setFactory(logFactory);
        //logFactory.newLogger()
        System.out.println("logger factory2: " + LoggerService.INSTANCE.getFactory());
        //Only turn log on error
        LoggerService.disable();
        LoggerService.error(true);

        //return logFactory.newLogger();
        throw new GdxRuntimeException("Cannot use Utils on non Log4LibGDXLogger logger.");
    }

    public static void setError(Logger logger){
        validateLogger(logger).setError();
    }

    public static void setDebug(Logger logger){
        validateLogger(logger).setDebug();
    }

    public static void setInfo(Logger logger){
        validateLogger(logger).setInfo();
    }

    public static int getLoggerLevel(Logger logger){
        return validateLogger(logger).getLoggerLevel();
    }
}
