package com.example.module_audio.mvp.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.module_audio.R;
import com.example.module_audio.app.utils.TimeUtils;
import com.example.module_audio.di.component.DaggerAudioDetailComponent;
import com.example.module_audio.mvp.model.entity.AudioDetailEntity;
import com.example.module_audio.mvp.model.entity.AudioDetailWebEntity;
import com.example.module_audio.player.IPlayback;
import com.example.module_audio.player.PlayState;
import com.example.module_audio.player.PlaybackService;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;

import com.example.module_audio.mvp.contract.AudioDetailContract;
import com.example.module_audio.mvp.presenter.AudioDetailPresenter;

import com.example.module_audio.R2;


import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.armscomponent.commonres.utils.AnalysisHTML;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.utils.AppUtil;
import me.jessyan.armscomponent.commonsdk.utils.setStatusBarUtils;
import me.jessyan.autosize.utils.LogUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/04/2019 11:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */

@Route(path = RouterHub.AUDIO_DETAILACTIVITY)
public class AudioDetailActivity extends BaseActivity<AudioDetailPresenter> implements AudioDetailContract.View, ObservableScrollViewCallbacks, IPlayback.Callback {

    @BindView(R2.id.image)
    ImageView image;
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
    @BindView(R2.id.favorite)
    ImageView favorite;
    @BindView(R2.id.write)
    ImageView write;
    @BindView(R2.id.share)
    ImageView share;
    @BindView(R2.id.toolBar)
    Toolbar toolBar;

    @BindView(R2.id.button_play_last)
    AppCompatImageView buttonPlayLast;
    @BindView(R2.id.button_play_toggle)
    AppCompatImageView buttonPlayToggle;
    @BindView(R2.id.button_play_next)
    AppCompatImageView buttonPlayNext;
    @BindView(R2.id.text_view_progress)
    TextView textViewProgress;
    @BindView(R2.id.seek_bar)
    AppCompatSeekBar seekBar;
    @BindView(R2.id.text_view_duration)
    TextView textViewDuration;
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
    private AudioDetailEntity.DatasBean item;

