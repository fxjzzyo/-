package fxjzzyo.com.sspkudormselection.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import fxjzzyo.com.sspkudormselection.MainActivity;
import fxjzzyo.com.sspkudormselection.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectSuccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectSuccessFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Unbinder unbinder;
    @BindView(R.id.btn_jump)
    Button btnJump;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SelectSuccessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectSuccessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectSuccessFragment newInstance(String param1, String param2) {
        SelectSuccessFragment fragment = new SelectSuccessFragment();
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
        View view = inflater.inflate(R.layout.fragment_select_success, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 跳转到my
     */
    @OnClick(R.id.btn_jump)
    public void jumpTo() {
        //切换菜单按钮
        MainActivity.mainActivityInstance.mNavigationView.setCheckedItem(R.id.navigation_my_selection);
        //跳转到myselectionfragment
        MainActivity.mainActivityInstance.switchFragment(this,MySelectionFragment.newInstance("", ""));

    }
}


