package fxjzzyo.com.sspkudormselection.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by fxjzzyo on 2017/11/24.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentlist;
    private String[] titles = new String[]{"单人办理","双人办理","三人办理","四人办理"};
    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragmentlist) {
        super(fm);
        this.fragmentlist = fragmentlist;


    }

    @Override
    public Fragment getItem(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return fragmentlist.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
