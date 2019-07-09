package me.jessyan.armscomponent.commonservice.video.service;

import com.alibaba.android.arouter.facade.template.IProvider;

import me.jessyan.armscomponent.commonservice.gold.bean.GoldInfo;
import me.jessyan.armscomponent.commonservice.video.bean.VideoInfo;

/**
 * author: 小川
 * Date: 2019/6/25
 * Description:
 */
public interface VideoInfoService extends IProvider {
    VideoInfo getInfo();
}
