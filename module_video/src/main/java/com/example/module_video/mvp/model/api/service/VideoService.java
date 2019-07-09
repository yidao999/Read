package com.example.module_video.mvp.model.api.service;

import com.example.module_video.mvp.model.entity.VideoDetailWebEntity;
import com.example.module_video.mvp.model.entity.VideoListEntity;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.example.module_video.mvp.model.api.Api.VIDEO_DETAIL_DOMAIN_NAME;
import static com.example.module_video.mvp.model.api.Api.VIDEO_LIST_DOMAIN_NAME;
import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * author: 小川
 * Date: 2019/7/5
 * Description:
 */
public interface VideoService {

    /**
     * http://static.owspace.com/?c=api&a=getList&p=1&model=1&page_id=0&create_time=0&client=android&version=1.3.0&time=1467867330&device_id=866963027059338&show_sdv=1
     *
     * @param c
     * @param a
     * @param page
     * @param model      (0:首页，1：文字，2：影像，3：声音，4：单向历)
     * @param pageId
     * @param createTime
     * @param client
     * @param version
     * @param time
     * @param deviceId
     * @param show_sdv
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + VIDEO_LIST_DOMAIN_NAME})
    @GET("/")
    Observable<VideoListEntity> getVideoList(@Query("c") String c, @Query("a") String a, @Query("p") int page, @Query("model") int model, @Query("page_id") String pageId, @Query("create_time") String createTime, @Query("client") String client, @Query("version") String version, @Query("time") long time, @Query("device_id") String deviceId, @Query("show_sdv") int show_sdv);

    //详情页http://static.owspace.com/?c=api&a=getPost&post_id=292296&show_sdv=1
    @Headers({DOMAIN_NAME_HEADER + VIDEO_DETAIL_DOMAIN_NAME})
    @GET("/")
    Observable<VideoDetailWebEntity> getVideoDetail(@Query("c") String c, @Query("a") String a, @Query("post_id") String post_id, @Query("show_sdv") int show_sdv);

}
