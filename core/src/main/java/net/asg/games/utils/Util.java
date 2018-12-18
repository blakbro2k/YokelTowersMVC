package net.asg.games.utils;

import com.badlogic.gdx.utils.GdxRuntimeException;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Util {

    public static class IDGenerator {
        private IDGenerator(){}
        public static String getID(){
            return StringUtils.replace(UUID.randomUUID() + "", "-","") ;
        }

        public static List<String> getGroupOfIDs(int num) throws IllegalArgumentException{
            if(num > 0) {
                List<String> ids = new ArrayList<String>();
                for(int i = 0; i < num; i++){
                    ids.add(getID());
                }
                return ids;
            }
            throw new IllegalArgumentException("Cannot create Group of ID's for number less than 1.");
        }
    }

    /**
     * @param numMonth
     * @return
     */
    public static String getThreeLetterMonth(int numMonth)  throws GdxRuntimeException {
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
}
