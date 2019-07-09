package com.example.module_video.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.module_video.mvp.model.entity.VideoListEntity;
import com.example.module_video.mvp.ui.adapter.VideoListAdapter;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;

import com.example.module_video.mvp.contract.VideoListContract;
import com.example.module_video.mvp.model.VideoListModel;

import java.util.ArrayList;
import java.util.List;


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
@Module
public abstract class VideoListModule {

    private static VideoListContract.View view;

    @Binds
    abstract VideoListContract.Model bindVideoListModel(VideoListModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(VideoListContract.View view){
        VideoListModule.view = view;
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static List<VideoListEntity.DatasBean> provideList(){
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static VideoListAdapter provideAdapter(List<VideoListEntity.DatasBean> infos){
        VideoListAdapter adapter = new VideoListAdapter(infos);
        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<VideoListEntity.DatasBean>() {
            @Override
            public void onItemClick(View view, int viewType, VideoListEntity.DatasBean item, int position) {
                skipAudioDetailData(item);
            }

            private void skipAudioDetailData(VideoListEntity.DatasBean item) {

                String video = item.getVideo();
                String thumbnail = item.getThumbnail();
                String update_time = item.getUpdate_time();
                String title = item.getTitle();
                String author = item.getAuthor();
                String lead = item.getLead();
                String id = item.getId();

                ARouter.getInstance().build(RouterHub.VIDEO_DETAILACTIVITY)
                        .withString("video",video)
                        .withString("thumbnail",thumbnail)
                        .withString("update_time",update_time)
                        .withString("title",title)
                        .withString("author",author)
                        .withString("lead",lead)
                        .withString("id",id)
                        .navigation(view.getActivity());
            }
        });
        return adapter;
    }
}