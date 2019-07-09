package com.example.module_text.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.module_text.R;
import com.example.module_text.R2;
import com.example.module_text.mvp.model.entity.TextListEntity;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;

/**
 * author: 小川
 * Date: 2019/7/2
 * Description:
 */
public class TextListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FOOTER_TYPE = 1001;
    private static final int CONTENT_TYPE = 1002;
    private List<TextListEntity.DatasBean> infos = new ArrayList<>();
    private Context mContext;
    private boolean hasMore=true;
    private boolean error=false;

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public TextListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_list_footer, parent, false);
            return new FooterViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_list_item, parent, false);
            return new ListHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTER_TYPE;
        }
        return CONTENT_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position + 1 == getItemCount()) {
            if (infos.size()==0){
                return;
            }
            FooterViewHolder footerHolder = (FooterViewHolder)holder;
            if (error){
                error = false;
                footerHolder.avi.setVisibility(View.GONE);
                footerHolder.noMoreTx.setVisibility(View.GONE);
                footerHolder.errorTx.setVisibility(View.VISIBLE);
                footerHolder.errorTx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 重新加载
                    }
                });
            }
            if (hasMore){
                footerHolder.avi.setVisibility(View.VISIBLE);
                footerHolder.noMoreTx.setVisibility(View.GONE);
                footerHolder.errorTx.setVisibility(View.GONE);
            }else {
                footerHolder.avi.setVisibility(View.GONE);
                footerHolder.noMoreTx.setVisibility(View.VISIBLE);
                footerHolder.errorTx.setVisibility(View.GONE);
            }
        } else {
            ListHolder listHolder = (ListHolder) holder;
            final TextListEntity.DatasBean item = infos.get(position);
            listHolder.authorTv.setText(item.getAuthor());
            listHolder.titleTv.setText(item.getTitle());
            GlideArms.with(mContext).load(item.getThumbnail()).into(listHolder.imageIv);
            listHolder.typeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    skipTextDetailData(item);
                }

                private void skipTextDetailData(TextListEntity.DatasBean item) {
                    String thumbnail = item.getThumbnail();
                    String model = item.getModel();
                    String update_time = item.getUpdate_time();
                    String title = item.getTitle();
                    String author = item.getAuthor();
                    String lead = item.getLead();
                    String id = item.getId();

                    ARouter.getInstance().build(RouterHub.TEXT_DETAILACTIVITY)
                            .withString("thumbnail",thumbnail)
                            .withString("model",model)
                            .withString("update_time",update_time)
                            .withString("title",title)
                            .withString("author",author)
                            .withString("lead",lead)
                            .withString("id",id)
                            .navigation(mContext);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return infos.size() + 1;
    }

    public void setArtList(List<TextListEntity.DatasBean> infos) {
        int position = infos.size() - 1;
        this.infos.addAll(infos);
        notifyItemChanged(position);
    }

    public void replaceAllData(List<TextListEntity.DatasBean> infos) {
        this.infos.clear();
        this.infos.addAll(infos);
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        if (infos.size() == 0) {
            return "0";
        }
        TextListEntity.DatasBean item = infos.get(infos.size() - 1);
        return item.getId();
    }

    public String getLastItemCreateTime() {
        if (infos.size() == 0) {
            return "0";
        }
        TextListEntity.DatasBean item = infos.get(infos.size() - 1);
        return item.getCreate_time();
    }

    static class ListHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.image_iv)
        ImageView imageIv;
        @BindView(R2.id.arrow_iv)
        ImageView arrowIv;
        @BindView(R2.id.title_tv)
        TextView titleTv;
        @BindView(R2.id.author_tv)
        TextView authorTv;
        @BindView(R2.id.type_container)
        RelativeLayout typeContainer;

        public ListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.avi)
        AVLoadingIndicatorView avi;
        @BindView(R2.id.nomore_tx)
        TextView noMoreTx;
        @BindView(R2.id.error_tx)
        TextView errorTx;
        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
