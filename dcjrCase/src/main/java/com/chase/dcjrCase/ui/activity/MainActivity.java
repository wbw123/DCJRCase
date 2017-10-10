package com.chase.dcjrCase.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.DrawerListAdapter;
import com.chase.dcjrCase.adapter.MyFragmentPageAdapter;
import com.chase.dcjrCase.bean.Menu;
import com.chase.dcjrCase.ui.fragment.CaseFragment;
import com.chase.dcjrCase.ui.fragment.HomeFragment;
import com.chase.dcjrCase.ui.fragment.MeFragment;
import com.chase.dcjrCase.ui.fragment.NewsFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RadioGroup mBtnGroup;
    private FrameLayout mDrawerLeft;
    //    private FrameLayout mContentFrame;
//    private RelativeLayout mDrawerRight;
    private ListView mDrawerList;
    /*定义模拟数据*/
    private String mMenuName[] = {"首页", "Menu2", "Menu3", "Menu4", "Menu5"};
    private int mMenuPicId[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private ArrayList<Menu> mMenuList;
    private DrawerListAdapter mDrawerListAdapter;
    /*定义fragment*/
    private HomeFragment homeFragment;
    private CaseFragment caseFragment;
    private NewsFragment newsFragment;
    private MeFragment meFragment;
    /*定义viewpager+adapter*/
    private ViewPager mViewPager;
    private MyFragmentPageAdapter myAdapter;
    private ArrayList<Fragment> mFragmentLists;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化控件
        initMenu();//初始化左侧滑菜单
        initActionBar();//初始化actionbar

        /**
         * RadioGroup+Fragment+ViewPager
         */
        initViewPager();
        /**
         * RadioGroup+Fragment
         */
//        initEvent();//初始化(点击)事件
//        initHomeData();//初始化首页数据

        initMenuEvent();//侧滑菜单listview条目点击事件
    }

    private void initViewPager() {
        homeFragment = new HomeFragment();
        caseFragment = new CaseFragment();
        newsFragment = new NewsFragment();
        meFragment = new MeFragment();

        mFragmentLists = new ArrayList<>();
        mFragmentLists.add(homeFragment);
        mFragmentLists.add(caseFragment);
        mFragmentLists.add(newsFragment);
        mFragmentLists.add(meFragment);

        //获取FragmentManager对象  
        FragmentManager fragmentManager = getSupportFragmentManager();
        //获取FragmentPageAdapter对象  
        myAdapter = new MyFragmentPageAdapter(fragmentManager, mFragmentLists);
        //设置Adapter，使ViewPager 与 Adapter 进行绑定  
        mViewPager.setAdapter(myAdapter);
        //设置ViewPager默认显示第一个View  
        mViewPager.setCurrentItem(0);
        //ViewPager页面切换监听  

        /*一次性加载4页,解决来回切换数据丢失*/
        /*这个办法并不好,因为会一次性加载4个fragment,浪费用户流量,但有没找到其他办法,我也很无奈*/
        mViewPager.setOffscreenPageLimit(3);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //使RadioGroup和viewpager一起滑动
                RadioButton childAt = (RadioButton) mBtnGroup.getChildAt(position);
                childAt.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mBtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_bottom_home:
                        mViewPager.setCurrentItem(0, false);// false禁用页面切换的动画效果
                        break;
                    case R.id.rb_bottom_case:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_bottom_news:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_bottom_me:
                        mViewPager.setCurrentItem(3, false);
                        break;
                }
            }

        });

    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mContentFrame = (FrameLayout) findViewById(R.id.content_frame);
        mDrawerLeft = (FrameLayout) findViewById(R.id.drawer_left);
//        mDrawerRight = (RelativeLayout) findViewById(R.id.drawer_right);//右侧边栏
        mDrawerList = (ListView) findViewById(R.id.lv_drawer_left);
        mBtnGroup = (RadioGroup) findViewById(R.id.btn_group);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    private void initMenu() {
        initMenuData();//初始化菜单数据
        initAdapter();//初始化菜单adapter
    }

    private void initMenuData() {
        mMenuList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Menu menu = new Menu();
            menu.menuName = mMenuName[i];
            menu.menuPicId = mMenuPicId[i];
            mMenuList.add(menu);
        }
    }

    private void initAdapter() {
        if (mMenuList != null && mDrawerListAdapter == null) {
            mDrawerListAdapter = new DrawerListAdapter(mMenuList, this);
            mDrawerList.setAdapter(mDrawerListAdapter);
        } else {
            mDrawerListAdapter.notifyDataSetChanged();
        }
    }

    private void initMenuEvent() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //跳到 首页fragment
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                }
                //每次点击后都要关闭 侧滑菜单drawer_left
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }

    private void initActionBar() {

        /*得到actionbar*/
        ActionBar actionBar = getSupportActionBar();
        /*去掉actionbar底部的阴影线*/
        actionBar.setElevation(0);
        /*设置标题*/
        actionBar.setTitle(R.string.title);
        /*设置ActionBar可见，并且切换菜单和内容视图*/
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        /*一定要使用同步*/
        mActionBarDrawerToggle.syncState();

        /*菜单布局设置监听*/
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActionBarDrawerToggle.onOptionsItemSelected(item);
                break;
//            case R.id.search:
//                System.out.println("搜索~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//                startActivity(new Intent(OsChinaApp.context, FindActivity.class));
//                break;
        }
        return super.onOptionsItemSelected(item);
    }











/*RadioGroup+Fragment*/
/*--------------------------------------------------------------------------------------------------------*/
    /*private void initEvent() {
        mBtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                switch (checkedId) {
                    case R.id.rb_bottom_home:
                        if (homeFragment == null) {
                            homeFragment = new HomeFragment();
                        }
                        ft.replace(R.id.content_frame, homeFragment);
                        break;
                    case R.id.rb_bottom_heihei:
                        if (caseFragment == null) {
                            caseFragment = new CaseFragment();
                        }
                        ft.replace(R.id.content_frame, caseFragment);
                        break;
                    case R.id.rb_bottom_xixi:
                        if (newsFragment == null) {
                            newsFragment = new NewsFragment();
                        }
                        ft.replace(R.id.content_frame, newsFragment);
                        break;
                    case R.id.rb_bottom_me:
                        if (meFragment == null) {
                            meFragment = new MeFragment();
                        }
                        ft.replace(R.id.content_frame, meFragment);
                        break;
                }

                ft.commit();
            }

        });

    }

    private void initHomeData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        ft.replace(R.id.content_frame, homeFragment);
        ft.commit();
    }*/
}
