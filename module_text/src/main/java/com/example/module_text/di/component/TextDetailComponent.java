package com.example.module_text.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.example.module_text.di.module.TextDetailModule;
import com.example.module_text.mvp.contract.TextDetailContract;

import com.jess.arms.di.scope.ActivityScope;
import com.example.module_text.mvp.ui.activity.TextDetailActivity;


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
@ActivityScope
@Component(modules = TextDetailModule.class, dependencies = AppComponent.class)
public interface TextDetailComponent {
    void inject(TextDetailActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TextDetailComponent.Builder view(TextDetailContract.View view);

        TextDetailComponent.Builder appComponent(AppComponent appComponent);

        TextDetailComponent build();
    }
}