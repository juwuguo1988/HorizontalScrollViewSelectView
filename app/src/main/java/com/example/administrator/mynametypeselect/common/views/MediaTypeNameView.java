package com.example.administrator.mynametypeselect.common.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.administrator.mynametypeselect.R;
import com.example.administrator.mynametypeselect.common.events.ChangeMediaTypeTabEvent;
import org.greenrobot.eventbus.EventBus;

public class MediaTypeNameView {
    public View convertView;
    public TextView tv_name;
    public LinearLayout item_name_root_layout, ll_focus_bar;
    public Context mContext;

    public MediaTypeNameView(Context context, String squareTypeName, int pos) {
        convertView = ((Activity) context).getLayoutInflater().inflate(R.layout.media_type_item_bucket, null);
        mContext = context;
        item_name_root_layout = (LinearLayout) convertView.findViewById(R.id.item_name_root_layout);
        ll_focus_bar = (LinearLayout) convertView.findViewById(R.id.ll_focus_bar);
        tv_name = (TextView) convertView.findViewById(R.id.tv_name);

        initData(squareTypeName, pos);

        initListener();
    }

    public void setSelectedStatus(boolean selected) {
        ColorStateList csl;
        if (selected) {
            csl = mContext.getResources().getColorStateList(R.color.c_2bb2ba);
            ll_focus_bar.setBackgroundResource(R.color.c_2bb2ba);
            ll_focus_bar.setVisibility(View.VISIBLE);
        } else {
            csl = mContext.getResources().getColorStateList(R.color.c_c5c5c5);
            ll_focus_bar.setVisibility(View.GONE);
        }
        tv_name.setTextColor(csl);
    }

    private void initData(String squareTypeName,int pos) {
        try {
            tv_name.setTag(pos + "");
            tv_name.setText(squareTypeName);
        } catch (Exception e) {
        }
    }

    private void initListener() {
        item_name_root_layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChangeMediaTypeTabEvent event = new ChangeMediaTypeTabEvent();
                event.position = Integer.parseInt(tv_name.getTag().toString());
                event.view_width = item_name_root_layout.getWidth();
                EventBus.getDefault().post(event);
            }
        });
    }

}
