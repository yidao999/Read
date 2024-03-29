package com.example.module_audio.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.integration.cache.IntelligentCache;
import com.jess.arms.utils.ArmsUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import com.example.module_audio.BuildConfig;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

import static com.example.module_audio.mvp.model.api.Api.AUDIO_DETAIL_DOMAIN;
import static com.example.module_audio.mvp.model.api.Api.AUDIO_DETAIL_DOMAIN_NAME;
import static com.example.module_audio.mvp.model.api.Api.AUDIO_LIST_DOMAIN;
import static com.example.module_audio.mvp.model.api.Api.AUDIO_LIST_DOMAIN_NAME;

/**
 * ================================================
 * 展示 {@link AppLifecycles} 的用法
 * <p>
 * Created by ArmsComponentTemplate on 07/03/2019 20:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent">Star me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/ArmsComponent-Template">模版请保持更新</a>
 * ================================================
 */
public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        //使用 RetrofitUrlManager 切换 BaseUrl
        RetrofitUrlManager.getInstance().putDomain(AUDIO_LIST_DOMAIN_NAME, AUDIO_LIST_DOMAIN);
        RetrofitUrlManager.getInstance().putDomain(AUDIO_DETAIL_DOMAIN_NAME,AUDIO_DETAIL_DOMAIN);

        //当所有模块集成到宿主 App 时, 在 App 中已经执行了以下代码
        if (BuildConfig.IS_BUILD_MODULE) {
            //leakCanary内存泄露检查
            ArmsUtils.obtainAppComponentFromContext(application).extras()
                    .put(IntelligentCache.getKeyOfKeep(RefWatcher.class.getName())
                            , BuildConfig.USE_CANARY ? LeakCanary.install(application) : RefWatcher.DISABLED);
        }
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
}
