package fxjzzyo.com.sspkudormselection.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fxjzzyo on 2017/7/12.
 */

public class NetUtils {

    public static Response getDatasFromNet(String url_root, String methord, String keyWord) {
        Response response = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        //2 构造Request

        //1 拿到OkHttpClient 对象
        Request.Builder builder = new Request.Builder();
        Request request=null;
        if(methord.equals("SuggestWord"))
        {
             request = builder.get().url(url_root+methord+"?wordKey="+keyWord).build();

        }else {
            //http://fy.webxml.com.cn/sound/1033
//            L.e("路径："+url_root+keyWord);
             request = builder.get().url(url_root+keyWord).build();

        }

        //3 将Request封装为Call
        Call call = okHttpClient.newCall(request);
        //4 执行Call
        try {
             response = call.execute();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void getDataFromNet(Context context, String url) {

//1
    }

    public static void postDataToNet(Context context, String url) {

    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     * @return
     */
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
