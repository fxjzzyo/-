package fxjzzyo.com.sspkudormselection.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fxjzzyo.com.sspkudormselection.Constant.Global;
import fxjzzyo.com.sspkudormselection.Constant.ResponseBean;
import fxjzzyo.com.sspkudormselection.MainActivity;
import fxjzzyo.com.sspkudormselection.R;
import fxjzzyo.com.sspkudormselection.utils.DialogUtil;
import fxjzzyo.com.sspkudormselection.utils.NetUtils;
import fxjzzyo.com.sspkudormselection.utils.SPFutils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectThreeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectThreeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
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
    @BindView(R.id.et_stduid1)
    EditText etStduid1;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.btn_post_select)
    Button btnPostSelect;
    Unbinder unbinder;
    @BindView(R.id.et_vcode1)
    EditText etVcode1;
    @BindView(R.id.et_stduid2)
    EditText etStduid2;
    @BindView(R.id.et_vcode2)
    EditText etVcode2;
    Unbinder unbinder1;
    private String mParam1;
    private String mParam2;
    private int selectBuilding;//选择的宿舍号
    private DialogUtil dialogUtil;//选择宿舍号的对话框工具类
    public SelectThreeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectThreeFragment.
     */
    public static SelectThreeFragment newInstance(String param1, String param2) {
        SelectThreeFragment fragment = new SelectThreeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_three, container, false);
        unbinder1 = ButterKnife.bind(this, view);
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

    /**
     * 下拉刷新剩余床位
     */
    @Override
    public void onRefresh() {
        queryFromNet(Global.gender);//默认查男生宿舍
    }

    /**
     * 根据选择的宿舍类别查询数据
     *
     * @param currentSelect
     */
    private void queryFromNet(int currentSelect) {
        NetUtils netUtils = NetUtils.getInstance();
        netUtils.getDataAsynFromNet(Global.GET_ROOM + "?gender=" + currentSelect, new NetUtils.MyNetCall() {
            @Override
            public void success(Call call, Response response) throws IOException {
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
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }

                        }
                    });

                }
            }

            @Override
            public void failed(Call call, IOException e) {
                Log.i("tag", "failed");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        Toast.makeText(getActivity(), "请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }

    @OnClick({R.id.ll_select_building, R.id.btn_post_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_building:
                //弹出对话框，选择楼号
                if (dialogUtil == null) {
                    dialogUtil = new DialogUtil(getActivity());
                }
                selectBuilding = dialogUtil.getSelect();
                dialogUtil.showDialog(selectBuilding,tvTargetBuilding);
                break;
            case R.id.btn_post_select:
                btnPostSelect();
                break;
        }
    }

    /**
     * 提交选择
     */
    private void btnPostSelect() {
        //确保选择了楼号
        if (tvTargetBuilding.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"请选择楼号！",Toast.LENGTH_SHORT).show();
            return;
        }
        //获取同住人信息
        String studid1 = etStduid1.getText().toString().trim();
        String vcode1 = etVcode1.getText().toString().trim();

        if (studid1.isEmpty() || vcode1.isEmpty()) {
            Toast.makeText(getActivity(),"请完善同住人信息！",Toast.LENGTH_SHORT).show();
            return;
        }
        String studid2 = etStduid2.getText().toString().trim();
        String vcode2 = etVcode2.getText().toString().trim();

        if (studid2.isEmpty() || vcode2.isEmpty()) {
            Toast.makeText(getActivity(),"请完善同住人信息！",Toast.LENGTH_SHORT).show();
            return;
        }
        //构造请求参数
        Map<String, String> reqBody = new ConcurrentSkipListMap<>();
        reqBody.put("num", "3");
        reqBody.put("stuid", Global.account);
        reqBody.put("stu1id", studid1);
        reqBody.put("v1code", vcode1);
        reqBody.put("stu2id", studid2);
        reqBody.put("v2code", vcode2);
        reqBody.put("buildingNo", selectBuilding + "");

        NetUtils netUtils = NetUtils.getInstance();
        netUtils.postDataAsynToNet(Global.SELECT_ROOM, reqBody, new NetUtils.MyNetCall() {
            @Override
            public void success(Call call, Response response) throws IOException {
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
                            if (error_code == 0) {//提交成功
                                //设置数据
                                resetEdittext();
                                Toast.makeText(getActivity(), "选择成功！", Toast.LENGTH_SHORT).show();
                                //跳转到selectSuccessfragment
                                MainActivity.mainActivityInstance.switchFragment(getParentFragment(),SelectSuccessFragment.newInstance("", ""));
                            } else {
                                Toast.makeText(getActivity(), "选择失败！错误代码： " + error_code, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
            @Override
            public void failed(Call call, IOException e) {
                Log.i("tag", "failed");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    /**
     * 重置输入框
     */
    private void resetEdittext() {
        etStduid1.setText("");
        etVcode1.setText("");
        etStduid2.setText("");
        etVcode2.setText("");
    }
}
