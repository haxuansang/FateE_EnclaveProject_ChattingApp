package application.android.com.fatee.utils;

public final class Constant {
    public final static String LOGIN_SUCCESS = "Login successfully";
    public final static String EMAIl_PATTERN = "^([A-Za-z]+.[A-Za-z0-9]*)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    public final static String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#?!@$%^&*-])[A-Za-z\\d#?!@$%^&*-]{8,30}";
    public final static String USERNAME_PATTERN = "^(?=.*[a-z])(?=.*\\d)[A-Za-z\\d#?!@$%^&*-]{8,30}";
    public final static String SHARED_PREFERENCES_NAME = "temp_banned_user";
    public final static String TEMP_BANNED_USER = "user";
    public final static String LOST_INTERNET_CONNECTION = "You lost Internet Connection. Please restart your Wifi or 3G.";
    public final static String USERNAME_ERROR = "Your username must match and must be at least 8 and 30 maximum in length";
    public final static String PASSWORD_ERROR = "Your password must match and must be at least 8 and 30 maximum in length";
    public final static String RIGHT_USERNAME_WRONG_PASSWORD = "You put a wrong password or username, try again";
    public final static String DELAY_AFTER_WRONG_5_TIMES = "You put a wrong password 5 times, now the login process is delayed";
    public final static String LOCKED_USERNAME = "You can't login with this username anymore";
    public final static String EMAIL_ERROR = "Your email must be formatted right";
    public final static String COMFIRM_ERROR = "Your password and comfirmed password are not the same";
    private Constant() {

    }
}
