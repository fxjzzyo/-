package fxjzzyo.com.sspkudormselection;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import fxjzzyo.com.sspkudormselection.Constant.Global;
import fxjzzyo.com.sspkudormselection.Constant.ResponseBean;
import fxjzzyo.com.sspkudormselection.utils.TrustAllCerts;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {
private static final int SUCCESS = 1;
private static final int FAILED = 0;

    private TextInputEditText etAccount,etPassword;
    private Button btnLogin;
    private ProgressBar loginProgress;
    private View mLoginFormView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initViews();
        initDatas();
        initEvent();
    }

    private void initViews() {
        etAccount = (TextInputEditText) findViewById(R.id.et_accout);
        etPassword = (TextInputEditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.sign_in_button);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
    }

    private void initDatas() {
//        loginProgress = new ProgressBar(this);

    }

    private void initEvent() {

    }
   /* public void Login(View view) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }*/
    /**
     * 点击登录按钮
     * @param view
     */
    public void Login(View view) {

        final String account = etAccount.getText().toString();
        final String pass= etPassword.getText().toString();
        if (account.isEmpty()||pass.isEmpty()) {
            Toast.makeText(this,"用户名或密码不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
//showProgress(true);
        loginProgress.setVisibility(View.VISIBLE);


//1 拿到OkHttpClient 对象,设置免https认证
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1000, TimeUnit.SECONDS);
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        OkHttpClient okHttpClient = builder.build();
        Response response = null;

        //2 构造Request
        Request request=null;
        Request.Builder requestBuilder = new Request.Builder();
        request = requestBuilder.get().url(Global.LOGIN+"?username="+account+"&password="+pass).build();

        //3 将Request封装为Call
        Call call = okHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("tag", "failed");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        showProgress(false);
                        loginProgress.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("tag", "success");
                String result = response.body().string();
                final ResponseBean responseBean = JSON.parseObject(result, ResponseBean.class);

                if (responseBean != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            showProgress(false);
                            loginProgress.setVisibility(View.GONE);
                            String errcode = responseBean.getErrcode();
                            if (errcode.equals("0")) {//登录成功
                                //记录学号
                                Global.account = account;

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "请求失败！错误代码： " + errcode, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });

    }
    private static SSLSocketFactory createSSLSocketFactory() {
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
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
