package com.example.module_audio.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.module_audio.R2;
import com.example.module_audio.mvp.model.entity.AudioListEntity;
import com.jess.arms.base.BaseHolder;
import com.jess.arms.http.imageloader.glide.GlideArms;

import java.util.List;

import butterknife.BindView;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;

/**
 * author: 小川
 * Date: 2019/7/3
 * Description:
 */
public class ListHolder extends BaseHolder<AudioListEntity.DatasBean> {

    private final List<AudioListEntity.DatasBean> infos;
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

    private final int itemCount;

    public ListHolder(View itemView, int itemCount,List<AudioListEntity.DatasBean> infos) {
        super(itemView);
        this.itemCount = itemCount;
        this.infos = infos;
    }

    @Override
    public void setData(AudioListEntity.DatasBean data, int position) {
        if (position + 1 == itemCount) {
            if (infos.size()==0){
                return;
            }
        } else {
            authorTv.setText(data.getAuthor());
            titleTv.setText(data.getTitle());
            GlideArms.with(itemView.getContext()).load(data.getThumbnail()).into(imageIv);
//            typeContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    skipTextDetailData(item);
//                }
//
//                private void skipTextDetailData(TextListEntity.DatasBean item) {
//                    String thumbnail = item.getThumbnail();
//                    String model = item.getModel();
//                    String update_time = item.getUpdate_time();
//                    String title = item.getTitle();
//                    String author = item.getAuthor();
//                    String lead = item.getLead();
//                    String id = item.getId();
//
//                    ARouter.getInstance().build(RouterHub.TEXT_DETAILACTIVITY)
//                            .withString("thumbnail",thumbnail)
//                            .withString("model",model)
//                            .withString("update_time",update_time)
//                            .withString("title",title)
//                            .withString("author",author)
//                            .withString("lead",lead)
//                            .withString("id",id)
//                            .navigation(mContext);
//                }
//            });
        }
    }
}
