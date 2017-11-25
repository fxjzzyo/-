package fxjzzyo.com.sspkudormselection.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fxjzzyo.com.sspkudormselection.Constant.Global;
import fxjzzyo.com.sspkudormselection.Constant.ResponseBean;
import fxjzzyo.com.sspkudormselection.R;
import fxjzzyo.com.sspkudormselection.utils.NetUtils;
import fxjzzyo.com.sspkudormselection.utils.SPFutils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectOneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectOneFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_studid)
    TextView tvStudid;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_vcode)
    TextView tvVcode;
    @BindView(R.id.btn_post_select)
    Button btnPostSelect;
    Unbinder unbinder;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.tv_13)
    TextView tv13;
    @BindView(R.id.tv_14)
    TextView tv14;
    @BindView(R.id.tv_8)
    TextView tv8;
    @BindView(R.id.tv_9)
    TextView tv9;
    @BindView(R.id.tv_target_building)
    TextView tvTargetBuilding;
    @BindView(R.id.ll_select_building)
    LinearLayout llSelectBuilding;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int selectBuilding;//选择的宿舍号
    public SelectOneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectOneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectOneFragment newInstance(String param1, String param2) {
        SelectOneFragment fragment = new SelectOneFragment();
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

        View view = inflater.inflate(R.layout.fragment_select_one, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();

    }

    private void initData() {
        String name = SPFutils.getStringData(getActivity(), SPFutils.NAME, "null");
        String stuid = SPFutils.getStringData(getActivity(), SPFutils.STUDID, "null");
        String gender = SPFutils.getStringData(getActivity(), SPFutils.GENDER, "null");
        String vcode = SPFutils.getStringData(getActivity(), SPFutils.VCODE, "null");

        tvName.setText(name);
        tvStudid.setText(stuid);
        tvGender.setText(gender);
        tvVcode.setText(vcode);

        queryFromNet(Global.gender);//设置剩余床位信息，默认查男生宿舍

    }

    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_post_select)
    public void postSelect() {
        //确保选择了楼号
        if (tvTargetBuilding.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"请选择楼号！",Toast.LENGTH_SHORT).show();
            return;
        }

        //1 拿到OkHttpClient 对象,设置免https认证
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5000, TimeUnit.SECONDS);
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
        // 2.1构造RequestBody
        FormBody.Builder fbuilder = new FormBody.Builder();
        fbuilder.add("num", "1")
                .add("stuid", Global.account)
                .add("buildingNo",selectBuilding+"");
        RequestBody formBody = fbuilder.build();
        request = requestBuilder.post(formBody).url(Global.SELECT_ROOM).build();
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
                JSONObject jsonObject1 = JSON.parseObject(result);

                if (jsonObject1 != null) {
                    final int error_code = jsonObject1.getIntValue("error_code");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("tag", "errcode: " + error_code);
                            if (error_code==0) {//提交成功
                                //设置数据
                                Toast.makeText(getActivity(), "选择成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "选择失败！错误代码： " + error_code, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });
    }

    @OnClick(R.id.ll_select_building)
    public void selectBuilding() {
        //弹出对话框，选择楼号
        showSingleChoiceButton();

    }

    private String building[] = new String[]{"5号楼", "13号楼", "14号楼", "8号楼", "9号楼"};
    private DialogOnClick dialogOnClick = new DialogOnClick();//默认选中当前选中的楼号

    // 在单选选项中显示 确定和取消按钮
    //buttonOnClickg变量的数据类型是ButtonOnClick,一个单击事件类
    private void showSingleChoiceButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.dialog_style);
        builder.setTitle("请选择楼号");
        //默认选中当前选中的楼号selectBuilding
        builder.setSingleChoiceItems(building, selectBuilding, dialogOnClick);
        builder.setPositiveButton("确定", dialogOnClick);
        builder.setNegativeButton("取消", dialogOnClick);
        builder.create().show();
    }

    /**
     * 下拉刷新剩余床位
     */
    @Override
    public void onRefresh() {
        queryFromNet(Global.gender);//默认查男生宿舍
    }
    /**
     * 根据选择的宿舍类别查询数据
     * @param currentSelect
     */
    private void queryFromNet(int currentSelect) {
        //1 拿到OkHttpClient 对象,设置免https认证
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5000, TimeUnit.SECONDS);
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
        request = requestBuilder.get().url(Global.GET_ROOM + "?gender=" + currentSelect).build();

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
                        if(swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
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
                    //解析各楼号的床位数
                    final String data = responseBean.getData();
                    final JSONObject jsonObject = JSON.parseObject(data);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String errcode = responseBean.getErrcode();
                            Log.i("tag", "errcode: " + errcode);
                            if (errcode.equals("0")) {//登录成功
                                //设置数据
                                setData(jsonObject);
                                Toast.makeText(getActivity(), "数据更新完毕！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "请求失败！错误代码： " + errcode, Toast.LENGTH_SHORT).show();
                            }
                            if(swipeRefreshLayout.isRefreshing()){
                                swipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    });

                }

            }
        });
    }
    /**
     * 设置剩余床位数据
     *
     * @param jsonObject
     */
    private void setData(JSONObject jsonObject) {
        //获取各楼号的床位数
        String string5 = jsonObject.getString("5");
        String string13 = jsonObject.getString("13");
        String string14 = jsonObject.getString("14");
        String string8 = jsonObject.getString("8");
        String string9 = jsonObject.getString("9");
        //设置数据
        tv5.setText(string5);
        tv13.setText(string13);
        tv14.setText(string14);
        tv8.setText(string8);
        tv9.setText(string9);
    }
    private class DialogOnClick implements DialogInterface.OnClickListener {


        public DialogOnClick() {
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
            Log.i("tag", "which: " + which);
            if (which >= 0) {
                //如果单击的是列表项，将当前列表项的索引保存在index中。
                //如果想单击列表项后关闭对话框，可在此处调用dialog.cancel()
                //或是用dialog.dismiss()方法。
                selectBuilding = which;//当前选中的宿舍号
            } else {
                //用户单击的是【确定】按钮
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    tvTargetBuilding.setText(building[selectBuilding]);//设置选择的楼号

                }
                //用户单击的是【取消】按钮
                else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    Toast.makeText(getActivity(), "你没有选择任何东西",
                            Toast.LENGTH_LONG);
                }
            }
        }
    }
}

