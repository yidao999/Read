package com.example.module_video.mvp.model;

import android.app.Application;

import com.example.module_video.mvp.model.api.service.VideoService;
import com.example.module_video.mvp.model.entity.VideoListEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.example.module_video.mvp.contract.VideoListContract;

import io.reactivex.Observable;


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
public class VideoListModel extends BaseModel implements VideoListContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public VideoListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<VideoListEntity> getList(String api, String getList, int page, int model, String pageId, String createTime, String android, String version, long time, String deviceId, int show_sdv) {
        return mRepositoryManager.obtainRetrofitService(VideoService.class)
                .getVideoList(api, getList, page, model, pageId, createTime, android, version, time, deviceId, show_sdv);
    }
}