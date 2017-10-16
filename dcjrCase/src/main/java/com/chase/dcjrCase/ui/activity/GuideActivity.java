package com.chase.dcjrCase.ui.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.uitl.PrefUtils;

import java.util.ArrayList;


/**
 * 新手引导
 * Created by zcm on 2017/9/22.
 */

public class GuideActivity extends Activity implements View.OnClickListener{


    private ViewPager mviewPager;
    private  int[] mImageIds=new  int[]{R.drawable.guide_one,R.drawable.guide_two,R.drawable.guide_three};

    private ArrayList<ImageView>mImageViewList;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;//小红点
    private int mPointWidth;//两个圆点的宽度
    private Button btnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        boolean is_guide_show = PrefUtils.getBoolean("is_guide_show", false, this);
        if (is_guide_show){
            startActivity(new Intent(this,SplashActivity.class));
            finish();
        }

        mviewPager = findViewById(R.id.vp_pager);
        llContainer = findViewById(R.id.ll_container);
        ivRedPoint = findViewById(R.id.iv_red_point);
        btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);

       // startActivity(new Intent(this, LoginActivity.class));



        //初始化ImageView
        mImageViewList=new ArrayList<ImageView>();
        for(int i=0;i<mImageIds.length;i++){
            ImageView imageView=new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(imageView);

            //初始化圆点---圆点的个数和图片的个数一样，就在for循环里写圆点的代码
            ImageView pointView=new ImageView(this);
            pointView.setImageResource(R.drawable.shape_circle_defalut);
            //给圆点加边距，从第二个点开始
            //初始化圆点布局参数
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i>0){
                params.leftMargin=10;//从第二个点开始设置边距
            }
            pointView.setLayoutParams(params);
            llContainer.addView(pointView);
        }
        mviewPager.setAdapter(new MyAdapter());
        //页面绘制结束之后，计算两个圆点的间距
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override//layout方法执行结束（位置确定）
            public void onGlobalLayout() {
                //移除监听--因为只要监听一次就好了
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //获取两个圆点的间距
                mPointWidth = llContainer.getChildAt(1).getLeft()-llContainer.getChildAt(0).getLeft();
                System.out.println("Width"+mPointWidth);
            }
        });


        mviewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           //页面滑动过程的回调
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //   System.out.println("当前位置"+position+","+"偏移的百分比"+positionOffset+"偏移的像素"+positionOffsetPixels);
                   //计算当前小红点的左边距
                  int leftMargin= (int) (mPointWidth*positionOffset+position*mPointWidth);
                   //拿到布局参数
                   RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
                    //修改小红点的左边距
                   params.leftMargin=leftMargin;
                   ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                    if (position==mImageIds.length-1){
                        //最后页面显示开始体验
                        btnStart.setVisibility(View.VISIBLE);
                    }else{
                        //其他页面隐藏开始体验
                        btnStart.setVisibility(View.GONE);
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start:
                //开始体验
                //记录新手引导已经被展示的状态，下次启动不再展示
                PrefUtils.putBoolean("is_guide_show",true,this);
                startActivity(new Intent(this,SplashActivity.class));
                System.out.println("已展示过，不再展示");
                finish();
                break;
            default:
                break;
        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view==object;
        }
         //初始化条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
           /* ImageView imageView=new ImageView(getApplicationContext());
            imageView.setBackgroundResource(mImageIds[position]);*/
           //每次从集合中取出所用视图，不用每次新建了
            ImageView imageView=mImageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
