package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.project.entry.R;

public class NotificationItem {

    private View view;

    public ConstraintLayout getNotificationItem() {
        return notificationItem;
    }

    public ImageView getNotiIcon() {
        return notiIcon;
    }

    public TextView getNotiTitle() {
        return notiTitle;
    }

    public TextView getNotiDate() {
        return notiDate;
    }

    public Button getBtn() {
        return btn;
    }

    private ConstraintLayout notificationItem;
    private ImageView notiIcon;
    private TextView notiTitle;
    private TextView notiDate;
    private Button btn;

    public NotificationItem(LayoutInflater inflater, ViewGroup parent)
    {
       view= inflater.inflate(R.layout.notification_item, parent,false);
       bind();
    }


    public void bind(){

        notificationItem = (ConstraintLayout) view.findViewById(R.id.notification_item);
        notiIcon = (ImageView) view.findViewById(R.id.noti_icon);
        notiTitle = (TextView) view.findViewById(R.id.noti_title);
        btn=(Button) view.findViewById(R.id.noti_show_btn);
        notiDate = (TextView) view.findViewById(R.id.noti_date);
        notificationItem.setTag(this);
    }





}
