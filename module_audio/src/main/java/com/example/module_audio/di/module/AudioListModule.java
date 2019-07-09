package com.example.module_audio.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.module_audio.mvp.model.entity.AudioListEntity;
import com.example.module_audio.mvp.ui.adapter.AudioListAdapter;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;

import com.example.module_audio.mvp.contract.AudioListContract;
import com.example.module_audio.mvp.model.AudioListModel;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/03/2019 20:47
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class AudioListModule {

    private static AudioListContract.View view;

    @Binds
    abstract AudioListContract.Model bindAudioListModel(AudioListModel model);

    @ActivityScope
    @Provides
    static RecyclerView.LayoutManager provideLayoutManager(AudioListContract.View view){
        AudioListModule.view = view;
        return new LinearLayoutManager(view.getActivity());
    }

    @ActivityScope
    @Provides
    static List<AudioListEntity.DatasBean> provideList(){
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    static AudioListAdapter provideAdapter(List<AudioListEntity.DatasBean> infos){
        AudioListAdapter adapter = new AudioListAdapter(infos);
        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<AudioListEntity.DatasBean>() {
            @Override
            public void onItemClick(View view, int viewType, AudioListEntity.DatasBean item, int position) {
                skipAudioDetailData(item);
            }

            private void skipAudioDetailData(AudioListEntity.DatasBean item) {
                String thumbnail = item.getThumbnail();
                String update_time = item.getUpdate_time();
                String title = item.getTitle();
                String author = item.getAuthor();
                String lead = item.getLead();
                String id = item.getId();

                ARouter.getInstance().build(RouterHub.AUDIO_DETAILACTIVITY)
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