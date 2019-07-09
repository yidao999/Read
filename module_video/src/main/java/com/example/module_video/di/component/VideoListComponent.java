package com.example.module_video.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.example.module_video.di.module.VideoListModule;
import com.example.module_video.mvp.contract.VideoListContract;

import com.jess.arms.di.scope.ActivityScope;
import com.example.module_video.mvp.ui.activity.VideoListActivity;


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
@ActivityScope
@Component(modules = VideoListModule.class, dependencies = AppComponent.class)
public interface VideoListComponent {
    void inject(VideoListActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        VideoListComponent.Builder view(VideoListContract.View view);

        VideoListComponent.Builder appComponent(AppComponent appComponent);

        VideoListComponent build();
    }
}