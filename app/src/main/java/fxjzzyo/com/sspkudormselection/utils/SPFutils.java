package fxjzzyo.com.sspkudormselection.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fxjzzyo on 2017/11/8.
 */

public class SPFutils {
    public static String PERSON_INFO = "person";

    public static String STUDID = "studid";
    public static String NAME = "name";
    public static String GENDER = "gender";
    public static String VCODE = "vcode";
    public static String ROOM = "room";
    public static String BUILDING = "building";
    public static String LOCATION = "location";
    public static String GRADE = "grade";

    public static final String SPF_USER = "user";
    public static final String IS_REMEMBER = "remember";
    public static final String KEY_PASSWORD = "password";
    public static final String IS_AUTO_LOGIN = "autoLogin";

    public static String getStringData(Context context,String key,String defaultValue) {
        SharedPreferences spf = context.getSharedPreferences(PERSON_INFO, MODE_PRIVATE);
        String value = spf.getString(key,defaultValue);
        return value;
    }

    public static void saveStringData(Context context,String key,String value) {
        SharedPreferences spf = context.getSharedPreferences(PERSON_INFO, MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(key, value);
        edit.commit();
    }
}
