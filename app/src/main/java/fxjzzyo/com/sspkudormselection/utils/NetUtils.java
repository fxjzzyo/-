package fxjzzyo.com.sspkudormselection.utils;

import java.io.IOException;

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
        //1 拿到OkHttpClient 对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //2 构造Request

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
}
