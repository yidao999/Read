package com.example.module_text.mvp.model;

import android.app.Application;

import com.example.module_text.mvp.model.api.service.TextService;
import com.example.module_text.mvp.model.entity.TextListEntity;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;

import javax.inject.Inject;

import com.example.module_text.mvp.contract.TextListContract;
import com.jess.arms.utils.ArmsUtils;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonsdk.utils.TimeUtil;


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
public class TextListModel extends BaseModel implements TextListContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public TextListModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
    @Override
    public Observable<TextListEntity> getList(String api, String getList, int page, int model, String pageId, String createTime, String android, String version, long time, String deviceId, int show_sdv) {
        return mRepositoryManager.obtainRetrofitService(TextService.class)
                .getTextList(api, getList, page, model, pageId, createTime, android, version, time, deviceId, show_sdv);
    }
}