package me.jessyan.armscomponent.app.mvp.model.api.service;

import io.reactivex.Observable;
import me.jessyan.armscomponent.app.mvp.model.entity.MainEntity;
import me.jessyan.armscomponent.app.mvp.model.entity.SplashEntity;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static me.jessyan.armscomponent.app.mvp.model.api.Api.MAIN_DOMAIN_NAME;
import static me.jessyan.armscomponent.app.mvp.model.api.Api.SPLASH_DOMAIN_NAME;
import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * author: 小川
 * Date: 2019/6/26
 * Description:
 */
public interface MainService {

    /**
     *    http://static.owspace.com/?c=api&a=getList&p=1&model=1&page_id=0&create_time=0&client=android&version=1.3.0&time=1467867330&device_id=866963027059338&show_sdv=1
     * @param c
     * @param a
     * @param page
     * @param model (0:首页，1：文字，2：影像，3：声音，4：单向历)
     * @param pageId
     * @param createTime
     * @param client
     * @param version
     * @param time
     * @param deviceId
     * @param show_sdv
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + MAIN_DOMAIN_NAME})
    @GET("/")
    Observable<MainEntity> getList(@Query("c") String c, @Query("a") String a, @Query("p") int page, @Query("model") int model, @Query("page_id") String pageId, @Query("create_time") String createTime, @Query("client") String client, @Query("version") String version, @Query("time") long time, @Query("device_id") String deviceId, @Query("show_sdv") int show_sdv);


    @Headers({DOMAIN_NAME_HEADER + SPLASH_DOMAIN_NAME})
    @GET("/static/picture_list.txt")
    Observable<SplashEntity> getSplash(@Query("client") String client, @Query("version") String version, @Query("time") Long time, @Query("device_id") String deviceId);

}
