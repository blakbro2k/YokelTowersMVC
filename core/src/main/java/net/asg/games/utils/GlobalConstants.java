package net.asg.games.utils;

/**
 * Created by Blakbro2k on 12/29/2017.
 */

public class GlobalConstants {
    public static final String USER_AGENT = "LMozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";

    //prevent instantation
    private GlobalConstants(){}

    public static final int MAX_WIDTH = 1024;
    public static final int MAX_HEIGHT = 1024;
    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 600;
    public static final int HTTP_REQUEST_TIMEOUT = 2500;
    public static final int ERROR_MSG_TIMEOUT = 4000;

    public static final int VIEWPORT_WIDTH = APP_WIDTH;
    public static final int VIEWPORT_HEIGHT = APP_HEIGHT;



    public static final String DEFAULT_FONT_PATH = "default.fnt";
    public static final String DEFAULT_FONT_IMAGE_PATH = "default.png";
    public static final String DEFAULT_UISKIN_ATLAS = "uiskin.atlas";
    public static final String DEFAULT_UISKIN_JSON = "uiskin.json";
    public static final String DEFAULT_ATLAS_PATH = "game.atlas";
    public static final String DEFAULT_DOWNLOAD_FOLDER = "yokeltowers";
    public static final String SHADE_UISKIN_ATLAS = "shade/uiskin.atlas";
    public static final String SHADE_UISKIN_JSON = "shade/uiskin.json";
    public static final String SHADE_ATLAS_PATH = "shade/imageAssets.atlas";

    public static final String GAME_TITLE = "RodKast Alpha";
    public static final String SOURCE_ASSETS_FOLDER_PATH = "raw/game";
    public static final String TARGET_ASSETS_FOLDER_PATH = "assets/ui/game";
    public static final String IMAGES_FOLDER_NAME = "images";
    public static final String GAME_ATLAS_NAME = "game";

    public static final String BAD_LOGIC_IMAGE_PATH = "badlogic.jpg";
    public static final String BANNER_IMAGE = "banner";
    public static final String GAME_IMAGE_PATH = GAME_ATLAS_NAME + ".png";

    public static final String PREFERENCES_NAME = "yokel_prefs";
    public static final String DEFAULT_HOST = "localhost";

    public static final int PING_SERVER = 90;
    public static final int GET_ROOMS = 91;
    public static final int GET_TABLES = 92;
    public static final int GET_SEATS = 93;
    public static final int REGISTER_PLAYER = 94;
}
