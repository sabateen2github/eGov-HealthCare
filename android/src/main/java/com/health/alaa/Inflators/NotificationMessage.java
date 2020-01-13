package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.health.project.entry.R;

public class NotificationMessage {

    private View view;
    private ConstraintLayout notiMsgItem;
    private TextView notiMsgTitle;
    private TextView notiMsgDesc;

    private Button related_condtion_btn;



    public NotificationMessage(LayoutInflater inflater, ViewGroup parent)
    {

        view=inflater.inflate(R.layout.notification_msg,parent,false);
        bind();
        view.setTag(this);

    }

    public Button getRelated_condtion_btn() {
        return related_condtion_btn;
    }


    public ConstraintLayout getNotiMsgItem() {
        return notiMsgItem;
    }

    public TextView getNotiMsgTitle() {
        return notiMsgTitle;
    }

    public TextView getNotiMsgDesc() {
        return notiMsgDesc;
    }

    public void bind(){
            notiMsgItem = (ConstraintLayout) view.findViewById(R.id.noti_msg_item);
            notiMsgTitle = (TextView) view.findViewById(R.id.noti_msg_title);
            notiMsgDesc = (TextView) view.findViewById(R.id.noti_msg_desc);
            related_condtion_btn=(Button)view.findViewById(R.id.related_condtion_btn);
    }


}
