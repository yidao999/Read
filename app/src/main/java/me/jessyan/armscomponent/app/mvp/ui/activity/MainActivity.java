/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.armscomponent.app.mvp.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.app.R;
import me.jessyan.armscomponent.app.di.component.DaggerMainComponent;
import me.jessyan.armscomponent.app.mvp.contract.MainContract;
import me.jessyan.armscomponent.app.mvp.model.entity.Event;
import me.jessyan.armscomponent.app.mvp.model.entity.MainEntity;
import me.jessyan.armscomponent.app.mvp.presenter.MainPresenter;
import me.jessyan.armscomponent.app.mvp.ui.Fragment.LeftMenuFragment;
import me.jessyan.armscomponent.app.mvp.ui.Fragment.RightMenuFragment;
import me.jessyan.armscomponent.app.mvp.ui.adapter.VerticalPagerAdapter;
import me.jessyan.armscomponent.app.mvp.ui.widget.VerticalViewPager;
import me.jessyan.armscomponent.commonsdk.core.EventBusHub;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.utils.AppUtil;
import me.jessyan.armscomponent.commonsdk.utils.setStatusBarUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * ================================================
 * 宿主 App 的主页
 *
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">ArmsComponent wiki 官方文档</a>
 * Created by JessYan on 19/04/2018 16:10
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@Route(path = RouterHub.APP_MAINACTIVITY)
public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.view_pager)
    VerticalViewPager viewPager;
    @BindView(R.id.title_bar)
    RelativeLayout main_relativeLayout;
    @BindView(R.id.topImage)
    ImageView topImage;
    @BindView(R.id.progress_load)
    FrameLayout progress_load;

    private long mPressedTime;
    private SlidingMenu slidingMenu;
    private LeftMenuFragment leftMenu;
    private RightMenuFragment rightMenu;
    private String deviceId;
    private VerticalPagerAdapter pagerAdapter;
    private int page = 1;
    private boolean isLoading = true;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        initStatus();
        initMenu();
        initPage();
        deviceId = AppUtil.getDeviceId(this);
        //        String getLunar= PreferenceUtils.getPrefString(this,"getLunar",null);
//        if (!TimeUtil.getDate("yyyyMMdd").equals(getLunar)){
//            loadRecommend();
//        }
        loadData(0, 0, "0", "0");
    }

    private void initMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slidingMenu.setMenu(R.layout.left_menu);
        leftMenu = new LeftMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.left_menu, leftMenu).commit();
        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        rightMenu = new RightMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.right_menu, rightMenu).commit();
    }

    @Subscriber(tag = EventBusHub.APPSHOWSLIDING)
    private void showMenuContent(Event event) {
        slidingMenu.showContent();
    }


    private void initStatus() {
        //设置全透明状态栏
        setStatusBarUtils setStatusBarUtils = new setStatusBarUtils(this);
        setStatusBarUtils.setStatusBarFullTransparent();

        //动态设置状态栏marginTop
        int statusBarHeight = getStatusBarHeight(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) main_relativeLayout.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
        main_relativeLayout.setLayoutParams(layoutParams);
        //动态设置状态栏Image
        ViewGroup.LayoutParams layoutParams1 = topImage.getLayoutParams();
        layoutParams1.height = statusBarHeight;
        topImage.setLayoutParams(layoutParams1);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    private void loadRecommend() {
        // TODO: 2019/6/27
    }

    private void initPage() {
        pagerAdapter = new VerticalPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (pagerAdapter.getCount() <= i + 2 && !isLoading) {
                    if (isLoading) {
                        Toast.makeText(MainActivity.this, "正在努力加载...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loadData(page, 0, pagerAdapter.getLastItemId(), pagerAdapter.getLastItemCreateTime());
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void loadData(int page, int mode, String pageId, String createTime) {
        isLoading = true;
        mPresenter.getTextDetail(page, mode, pageId, deviceId, createTime);
    }

    @Override
    public void onBackPressed() {
        //获取第一次按键时间
        long mNowTime = System.currentTimeMillis();
        //比较两次按键时间差
        if ((mNowTime - mPressedTime) > 2000) {
            ArmsUtils.makeText(getApplicationContext(),
                    "再按一次退出" + ArmsUtils.getString(getApplicationContext(), R.string.public_app_name));
            mPressedTime = mNowTime;
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 这里注意下在组件的页面中(使用了 R2 的页面)使用 {@link butterknife.OnClick} 会有概率出现 id 不正确的问题, 使用以下方式解决
     * <pre>
     * @OnClick({R2.id.button1, R2.id.button2})
     * public void Onclick(View view){
     *      if (view.getId() == R.id.button1){
     *          ...
     *      } else if(view.getId() == R.id.button2){
     *          ...
     *      }
     * }
     * </pre>
     * <p>
     * 在注解上使用 R2, 下面使用 R, 并且使用 {@code if else}, 替代 {@code switch}
     *
     * @param view
     */
    @OnClick({R.id.left_slide, R.id.right_slide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_slide:
                slidingMenu.showMenu();
                leftMenu.startAnim();
                break;
            case R.id.right_slide:
                slidingMenu.showSecondaryMenu();
                rightMenu.startAnim();
                break;
        }
    }

    @Override
    public void showLoading() {
        progress_load.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progress_load.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void launchActivity(@NonNull Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public void updateListUI(List<MainEntity.DatasBean> itemList) {
        isLoading = false;
        pagerAdapter.setDataList(itemList);
        page++;
    }

    @Override
    public void showNoMore() {
        Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(slidingMenu.isMenuShowing()){
                slidingMenu.showContent();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
