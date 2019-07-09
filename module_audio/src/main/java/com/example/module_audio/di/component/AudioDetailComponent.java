package com.example.module_audio.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.example.module_audio.di.module.AudioDetailModule;
import com.example.module_audio.mvp.contract.AudioDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.example.module_audio.mvp.ui.activity.AudioDetailActivity;


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
@ActivityScope
@Component(modules = AudioDetailModule.class, dependencies = AppComponent.class)
public interface AudioDetailComponent {
    void inject(AudioDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AudioDetailComponent.Builder view(AudioDetailContract.View view);

        AudioDetailComponent.Builder appComponent(AppComponent appComponent);

        AudioDetailComponent build();
    }
}