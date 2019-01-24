package net.asg.games.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.czyzby.kiwi.util.gdx.collection.GdxArrays;

import net.asg.games.game.objects.YokelLounge;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

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
                Array<String> ids = new Array<String>();
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

    public static <T> String[] fromCollectionToStringArray(Array<T> collection) {
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
            return new Array.ArrayIterable<T>(array);
        }
        return new Array.ArrayIterable<T>(GdxArrays.<T>newArray());
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
}
