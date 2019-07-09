package com.example.module_audio.mvp.model;

import android.app.Application;

import com.example.module_audio.mvp.model.api.service.AudioService;
import com.example.module_audio.mvp.model.entity.AudioDetailEntity;
import com.example.module_audio.mvp.model.entity.AudioDetailWebEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.example.module_audio.mvp.contract.AudioDetailContract;

import io.reactivex.Observable;


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
public class AudioDetailModel extends BaseModel implements AudioDetailContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public AudioDetailModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    public Observable<AudioDetailWebEntity> getDetail(String api, String getPost, String itemId, int show_sdv) {
        return mRepositoryManager.obtainRetrofitService(AudioService.class)
                .getAudioDetail(api, getPost, itemId, show_sdv);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}