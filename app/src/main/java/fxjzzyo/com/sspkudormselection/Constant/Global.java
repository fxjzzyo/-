package fxjzzyo.com.sspkudormselection.Constant;

/**
 * Created by fxjzzyo on 2017/11/21.
 */

public class Global {


    public static String BASE_URL = "https://api.mysspku.com/index.php/V1/MobileCourse/";
    public static String LOGIN = BASE_URL + "Login";
    public static String GET_DETAIL = BASE_URL + "getDetail";
    public static String GET_ROOM = BASE_URL + "getRoom";
    public static String SELECT_ROOM = BASE_URL + "SelectRoom";

    //自己的学号
    public static String account = "";
    //自己的校验码
    public static String vcode = "";

    //自己的性别，默认男生。1：男；2：女
    public static int gender = 1;
    //标记网络是否可用
    public static boolean isNetAvailable;

}
