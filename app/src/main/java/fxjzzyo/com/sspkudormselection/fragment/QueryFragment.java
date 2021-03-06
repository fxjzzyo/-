package fxjzzyo.com.sspkudormselection.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fxjzzyo.com.sspkudormselection.Constant.Global;
import fxjzzyo.com.sspkudormselection.Constant.ResponseBean;
import fxjzzyo.com.sspkudormselection.R;
import fxjzzyo.com.sspkudormselection.utils.NetUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.drawerIcon)
    ImageView drawerIcon;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
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
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ActionBar actionBar;

    private String mParam1;
    private String mParam2;
    private ArrayAdapter<String> adapter;
    private int currentSelect;//当前选择的宿舍类别，1：男；2：女

    public QueryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueryFragment.
     */
    public static QueryFragment newInstance(String param1, String param2) {
        QueryFragment fragment = new QueryFragment();
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
        View v = inflater.inflate(R.layout.fragment_query, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    private void initEvent() {
        //设置actionbar
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);
//        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        tvTitle.setText("查询剩余床位数");
        drawerIcon.setOnClickListener(this);
        //实例化适配器
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        //向适配器中添加数据
        adapter.add("男");
        adapter.add("女");
        //为spinner设置适配器
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {//男生
                    currentSelect = 1;
                } else {//女生
                    currentSelect = 2;
                }

                //根据选择，查询数据
                queryFromNet(currentSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.green, R.color.blue);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        initData();
        //初始化事件
        initEvent();

    }


    /**
     * 初始化数据
     */
    private void initData() {
        //默认选择男生宿舍
        currentSelect = Global.gender;
        //从网络获取数据
        queryFromNet(currentSelect);

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
     * 设置数据
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
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        //从网络获取数据
        queryFromNet(currentSelect);
    }

    public interface QueryFragmentListener {
        public void queryFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.drawerIcon://只有在抽屉关闭的状态下 才能看到这个icon 故这是判断抽屉关闭的时候 点击icon时 触发怎样的事件 这个触发事件放在mainactivity中实现。
                if (getActivity() instanceof QueryFragmentListener) {
                    ((QueryFragmentListener) getActivity()).queryFragment();
                }
                break;

            default:
                break;
        }
    }
}
