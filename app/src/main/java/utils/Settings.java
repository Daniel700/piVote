package utils;

/**
 * Created by Daniel on 26.08.2015.
 */
public abstract class Settings {

    public static final int REFRESH_NUMBER = 2;
    //14400000 ms == 4 hours
    public static final long DURATION = 14400000;

    public static final int POLL_CREATION_LIMIT = 2;
    public static final int FAVORITE_LIST_MAXIMUM = 25;

    //PRODUCTION ENVIRONMENT !!!! LIVE SERVER ADDRESS !!!!
    //public static final String PROJECT_ADDRESS = "https://pivote-1065.appspot.com/_ah/api/";

    //TEST ENVIRONMENT
    public static final String PROJECT_ADDRESS = "https://pivote-test.appspot.com/_ah/api/";

    public static final boolean AD_MOB_TEST_ENVIRONMENT = true;
}
