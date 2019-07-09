package me.jessyan.armscomponent.app.mvp.ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.jessyan.armscomponent.app.R;
import me.jessyan.armscomponent.app.mvp.model.entity.MainEntity;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonsdk.imgaEngine.config.CommonImageConfigImpl;

/**
 * author: 小川
 * Date: 2019/6/26
 * Description:
 */
public class MainFragment extends Fragment {

    String title;
    @BindView(R.id.image_iv)
    ImageView imageIv;
    @BindView(R.id.type_container)
    LinearLayout typeContainer;
    @BindView(R.id.comment_tv)
    TextView commentTv;
    @BindView(R.id.like_tv)
    TextView likeTv;
    @BindView(R.id.readcount_tv)
    TextView readcountTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.author_tv)
    TextView authorTv;
    @BindView(R.id.type_tv)
    TextView typeTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.image_type)
    ImageView imageType;
    @BindView(R.id.download_start_white)
    ImageView downloadStartWhite;
    @BindView(R.id.home_advertise_iv)
    ImageView homeAdvertiseIv;
    @BindView(R.id.pager_content)
    RelativeLayout pagerContent;
    private AppComponent appComponent;
    private ImageLoader imageLoader;
    private Context context;

    public static Fragment instance(MainEntity.DatasBean datasBean) {
        Fragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", datasBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.item_main_page, container, false);
        ButterKnife.bind(this, inflate);
        context = inflate.getContext();
        appComponent = ArmsUtils.obtainAppComponentFromContext(context);
        imageLoader = appComponent.imageLoader();
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        final MainEntity.DatasBean item = (MainEntity.DatasBean) getArguments().getSerializable("item");
        final int model = Integer.valueOf(item.getModel());
        if (model == 5) {
            pagerContent.setVisibility(View.GONE);
            homeAdvertiseIv.setVisibility(View.VISIBLE);
            imageLoader.loadImage(context,
                    CommonImageConfigImpl
                            .builder()
                            .url(item.getThumbnail())
                            .imageView(homeAdvertiseIv)
                            .build());
        } else {
            pagerContent.setVisibility(View.VISIBLE);
            homeAdvertiseIv.setVisibility(View.GONE);
            title = item.getTitle();
            imageLoader.loadImage(context,
                    CommonImageConfigImpl
                            .builder()
                            .url(item.getThumbnail())
                            .imageView(imageIv)
                            .build());
            commentTv.setText(item.getComment());
            likeTv.setText(item.getGood());
            readcountTv.setText(item.getView());
            titleTv.setText(item.getTitle());
            contentTv.setText(item.getExcerpt());
            authorTv.setText(item.getAuthor());
            typeTv.setText(item.getCategory());
            switch (model) {
                case 2:
                    imageType.setVisibility(View.VISIBLE);
                    downloadStartWhite.setVisibility(View.GONE);
                    imageType.setImageResource(R.drawable.library_video_play_symbol);
                    break;
                case 3:
                    imageType.setVisibility(View.VISIBLE);
                    downloadStartWhite.setVisibility(View.VISIBLE);
                    imageType.setImageResource(R.drawable.library_voice_play_symbol);
                    break;
                default:
                    downloadStartWhite.setVisibility(View.GONE);
                    imageType.setVisibility(View.GONE);
            }
        }

        typeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (model) {
                    case 5:
                        Uri uri = Uri.parse(item.getHtml5());
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                    case 3://声音
                        skipAudioDetailData(item);
                        break;
                    case 2://电影
                        skipVideoDetailData(item);
                        break;
                    case 1://文字
                        skipTextDetailData(item);
                        break;
//                    default:
//                        intent = new Intent(getActivity(), DetailActivity.class);
//                        intent.putExtra("item",item);
//                        startActivity(intent);
                }
            }

            private void skipVideoDetailData(MainEntity.DatasBean item) {

                String video = item.getVideo();
                String thumbnail = item.getThumbnail();
                String update_time = item.getUpdate_time();
                String title = item.getTitle();
                String author = item.getAuthor();
                String lead = item.getLead();
                String id = item.getId();

                ARouter.getInstance().build(RouterHub.VIDEO_DETAILACTIVITY)
                        .withString("video", video)
                        .withString("thumbnail", thumbnail)
                        .withString("update_time", update_time)
                        .withString("title", title)
                        .withString("author", author)
                        .withString("lead", lead)
                        .withString("id", id)
                        .navigation(getContext());
            }

            private void skipAudioDetailData(MainEntity.DatasBean item) {
                String thumbnail = item.getThumbnail();
                String update_time = item.getUpdate_time();
                String title = item.getTitle();
                String author = item.getAuthor();
                String lead = item.getLead();
                String id = item.getId();

                ARouter.getInstance().build(RouterHub.AUDIO_DETAILACTIVITY)
                        .withString("thumbnail", thumbnail)
                        .withString("update_time", update_time)
                        .withString("title", title)
                        .withString("author", author)
                        .withString("lead", lead)
                        .withString("id", id)
                        .navigation(getContext());
            }

            private void skipTextDetailData(MainEntity.DatasBean item) {
                String thumbnail = item.getThumbnail();
                String model = item.getModel();
                String update_time = item.getUpdate_time();
                String title = item.getTitle();
                String author = item.getAuthor();
                String lead = item.getLead();
                String id = item.getId();

                ARouter.getInstance().build(RouterHub.TEXT_DETAILACTIVITY)
                        .withString("thumbnail", thumbnail)
                        .withString("model", model)
                        .withString("update_time", update_time)
                        .withString("title", title)
                        .withString("author", author)
                        .withString("lead", lead)
                        .withString("id", id)
                        .navigation(getContext());
            }
        });
    }

}
