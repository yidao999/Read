package com.example.module_audio.mvp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;

import com.example.module_audio.R;
import com.example.module_audio.mvp.model.entity.AudioListEntity;
import com.example.module_audio.mvp.ui.holder.FooterViewHolder;
import com.example.module_audio.mvp.ui.holder.ListHolder;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import org.simple.eventbus.EventBus;

import java.util.List;

import me.jessyan.armscomponent.commonsdk.core.EventBusHub;

/**
 * author: 小川
 * Date: 2019/7/3
 * Description:
 */
public class AudioListAdapter extends DefaultAdapter<AudioListEntity.DatasBean> {

    private static final int FOOTER_TYPE = 1001;
    private static final int CONTENT_TYPE = 1002;
    private final List<AudioListEntity.DatasBean> infos;
    private FooterViewHolder footerViewHolder;

    public void setHasMore(boolean hasMore) {
        if(footerViewHolder !=null){
            footerViewHolder.setHasMore(hasMore);
        }
    }

    public void setError(boolean error) {
        if(footerViewHolder !=null){
            footerViewHolder.setError(error);
        }
    }

    public AudioListAdapter(List<AudioListEntity.DatasBean> infos) {
        super(infos);
        this.infos = infos;
    }

    @Override
    public BaseHolder<AudioListEntity.DatasBean> getHolder(View v, int viewType) {
        if (viewType == FOOTER_TYPE){
            footerViewHolder = new FooterViewHolder(v, getItemCount(), infos);
            return footerViewHolder;
        }else{
            return new ListHolder(v, getItemCount(),infos);
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == FOOTER_TYPE){
            return R.layout.audio_list_footer;
        }else{
            return R.layout.audio_list_item;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTER_TYPE;
        }
        return CONTENT_TYPE;
    }

}
