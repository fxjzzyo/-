package fxjzzyo.com.sspkudormselection.Constant;

import android.app.Application;
import android.util.Log;

import fxjzzyo.com.sspkudormselection.utils.NetUtils;

/**
 * Created by fxjzzyo on 2017/11/29.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //判断网络是否可用
        Global.isNetAvailable =  NetUtils.isNetworkAvailable(this);
        Log.i("tag", "net status: " + Global.isNetAvailable);
    }
}
