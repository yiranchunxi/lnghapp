package com.siasun.rtd.lngh.ui.adapter;
import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.siasun.rtd.lngh.R;
import com.siasun.rtd.lngh.common.MyAdapter;
import com.siasun.rtd.lngh.http.glide.GlideApp;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseItemDTO;


public class NewsAdapter extends MyAdapter<QueryNewsResponseItemDTO>{
    private static final int TYPE_TOP = 0;
    private static final int TYPE_NEWS = 1;
    private static final int TYPE_TOP_TEXT = 2;
    private static final int TYPE_NEWS_TEXT = 3;
    private Context context;
    public NewsAdapter(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TOP:
                return new TopViewHolder();
            case TYPE_NEWS:
                return new NewsViewHolder();
            case TYPE_TOP_TEXT:
                return new TopViewTextHolder();
            case TYPE_NEWS_TEXT:
                return new ViewTextHolder();
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        String is_top=getData().get(position).is_top;
        String title_type=getData().get(position).title_type;
        boolean top=!TextUtils.isEmpty(is_top)&&is_top.endsWith("1");
        boolean type=!TextUtils.isEmpty(title_type)&&title_type.endsWith("1");

        if(top){
            if(type){
                return TYPE_TOP_TEXT;
            }else{
                return TYPE_TOP;
            }
        }else{
            if(type){
                return TYPE_NEWS_TEXT;
            }else{
                return TYPE_NEWS;
            }
        }
    }


    private final class NewsViewHolder extends MyAdapter.ViewHolder{
        public TextView newsItemTitleTextView;
        public TextView newsItemDateTextView;
        public TextView newsItemClassTextView;
        public ImageView newsItemImgView;

        public NewsViewHolder() {
            super(R.layout.view_news_item);
            newsItemTitleTextView = (TextView) findViewById(R.id.newsItemTitleTextView);
            newsItemDateTextView = (TextView) findViewById(R.id.newsItemDateTextView);
            newsItemImgView = (ImageView) findViewById(R.id.newsItemImgView);
            newsItemClassTextView= (TextView) findViewById(R.id.newsClassTextView);
        }

        @Override
        public void onBindView(int position) {
            newsItemTitleTextView.setText(getItem(position).title);
            newsItemDateTextView.setText(getItem(position).createtime);
            newsItemClassTextView.setText(getItem(position).news_class);

            GlideApp.with(context)
                    .load(getItem(position).short_cut)
                    .into(newsItemImgView);
        }
    }
    private final class ViewTextHolder  extends MyAdapter.ViewHolder{
        public TextView newsItemTitleTextView;
        public TextView newsItemDateTextView;
        public TextView newsItemClassTextView;

        public ViewTextHolder() {
            super(R.layout.view_news_text_item);
            newsItemTitleTextView = (TextView) findViewById(R.id.newsItemTitleTextView);
            newsItemDateTextView = (TextView) findViewById(R.id.newsItemDateTextView);
            newsItemClassTextView= (TextView) findViewById(R.id.newsClassTextView);
        }

        @Override
        public void onBindView(int position) {
           newsItemTitleTextView.setText(getItem(position).title);
           newsItemDateTextView.setText(getItem(position).createtime);
           newsItemClassTextView.setText(getItem(position).news_class);
        }
    }

    private final class  TopViewTextHolder extends MyAdapter.ViewHolder{
        public TextView newsTopItemTitleTextView;
        public TextView newsTopItemDateTextView;
        public TextView newsTopItemClassTextView;

        public TopViewTextHolder() {
            super(R.layout.view_news_top_text_item);
            newsTopItemClassTextView= (TextView) findViewById(R.id.newsTopClassTextView);
            newsTopItemDateTextView= (TextView) findViewById(R.id.newsTopItemDateTextView);
            newsTopItemTitleTextView= (TextView) findViewById(R.id.newsTopItemTitleTextView);
        }

        @Override
        public void onBindView(int position) {
            newsTopItemTitleTextView.setText(getItem(position).title);
            newsTopItemDateTextView.setText(getItem(position).createtime);
            newsTopItemClassTextView.setText(getItem(position).news_class);
        }
    }

    private final class TopViewHolder extends MyAdapter.ViewHolder{

        public TextView newsTopItemTitleTextView;
        public TextView newsTopItemDateTextView;
        public TextView newsTopItemClassTextView;
        public ImageView newsTopItemImgView;

        public TopViewHolder() {
            super(R.layout.view_news_top_item);
            newsTopItemClassTextView= (TextView) findViewById(R.id.newsTopClassTextView);
            newsTopItemDateTextView= (TextView) findViewById(R.id.newsTopItemDateTextView);
            newsTopItemImgView= (ImageView) findViewById(R.id.newsTopItemImgView);
            newsTopItemTitleTextView= (TextView) findViewById(R.id.newsTopItemTitleTextView);
        }

        @Override
        public void onBindView(int position) {
            newsTopItemTitleTextView.setText(getItem(position).title);
            newsTopItemDateTextView.setText(getItem(position).createtime);
            newsTopItemClassTextView.setText(getItem(position).news_class);
            GlideApp.with(context)
                    .load(getItem(position).short_cut)
                    .into(newsTopItemImgView);
        }
    }
}
