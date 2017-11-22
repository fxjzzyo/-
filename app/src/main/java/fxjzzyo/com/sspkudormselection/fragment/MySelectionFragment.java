package fxjzzyo.com.sspkudormselection.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fxjzzyo.com.sspkudormselection.Constant.Global;
import fxjzzyo.com.sspkudormselection.Constant.PersonData;
import fxjzzyo.com.sspkudormselection.Constant.ResponseBean;
import fxjzzyo.com.sspkudormselection.R;
import fxjzzyo.com.sspkudormselection.utils.NetUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static fxjzzyo.com.sspkudormselection.Constant.Global.account;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MySelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MySelectionFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.drawerIcon)
    ImageView drawerIcon;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tv_stuid)
    TextView tvStuid;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_vcode)
    TextView tvVcode;
    @BindView(R.id.tv_room)
    TextView tvRoom;
    @BindView(R.id.tv_building)
    TextView tvBuilding;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private View v;

    private ActionBar actionBar;

    private String mParam1;
    private String mParam2;


    public MySelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySelectionFragment newInstance(String param1, String param2) {
        MySelectionFragment fragment = new MySelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v != null) {
            unbinder = ButterKnife.bind(this, v);
            return v;
        }
        View v = inflater.inflate(R.layout.fragment_my_selection, container, false);
        //绑定控件
        unbinder = ButterKnife.bind(this, v);
        //设置actionbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        tvTitle.setText("个人信息");
        //设置打开菜单监听
        drawerIcon.setOnClickListener(this);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        //1 拿到OkHttpClient 对象,设置免https认证
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1000, TimeUnit.SECONDS);
        builder.sslSocketFactory(NetUtils.createSSLSocketFactory());
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        OkHttpClient okHttpClient = builder.build();
        Response response = null;

        //2 构造Request
        Request request = null;
        Request.Builder requestBuilder = new Request.Builder();
        request = requestBuilder.get().url(Global.GET_DETAIL + "?stuid=" + account).build();

        //3 将Request封装为Call
        Call call = okHttpClient.newCall(request);
        //4 执行Call
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("tag", "failed");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        showProgress(false);
//                        loginProgress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("tag", "success");
                String result = response.body().string();
                Log.i("tag", "result: " + result);
                //解析数据
                final ResponseBean responseBean = JSON.parseObject(result, ResponseBean.class);

                Log.i("tag", "responseBean: " + responseBean.toString());
                if (responseBean != null) {
                    final String data = responseBean.getData();
                    final PersonData personData = JSON.parseObject(data, PersonData.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            showProgress(false);
//                            loginProgress.setVisibility(View.GONE);
                            String errcode = responseBean.getErrcode();
                            Log.i("tag", "errcode: " + errcode);
                            Log.i("tag", "data: " + personData.toString());
                            if (errcode.equals("0")) {//登录成功
                                //设置数据
                                setData(personData);
                            } else {
                                Toast.makeText(getActivity(), "请求失败！错误代码： " + errcode, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });
    }

    /**
     * 设置数据
     *
     * @param personData
     */
    private void setData(PersonData personData) {
        tvStuid.setText(personData.getStudentid());
        tvName.setText(personData.getName());
        tvVcode.setText(personData.getVcode());
        tvRoom.setText(personData.getRoom());
        tvBuilding.setText(personData.getBuilding());
        tvGrade.setText(personData.getGrade());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除绑定
        unbinder.unbind();
    }

    public interface MySelectionFragmentListener {
        public void myselectionFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.drawerIcon://只有在抽屉关闭的状态下 才能看到这个icon 故这是判断抽屉关闭的时候 点击icon时 触发怎样的事件 这个触发事件放在mainactivity中实现。
                if (getActivity() instanceof MySelectionFragmentListener) {
                    ((MySelectionFragmentListener) getActivity()).myselectionFragment();
                }
                break;

            default:
                break;
        }
    }
}
