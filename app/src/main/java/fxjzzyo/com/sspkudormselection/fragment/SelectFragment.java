package fxjzzyo.com.sspkudormselection.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fxjzzyo.com.sspkudormselection.R;
import fxjzzyo.com.sspkudormselection.adapter.MyFragmentPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.drawerIcon)
    ImageView drawerIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    Unbinder unbinder;
  /*  @BindView(R.id.btn_post_select)
    Button btnPostSelect;*/

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ActionBar actionBar;
    //ToolBar三个按钮对应的Fragment
    private List<Fragment> fragmentlist = new ArrayList<>(3);
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    public SelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectFragment newInstance(String param1, String param2) {
        SelectFragment fragment = new SelectFragment();
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
        View v = inflater.inflate(R.layout.fragment_select, container, false);

        unbinder = ButterKnife.bind(this, v);

        return v;
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
        //添加选宿舍的四个fragment
        addFragment();
        //初始化myFragmentPagerAdapter
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),fragmentlist);
    }

    /**
     * 添加选宿舍的四个fragment
     */
    private void addFragment() {
        fragmentlist.add(SelectOneFragment.newInstance("",""));
        fragmentlist.add(SelectTwoFragment.newInstance("",""));
        fragmentlist.add(SelectThreeFragment.newInstance("",""));
        fragmentlist.add(SelectFourFragment.newInstance("",""));
    }

    private void initEvent() {
        //设置actionbar
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolBar);
//        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        tvTitle.setText("选择宿舍");
        drawerIcon.setOnClickListener(this);
        //为viewpager设置适配器
        viewPager.setAdapter(myFragmentPagerAdapter);
        //设置viewpager缓存的页面个数为3
        viewPager.setOffscreenPageLimit(3);
        //将 tabLayout与 viewPager关联
        tabLayout.setupWithViewPager(viewPager);


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    @OnClick(R.id.fab)
    /*public void addSelect() {
        //添加办理人
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("添加办理人");

        final View view = getActivity().getLayoutInflater().inflate(R.layout.list_item_layout, null);

        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView tvStuid = (TextView) view.findViewById(R.id.tv_stuid);
                TextView tvCode = (TextView) view.findViewById(R.id.tv_vcode);
                String stuid = tvStuid.getText().toString().trim();
                String code = tvCode.getText().toString().trim();
                if (stuid.isEmpty()) {
                    Snackbar.make(getView(), "请输入学号！", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if (code.isEmpty()) {
                    Snackbar.make(getView(), "请输入校验码！", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                //构建新的办理人
                SelectPerson selectPerson = new SelectPerson();
                selectPerson.setStudId(stuid);
                selectPerson.setVcode(code);
                //将数据添加到列表
                datas.add(selectPerson);
                //通知更新列表
                myAdapter.notifyDataSetChanged();


            }
        });
        builder.setNegativeButton("取消", null);

        builder.create().show();


        Snackbar.make(getView(), "添加办理人", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }*/

    /**
     * 提交选择
     */
    /*@OnClick(R.id.btn_post_select)
    public void postSelect() {
        //同住总人数
        int num = datas.size();

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
        fbuilder.add("num", num + "")
                .add("stuid", Global.account);

        if (num == 0) {//单人办理

        }

        for (int i = 0; i < datas.size(); i++) {
            SelectPerson selectPerson = datas.get(i);
            fbuilder.add("stu1id", selectPerson.getStudId());
            fbuilder.add("v1code", selectPerson.getVcode());
        }
        RequestBody formBody = fbuilder.build();

//        request = requestBuilder.post().url(Global.GET_ROOM + "?gender=" + currentSelect).build();

        //3 将Request封装为Call
        Call call = okHttpClient.newCall(request);
    }*/

    public interface SelectFragmentListener {
        public void selectFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.drawerIcon://只有在抽屉关闭的状态下 才能看到这个icon 故这是判断抽屉关闭的时候 点击icon时 触发怎样的事件 这个触发事件放在mainactivity中实现。
                if (getActivity() instanceof SelectFragmentListener) {
                    ((SelectFragmentListener) getActivity()).selectFragment();
                }
                break;

            default:
                break;
        }
    }

}
