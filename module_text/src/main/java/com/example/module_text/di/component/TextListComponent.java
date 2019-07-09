package com.example.module_text.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.example.module_text.di.module.TextListModule;
import com.example.module_text.mvp.contract.TextListContract;

import com.jess.arms.di.scope.ActivityScope;
import com.example.module_text.mvp.ui.activity.TextListActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/02/2019 11:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TextListModule.class, dependencies = AppComponent.class)
public interface TextListComponent {
    void inject(TextListActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TextListComponent.Builder view(TextListContract.View view);

        TextListComponent.Builder appComponent(AppComponent appComponent);

        TextListComponent build();
    }
}