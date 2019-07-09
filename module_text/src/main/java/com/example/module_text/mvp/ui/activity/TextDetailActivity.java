package com.example.module_text.mvp.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.module_text.R;
import com.example.module_text.R2;
import com.example.module_text.di.component.DaggerTextDetailComponent;
import com.example.module_text.mvp.contract.TextDetailContract;
import com.example.module_text.mvp.model.entity.TextDetailWebEntity;
import com.example.module_text.mvp.model.entity.TextDetailEntity;
import com.example.module_text.mvp.presenter.TextDetailPresenter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.armscomponent.commonres.utils.AnalysisHTML;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.utils.AppUtil;
import me.jessyan.armscomponent.commonsdk.utils.Utils;
import me.jessyan.armscomponent.commonsdk.utils.setStatusBarUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 06/26/2019 22:17
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */

@Route(path = RouterHub.TEXT_DETAILACTIVITY)
public class TextDetailActivity extends BaseActivity<TextDetailPresenter> implements TextDetailContract.View, ObservableScrollViewCallbacks {

    @BindView(R2.id.favorite)
    ImageView favorite;
    @BindView(R2.id.write)
    ImageView write;
    @BindView(R2.id.share)
    ImageView share;
    @BindView(R2.id.toolBar)
    Toolbar toolBar;
    @BindView(R2.id.webView)
    WebView webView;
    @BindView(R2.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R2.id.image)
    ImageView image;
    @BindView(R2.id.news_parse_web)
    LinearLayout newsParseWeb;
    @BindView(R2.id.news_top_type)
    TextView newsTopType;
    @BindView(R2.id.news_top_date)
    TextView newsTopDate;
    @BindView(R2.id.news_top_title)
    TextView newsTopTitle;
    @BindView(R2.id.news_top_author)
    TextView newsTopAuthor;
    @BindView(R2.id.news_top_lead)
    TextView newsTopLead;
    @BindView(R2.id.news_top)
    LinearLayout newsTop;
    @BindView(R2.id.news_top_img_under_line)
    View newsTopImgUnderLine;
    @BindView(R2.id.news_top_lead_line)
    View newsTopLeadLine;

    private int mParallaxImageHeight;

    @Autowired
    String thumbnail;
    @Autowired
    String model;
    @Autowired
    String update_time;
    @Autowired
    String title;
    @Autowired
    String author;
    @Autowired
    String lead;
    @Autowired
    String id;
    private TextDetailEntity.DatasBean item;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTextDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.text_detail_activity; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        loadView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (item != null) {
            GlideArms.with(this).load(item.getThumbnail()).centerCrop().into(image);
            int mode = Integer.valueOf(item.getModel());
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            newsTopType.setText("文 字");
            newsTopDate.setText(item.getUpdate_time());
            newsTopTitle.setText(item.getTitle());
            newsTopAuthor.setText(item.getAuthor());
            newsTopLead.setText(item.getLead());
            newsTopLead.setLineSpacing(1.5f, 1.8f);
            mPresenter.getTextDetail2(item.getId());
        }
    }

    private void initData() {
        item = new TextDetailEntity.DatasBean();
        item.setThumbnail(thumbnail);
        item.setModel(model);
        item.setUpdate_time(update_time);
        item.setTitle(title);
        item.setAuthor(author);
        item.setLead(lead);
        item.setId(id);
    }

    private void loadView() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.public_colorPrimary)));
        scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.public_parallax_image_height);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        initStatus();
    }

    private void initStatus() {
        //设置全透明状态栏
        setStatusBarUtils setStatusBarUtils = new setStatusBarUtils(this);
        setStatusBarUtils.setStatusBarFullTransparent();
        int statusBarHeight = getStatusBarHeight(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolBar.getLayoutParams();
        int height = toolBar.getHeight();
        layoutParams.height = height + statusBarHeight;
        toolBar.setLayoutParams(layoutParams);
        toolBar.setPadding(0, statusBarHeight, 0, 0);
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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
        int baseColor = getResources().getColor(R.color.public_colorPrimary);
        float alpha = Math.min(1, (float) i / mParallaxImageHeight);
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
//        ViewHelper.setTranslationY(image, i / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void updateListUI(TextDetailWebEntity.DatasBean detailEntity) {
        if (detailEntity.getParseXML() == 1) {
            int i = detailEntity.getLead().trim().length();
            AnalysisHTML analysisHTML = new AnalysisHTML();
            analysisHTML.loadHtml(this, detailEntity.getContent(), analysisHTML.HTML_STRING, newsParseWeb, i);
            newsTopType.setText("文 字");
        } else {
            initWebViewSetting();
            newsParseWeb.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            newsTop.setVisibility(View.GONE);
            webView.loadUrl(addParams2WezeitUrl(detailEntity.getHtml5(), false));
        }
    }

    public String addParams2WezeitUrl(String url, boolean paramBoolean) {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append(url);
        localStringBuffer.append("?client=android");
        localStringBuffer.append("&device_id=" + AppUtil.getDeviceId(this));
        localStringBuffer.append("&version=" + "1.3.0");
        if (paramBoolean)
            localStringBuffer.append("&show_video=0");
        else {
            localStringBuffer.append("&show_video=1");
        }
        return localStringBuffer.toString();
    }

    private void initWebViewSetting() {
        WebSettings localWebSettings = this.webView.getSettings();
        localWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setSupportZoom(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        localWebSettings.setUseWideViewPort(true);
        localWebSettings.setLoadWithOverviewMode(true);
    }
}
