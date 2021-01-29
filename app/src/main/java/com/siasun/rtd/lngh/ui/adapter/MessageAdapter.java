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
import com.siasun.rtd.lngh.http.response.MyMessageResponseItem;
import com.siasun.rtd.lngh.http.response.QueryNewsResponseItemDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MessageAdapter extends MyAdapter<MyMessageResponseItem>{

    public MessageAdapter(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder();
    }




    private final class MessageViewHolder extends ViewHolder{
        private TextView tv_message_title,tv_message_time,tv_message_body;

        public MessageViewHolder() {
            super(R.layout.list_item_message);
            tv_message_title=findViewById(R.id.tv_message_title);
            tv_message_time=findViewById(R.id.tv_message_time);
            tv_message_body=findViewById(R.id.tv_message_body);
        }

        @Override
        public void onBindView(int position) {
            tv_message_title.setText(getItem(position).title);
            tv_message_body.setText(getItem(position).body);

            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date=sdf.parse(getItem(position).date_line);
                tv_message_time.setText(sdf1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


}
