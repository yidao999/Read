package com.example.module_audio.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.example.module_audio.di.module.AudioListModule;
import com.example.module_audio.mvp.contract.AudioListContract;

import com.jess.arms.di.scope.ActivityScope;
import com.example.module_audio.mvp.ui.activity.AudioListActivity;


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
@ActivityScope
@Component(modules = AudioListModule.class, dependencies = AppComponent.class)
public interface AudioListComponent {
    void inject(AudioListActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        AudioListComponent.Builder view(AudioListContract.View view);

        AudioListComponent.Builder appComponent(AppComponent appComponent);

        AudioListComponent build();
    }
}