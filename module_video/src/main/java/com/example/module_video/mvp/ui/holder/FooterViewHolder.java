package com.example.module_video.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.example.module_video.R2;
import com.example.module_video.mvp.model.entity.VideoListEntity;
import com.jess.arms.base.BaseHolder;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;

/**
 * author: 小川
 * Date: 2019/7/3
 * Description:
 */
public class FooterViewHolder extends BaseHolder {

    private final int itemCount;
    private final List<VideoListEntity.DatasBean> infos;
    @BindView(R2.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R2.id.nomore_tx)
    TextView noMoreTx;
    @BindView(R2.id.error_tx)
    TextView errorTx;

    private boolean hasMore = true;
    private boolean error = false;

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public FooterViewHolder(View itemView, int itemCount, List<VideoListEntity.DatasBean> infos) {
        super(itemView);
        this.itemCount = itemCount;
        this.infos = infos;
    }

    @Override
    public void setData(Object data, int position) {
        if (position + 1 == itemCount) {
            if (infos.size() == 0) {
                return;
            }
            if (error) {
                error = false;
                avi.setVisibility(View.GONE);
                noMoreTx.setVisibility(View.GONE);
                errorTx.setVisibility(View.VISIBLE);
                errorTx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 重新加载
                    }
                });
            }
            if (hasMore) {
                avi.setVisibility(View.VISIBLE);
                noMoreTx.setVisibility(View.GONE);
                errorTx.setVisibility(View.GONE);
            } else {
                avi.setVisibility(View.GONE);
                noMoreTx.setVisibility(View.VISIBLE);
                errorTx.setVisibility(View.GONE);
            }
        }
    }
}
