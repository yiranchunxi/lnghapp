package com.siasun.rtd.lngh.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyAdapter;
import com.siasun.rtd.lngh.http.bean.VideoBean;
import com.siasun.rtd.lngh.http.glide.GlideApp;


public class VideoAdapter extends MyAdapter<VideoBean> {

    private Context context;
    public VideoAdapter(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder();
    }


    private final class VideoViewHolder extends ViewHolder{

        private TextView tv_video_name,tv_video_title;
        private ImageView iv_thumb;

        public VideoViewHolder() {
            super(R.layout.item_videoview);
            tv_video_name=findViewById(R.id.tv_video_name);
            tv_video_title=findViewById(R.id.tv_video_title);
            iv_thumb=findViewById(R.id.iv_thumb);
        }

        @Override
        public void onBindView(int position) {
            tv_video_name.setText(getItem(position).name);
            tv_video_title.setText(getItem(position).title);
            GlideApp.with(context)
                    .load(getItem(position).thumb)
                    .into(iv_thumb);
        }
    }
}
