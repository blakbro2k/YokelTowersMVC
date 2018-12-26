package net.asg.games.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

public class Util {

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
            Array.ArrayIterable<Object> c3 = new Array.ArrayIterable<Object>(c2);
            for(Object o : c3){
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

    public static <T> boolean isCollectionEmpty(Array<T> collection){
        return collection == null || collection.size == 0;
    }

    public static boolean isArrayEmpty(Object[] array){
        return array == null || array.length < 1;
    }

    public static <T> String[] fromCollectionToArray(Array<T> playerNames) {
        if(playerNames != null){
            int size = playerNames.size;
            String[] c2 = new String[size];
            Array.ArrayIterable<T> c3 = toIterable(playerNames);
            int c = 0;
            for(Object o : c3){
                if(o != null){
                    c2[c] = o.toString();
                    c++;
                }
            }
            return c2;
        }
        return null;
    }

    public static <T> Array.ArrayIterable<T> toIterable(Array<T> array){
        if(!isCollectionEmpty(array)){
            return new Array.ArrayIterable<T>(array);
        }
        return new Array.ArrayIterable<T>(new Array<T>());
    }
}
