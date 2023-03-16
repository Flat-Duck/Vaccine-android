package ly.smarthive.vaccine;
public class COMMON {

    public static  String BASE_URL = "http://10.0.2.2:8080/vaccine/public/api/";
    public static  String REQUESTS_URL = BASE_URL + "requests/";

    public static  String QR_URL = BASE_URL + "qr/";
    public static  String PROFILE_URL = BASE_URL + "profile/";
    public static  String DONORS_URL = BASE_URL + "donors/";
    public static  String REQUEST_DONATION_URL =  "/request";
    public static  String LOGIN_URL = BASE_URL + "login";
    public static  String ACCEPT_REQUEST_URL = "/accept";
    public static  String REFUSE_REQUEST_URL = "/refuse";
    public static  String POSTS_URL = BASE_URL + "posts";
    public static  String SWIPES_URL = BASE_URL + "swipes/";
    public static  String CENTERS_URL = BASE_URL + "centers/";
    public static  String MAIN_URL = BASE_URL + "main/";
    public static  String UPDATE_PASSWORD_URL = BASE_URL + "password/";
    public static  String FB_TOKEN;
    public static  String CURRENT_USER_ID;
    public static  String CURRENT_USER_EMAIL;
    public static  String CURRENT_USER_PASSWORD;
    public static  String CURRENT_LAT;
    public static  String CURRENT_LNG;
    public static  String USER_TOKEN ;
    // Shared pref mode
    public static final String PREF_NAME = "Save_Me_App";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_TOKEN = "accessToken";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_STATUS = "stat";
    public static final String KEY_EMAIL = "email";


}
