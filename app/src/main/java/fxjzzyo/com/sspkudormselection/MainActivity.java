package fxjzzyo.com.sspkudormselection;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import fxjzzyo.com.sspkudormselection.Constant.Global;
import fxjzzyo.com.sspkudormselection.fragment.MySelectionFragment;
import fxjzzyo.com.sspkudormselection.fragment.QueryFragment;
import fxjzzyo.com.sspkudormselection.fragment.SelectFragment;
import fxjzzyo.com.sspkudormselection.utils.BackHandlerHelper;
import fxjzzyo.com.sspkudormselection.utils.SPFutils;

public class MainActivity extends FragmentActivity implements MySelectionFragment.MySelectionFragmentListener
,QueryFragment.QueryFragmentListener,SelectFragment.SelectFragmentListener{
    private DrawerLayout mDrawerLayout;
    public NavigationView mNavigationView;
    private boolean isOpen;
    private Fragment currentFragment;
    private MySelectionFragment mySelectionFragment;
    private QueryFragment queryFragment;
    private SelectFragment selectFragment;

    private long lastBackKeyDownTick = 0;
    private static final long MAX_DOUBLE_BACK_DURATION = 1500;
    public static MainActivity mainActivityInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        showDefaultFragment();
        mNavigationView.setItemIconTintList(null);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        initNavigationViewItemSelected();
        mainActivityInstance = this;

    }

    public void showDefaultFragment() {
        if (mySelectionFragment == null) {
            mySelectionFragment = MySelectionFragment.newInstance("myselection", "myselection");
        }
        addFragment(R.id.activity_main, mySelectionFragment);
        currentFragment = mySelectionFragment;
    }

    public void addFragment(int id, Fragment fragment) {
        getFragmentTransaction().add(id, fragment).commit();
    }

    public FragmentTransaction getFragmentTransaction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        return fragmentTransaction;
    }

    public void initNavigationViewItemSelected() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_my_selection:
                        if (mySelectionFragment == null) {
                            mySelectionFragment = MySelectionFragment.newInstance("", "");
                        }
                        switchFragment(currentFragment, mySelectionFragment);
                        break;
                    case R.id.navigation_query:
                        if (queryFragment == null) {
                            queryFragment = QueryFragment.newInstance("","");
                        }
                        switchFragment(currentFragment, queryFragment);
                        break;
                    case R.id.navigation_select:
                        if (selectFragment == null) {
                            selectFragment = SelectFragment.newInstance("","");
                        }
                        switchFragment(currentFragment, selectFragment);
                        break;
                    case R.id.navigation_share:
//                        ShareUtils.getInstance(MainActivity.this).share(getResources().getString(R.string.share_app_to_friends), "program");
                        break;
                   /* case R.id.navigation_about_project:
                        if (aboutProjectFragment == null) {
                            aboutProjectFragment = AboutProjectFragment.newInstance();
                        }
                        switchFragment(currentFragment, aboutProjectFragment);
                        break;
                    case R.id.navigation_about_author:
                        if (aboutAuthorFragment == null) {
                            aboutAuthorFragment = new AboutAuthorFragment();
                        }
                        switchFragment(currentFragment, aboutAuthorFragment);
                        break;*/
                    default:
                        break;

                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }
    public void switchFragment(Fragment from,Fragment to){
        if(currentFragment!=to){
            currentFragment=to;
            if(!to.isAdded()){
                getFragmentTransaction().hide(from).add(R.id.activity_main,to).commit();
            }else{
                getFragmentTransaction().hide(from).show(to).commit();
            }
        }
    }
    @Override
    public void onBackPressed() {
        long currentTick = System.currentTimeMillis();
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (isOpen) {
                mDrawerLayout.closeDrawer(mNavigationView);
                isOpen = false;
            } else {
                if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
                    Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                    lastBackKeyDownTick = currentTick;
                } else {
                    finish();
                    System.exit(0);
                }
            }
        }
    }

    /**
     * 注销登录
     * @param v
     */
    public void logOutOnClick(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        Global.account = null;
        //设置下次取消自动登录
        SPFutils.saveStringData(this,SPFutils.IS_AUTO_LOGIN,null);
        this.finish();

    }

    /**
     * 退出程序
     * @param v
     */
    public void quitOnClick(View v){
        finish();
        System.exit(0);
    }

    @Override
    public void myselectionFragment() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void queryFragment() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void selectFragment() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}
