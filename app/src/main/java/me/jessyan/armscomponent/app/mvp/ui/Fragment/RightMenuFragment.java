package me.jessyan.armscomponent.app.mvp.ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.armscomponent.app.R;
import me.jessyan.armscomponent.app.mvp.model.entity.Event;
import me.jessyan.armscomponent.commonsdk.core.EventBusHub;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/7/22
 * owspace
 */
public class RightMenuFragment extends Fragment {
    @BindView(R.id.right_slide_close)
    ImageView rightSlideClose;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.avater_iv)
    ImageView avaterIv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.notification_tv)
    TextView notificationTv;
    @BindView(R.id.favorites_tv)
    TextView favoritesTv;
    @BindView(R.id.download_tv)
    TextView downloadTv;
    @BindView(R.id.note_tv)
    TextView noteTv;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;

    private List<View> mViewList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right_menu, container, false);
        ButterKnife.bind(this, view);
        loadView();
        initStatus();
        return view;
    }

    private void initStatus() {
        int statusBarHeight = getStatusBarHeight(getContext());
        relativeLayout.setPadding(0,statusBarHeight,0,0);
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

    private void loadView() {
        mViewList.add(notificationTv);
        mViewList.add(favoritesTv);
        mViewList.add(downloadTv);
        mViewList.add(noteTv);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.right_slide_close, R.id.setting, R.id.notification_tv, R.id.favorites_tv, R.id.download_tv, R.id.note_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_slide_close:
                EventBus.getDefault().post(new Event(), EventBusHub.APPSHOWSLIDING);
                break;
            case R.id.setting:
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), SettingsActivity.class);
//                startActivity(intent);
                break;
            case R.id.notification_tv:
                break;
            case R.id.favorites_tv:
                break;
            case R.id.download_tv:
                break;
            case R.id.note_tv:
                break;
        }
    }
    public void startAnim() {
        startIconAnim(rightSlideClose);
        startIconAnim(setting);
        startColumnAnim();
    }

    private void startColumnAnim() {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation(0F, 0.0F, 0.0F, 0.0F);
        localTranslateAnimation.setDuration(700L);
        for (int j=0;j<mViewList.size();j++){
            View localView = this.mViewList.get(j);
            localView.startAnimation(localTranslateAnimation);
            localTranslateAnimation = new TranslateAnimation(j * 35,0.0F, 0.0F, 0.0F);
            localTranslateAnimation.setDuration(700L);
        }
    }

    private void startIconAnim(View paramView) {
        ScaleAnimation localScaleAnimation = new ScaleAnimation(0.1F, 1.0F, 0.1F, 1.0F, paramView.getWidth() / 2, paramView.getHeight() / 2);
        localScaleAnimation.setDuration(1000L);
        paramView.startAnimation(localScaleAnimation);
        float f1 = paramView.getWidth() / 2;
        float f2 = paramView.getHeight() / 2;
        localScaleAnimation = new ScaleAnimation(1.0F, 0.5F, 1.0F, 0.5F, f1, f2);
        localScaleAnimation.setInterpolator(new BounceInterpolator());
    }
}
