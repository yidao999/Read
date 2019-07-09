package com.example.module_video.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.module_video.R2;
import com.example.module_video.di.component.DaggerVideoListComponent;
import com.example.module_video.mvp.model.entity.VideoListEntity;
import com.example.module_video.mvp.ui.adapter.VideoListAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import com.example.module_video.mvp.contract.VideoListContract;
import com.example.module_video.mvp.presenter.VideoListPresenter;

import com.example.module_video.R;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import me.jessyan.armscomponent.commonres.widget.CustomPtrHeader;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.utils.AppUtil;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/05/2019 12:32
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */

@Route(path = RouterHub.VIDEO_LISTACTIVITY)
public class VideoListActivity extends BaseActivity<VideoListPresenter> implements VideoListContract.View {

    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.toolBar)
    Toolbar toolbar;
    @BindView(R2.id.recycleView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.ptrFrameLayout)
    PtrClassicFrameLayout mPtrFrame;

    private int page = 1;
    private int mode = 2;//电影
    private boolean isRefresh;
    private boolean hasMore=true;
    private String deviceId;
    private int lastVisibleItem;

    @Inject
    RecyclerView.LayoutManager layoutManager;
    @Inject
    VideoListAdapter recycleViewAdapter;
    @Inject
    List<VideoListEntity.DatasBean> mData;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerVideoListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.video_list_activity; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        deviceId = AppUtil.getDeviceId(this);
        loadView();
        initRecyclerView();
        initPtrFrame();
    }

    private void initPtrFrame() {
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page=1;
                isRefresh=true;
                hasMore = true;
                loadData(page, mode, "0", "0");
            }
        });
        mPtrFrame.setOffsetToRefresh(200);
        mPtrFrame.autoRefresh(true);
        CustomPtrHeader header = new CustomPtrHeader(this,mode);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
    }

    private void initRecyclerView() {
        ArmsUtils.configRecyclerView(mRecyclerView,layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(recycleViewAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isRefresh && hasMore && (lastVisibleItem+1  == recycleViewAdapter.getItemCount())){
                    loadData(page, mode, getLastItemId(), getLastItemCreateTime());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
    }

    private String getLastItemCreateTime() {
        if (mData.size() == 0) {
            return "0";
        }
        VideoListEntity.DatasBean item = mData.get(mData.size() - 1);
        return item.getCreate_time();
    }

    public String getLastItemId() {
        if (mData.size() == 0) {
            return "0";
        }
        VideoListEntity.DatasBean item = mData.get(mData.size() - 1);
        return item.getId();
    }

    private void loadView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        title.setText("电  影");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
    public void showOnFailure() {
        if (!isRefresh){
            //显示失败
            recycleViewAdapter.setError(true);
            recycleViewAdapter.notifyItemChanged(recycleViewAdapter.getItemCount()-1);
        }else{
            Toast.makeText(this,"~~~~(>_<)~~~~刷新失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateListUI(List<VideoListEntity.DatasBean> infos) {
        mPtrFrame.refreshComplete();
        page++;
        if (isRefresh) {
            recycleViewAdapter.setHasMore(true);
            recycleViewAdapter.setError(false);
            isRefresh = false;
            mData.clear();
            mData.addAll(infos);
            recycleViewAdapter.notifyDataSetChanged();
        } else {
            int position = infos.size() - 1;
            mData.addAll(infos);
            recycleViewAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void showNoMore() {
        hasMore = false;
        if (!isRefresh){
            //显示没有更多
            recycleViewAdapter.setHasMore(false);
            recycleViewAdapter.notifyItemChanged(recycleViewAdapter.getItemCount()-1);
        }
    }

    private void loadData(int page, int mode, String pageId, String createTime) {
        mPresenter.getAudioList(page, mode, pageId, deviceId, createTime);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

}
