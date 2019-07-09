package com.example.module_video.mvp.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.module_video.R;
import com.example.module_video.di.component.DaggerVideoDetailComponent;
import com.example.module_video.mvp.model.entity.VideoDetailEntity;
import com.example.module_video.mvp.model.entity.VideoDetailWebEntity;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;

import com.example.module_video.mvp.contract.VideoDetailContract;
import com.example.module_video.mvp.presenter.VideoDetailPresenter;

import com.example.module_video.R2;


import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.jessyan.armscomponent.commonres.utils.AnalysisHTML;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.utils.AppUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/05/2019 18:54
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Route(path = RouterHub.VIDEO_DETAILACTIVITY)
public class VideoDetailActivity extends BaseActivity<VideoDetailPresenter> implements VideoDetailContract.View {

    @BindView(R2.id.favorite)
    ImageView favorite;
    @BindView(R2.id.write)
    ImageView write;
    @BindView(R2.id.share)
    ImageView share;
    @BindView(R2.id.toolBar)
    Toolbar toolBar;
    @BindView(R2.id.video)
    JCVideoPlayerStandard jcVideo;
    @BindView(R2.id.news_top_img_under_line)
    View newsTopImgUnderLine;
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
    @BindView(R2.id.news_top_lead_line)
    View newsTopLeadLine;
    @BindView(R2.id.news_top)
    LinearLayout newsTop;
    @BindView(R2.id.news_parse_web)
    LinearLayout newsParseWeb;
    @BindView(R2.id.webView)
    WebView webView;
    @BindView(R2.id.scrollView)
    ObservableScrollView scrollView;

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
    @Autowired
    String video;
    private VideoDetailEntity.DatasBean item;
    
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVideoDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.video_detail_activity; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        loadView();
        initData();
    }

    private void initData() {
        item = new VideoDetailEntity.DatasBean();
        item.setThumbnail(thumbnail);
        item.setUpdate_time(update_time);
        item.setTitle(title);
        item.setAuthor(author);
        item.setLead(lead);
        item.setVideo(video);
        item.setId(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (item != null){
            jcVideo.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;   //横向
            jcVideo.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;  //纵向
            jcVideo.setUp(item.getVideo(), JCVideoPlayer.SCREEN_LAYOUT_LIST,"");
            GlideArms.with(this).load(item.getThumbnail()).centerCrop().into(jcVideo.thumbImageView);
            newsTopType.setText("视 频");
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            newsTopDate.setText(item.getUpdate_time());
            newsTopTitle.setText(item.getTitle());
            newsTopAuthor.setText(item.getAuthor());
            newsTopLead.setText(item.getLead());
            mPresenter.getVideoDetail(item.getId());
        }
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
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.public_primary)));
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
    public void updateListUI(VideoDetailWebEntity.DatasBean detailEntity) {
        if (detailEntity.getParseXML() == 1) {
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            int i = detailEntity.getLead().trim().length();
            AnalysisHTML analysisHTML = new AnalysisHTML();
            analysisHTML.loadHtml(this, detailEntity.getContent(), analysisHTML.HTML_STRING, newsParseWeb, i);
        } else {
            initWebViewSetting();
            newsParseWeb.setVisibility(View.GONE);
            jcVideo.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            newsTop.setVisibility(View.GONE);
            webView.loadUrl(addParams2WezeitUrl(detailEntity.getHtml5(), false));
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