    private PlaybackService mPlaybackService;
    Timer timer = null;
    Handler handleProgress = new Handler();
    String song;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mPlaybackService == null || song == null) {
                LogUtils.d("mPlaybackService == null");
                return;
            }
            if (mPlaybackService.isPlaying()) {
                if (isFinishing()) {
                    return;
                }
                int progress = (int) (seekBar.getMax()
                        * ((float) mPlaybackService.getProgress() / (float) mPlaybackService.getDuration()));
                updateProgressTextWithProgress(mPlaybackService.getProgress());
                if (progress >= 0 && progress <= seekBar.getMax()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        seekBar.setProgress(progress, true);
                    } else {
                        seekBar.setProgress(progress);
                    }
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (item != null) {
            GlideArms.with(this).load(item.getThumbnail()).centerCrop().into(image);
            newsTopLeadLine.setVisibility(View.VISIBLE);
            newsTopImgUnderLine.setVisibility(View.VISIBLE);
            newsTopType.setText("声 音");
            newsTopDate.setText(item.getUpdate_time());
            newsTopTitle.setText(item.getTitle());
            newsTopAuthor.setText(item.getAuthor());
            newsTopLead.setText(item.getLead());
            newsTopLead.setLineSpacing(1.5f, 1.8f);
            mPresenter.getAudioDetail(item.getId());
        }
    }

    @OnClick(R2.id.button_play_toggle)
    public void onClick() {
        if (mPlaybackService == null || song == null) {
            LogUtils.d("mPlaybackService == null");
            return;
        }
        if (mPlaybackService.isPlaying()) {
            if (song.equals(mPlaybackService.getSong())) {
                mPlaybackService.pause();
                buttonPlayToggle.setImageResource(R.drawable.public_ic_play);
            } else {
                mPlaybackService.play(song);
                buttonPlayToggle.setImageResource(R.drawable.public_ic_pause);
            }
        } else {
            if (song.equals(mPlaybackService.getSong())) {
                mPlaybackService.play();
            } else {
                mPlaybackService.play(song);
            }
            buttonPlayToggle.setImageResource(R.drawable.public_ic_pause);
        }
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
    public void onPlayStatusChanged(PlayState status) {
        LogUtils.d("onPlayStatusChanged.......status=" + status);
        switch (status) {
            case INIT:
                break;
            case PREPARE:
                break;
            case PLAYING:
                updateDuration();
                playTimer();
                buttonPlayToggle.setImageResource(R.drawable.public_ic_pause);
                LogUtils.d(mPlaybackService.getDuration() + "");
                break;
            case PAUSE:
                cancelTimer();
                buttonPlayToggle.setImageResource(R.drawable.public_ic_play);
                break;
            case ERROR:
                break;
            case COMPLETE:
                cancelTimer();
                buttonPlayToggle.setImageResource(R.drawable.public_ic_play);
                seekBar.setProgress(0);
                break;
        }
    }

    private void updateProgressTextWithProgress(int progress) {
        textViewProgress.setText(TimeUtils.formatDuration(progress));
    }

    private void updateDuration() {
        textViewDuration.setText(TimeUtils.formatDuration(mPlaybackService.getDuration()));
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mPlaybackService = ((PlaybackService.LocalBinder) service).getService();
            register();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            unRegister();
            mPlaybackService = null;
        }
    };

    private void register() {
        mPlaybackService.registerCallback(this);
    }

    private void unRegister() {
        if (mPlaybackService != null) {
            mPlaybackService.unregisterCallback(this);
            mPlaybackService = null;
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerAudioDetailComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.audio_detail_activity; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        loadView();
        initData();
        bindPlaybackService();
    }

    private void initData() {
        item = new AudioDetailEntity.DatasBean();
        item.setThumbnail(thumbnail);
        item.setUpdate_time(update_time);
        item.setTitle(title);
        item.setAuthor(author);
        item.setLead(lead);
        item.setId(id);
    }

    public void bindPlaybackService() {
        this.bindService(new Intent(this, PlaybackService.class), mConnection, Context.BIND_AUTO_CREATE);
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
        scrollView.setScrollViewCallbacks(this);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.public_parallax_image_height);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                cancelTimer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlaybackService.seekTo(getSeekDuration(seekBar.getProgress()));
                playTimer();
            }
        });
    }

    private void playTimer() {
        if (mPlaybackService == null || song == null) {
            LogUtils.d("mPlaybackService == null");
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mPlaybackService == null)
                    return;
                if (mPlaybackService.isPlaying()) {
                    handleProgress.post(runnable);
                }
            }
        }, 0, 1000);
    }

    private int getSeekDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / seekBar.getMax()));
    }

    private int getCurrentSongDuration() {
        int duration = 0;
        if (mPlaybackService != null) {
            duration = mPlaybackService.getDuration();
        }
        return duration;
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
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
    public void updateListUI(AudioDetailWebEntity.DatasBean infos) {
        song = infos.getFm();
        if (infos.getParseXML() == 1) {
            int i = infos.getLead().trim().length();
            AnalysisHTML analysisHTML = new AnalysisHTML();
            analysisHTML.loadHtml(this, infos.getContent(), analysisHTML.HTML_STRING, newsParseWeb, i);
            newsTopType.setText("音 频");
        } else {
            initWebViewSetting();
            newsParseWeb.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            newsTop.setVisibility(View.GONE);
            webView.loadUrl(addParams2WezeitUrl(infos.getHtml5(), false));
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

    @Override
    public void onComplete(PlayState status) {
        LogUtils.d("onComplete.......");
        cancelTimer();
    }

    @Override
    public void onPosition(int position) {
        LogUtils.d("onPosition.......=" + position);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {
        int baseColor = getResources().getColor(R.color.public_primary);
        float alpha = Math.min(1, (float) i / mParallaxImageHeight);
        toolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    protected void onDestroy() {
        handleProgress.removeCallbacks(runnable);
        unRegister();
        unbindService(mConnection);
        super.onDestroy();
    }
}
