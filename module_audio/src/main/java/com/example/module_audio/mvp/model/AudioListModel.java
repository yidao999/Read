package com.example.module_audio.mvp.model;

import android.app.Application;

import com.example.module_audio.mvp.model.api.service.AudioService;
import com.example.module_audio.mvp.model.entity.AudioListEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.example.module_audio.mvp.contract.AudioListContract;

import io.reactivex.Observable;


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
public class AudioListModel extends BaseModel implements AudioListContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public AudioListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<AudioListEntity> getList(String api, String getList, int page, int model, String pageId, String createTime, String android, String version, long time, String deviceId, int show_sdv) {
        return mRepositoryManager.obtainRetrofitService(AudioService.class)
                .getAudioList(api, getList, page, model, pageId, createTime, android, version, time, deviceId, show_sdv);
    }
}