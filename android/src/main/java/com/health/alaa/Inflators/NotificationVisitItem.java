package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.health.project.entry.R;

public class NotificationVisitItem {

    private View view;

    private ConstraintLayout notiVisitMainItem;
    private TextView notiVisitStateHeader;
    private TextView notiVisitState;
    private TextView notiVisitHeader;
    private TextView notiPurposeDesc;
    private TextView notiPurposeHeader;
    private TextView notiDate;
    private TextView notiDateHeader;
    private View noti_visit_header_;
    private Button related_condtion_btn;

    public ProgressBar getBar() {
        return bar;
    }

    private ProgressBar bar;

    public boolean isIs_show() {
        return is_show;
    }

    private boolean is_show = false;


    public Button getRelated_condtion_btn() {
        return related_condtion_btn;
    }

    public ConstraintLayout getNotiVisitMainItem() {
        return notiVisitMainItem;
    }

    public TextView getNotiVisitStateHeader() {
        return notiVisitStateHeader;
    }

    public TextView getNotiVisitState() {
        return notiVisitState;
    }

    public TextView getNotiVisitHeader() {
        return notiVisitHeader;
    }

    public TextView getNotiPurposeDesc() {
        return notiPurposeDesc;
    }

    public TextView getNotiPurposeHeader() {
        return notiPurposeHeader;
    }

    public TextView getNotiDate() {
        return notiDate;
    }

    public TextView getNotiDateHeader() {
        return notiDateHeader;
    }

    public NotificationVisitItem(LayoutInflater inflater, ViewGroup group) {

        view = inflater.inflate(R.layout.notification_visit_item, group, false);
        view.setTag(this);
        bind();
    }


    public void bind() {


        notiVisitMainItem = (ConstraintLayout) view.findViewById(R.id.noti_visit_main_item);
        notiVisitStateHeader = (TextView) view.findViewById(R.id.noti_state_visit_header);
        notiVisitState = (TextView) view.findViewById(R.id.noti_state_visit);
        notiVisitHeader = (TextView) view.findViewById(R.id.noti_visit_header);
        related_condtion_btn = (Button) view.findViewById(R.id.noti_show_condtion_btn);
        notiPurposeDesc = (TextView) view.findViewById(R.id.noti_purpose_desc);
        notiPurposeHeader = (TextView) view.findViewById(R.id.noti_purpose_header);
        notiDate = (TextView) view.findViewById(R.id.noti_date);
        notiDateHeader = (TextView) view.findViewById(R.id.noti_date_header);
        bar = (ProgressBar) view.findViewById(R.id.noti_visit_item_progress);
        noti_visit_header_ = view.findViewById(R.id.noti_visit_header_);

    }

    public void show(boolean show_related_condtion) {

        is_show = true;
        notiVisitStateHeader.setVisibility(View.VISIBLE);
        notiVisitState.setVisibility(View.VISIBLE);
        notiVisitHeader.setVisibility(View.VISIBLE);
        if (show_related_condtion) {
            related_condtion_btn.setVisibility(View.VISIBLE);
        }else
        {

            related_condtion_btn.setVisibility(View.GONE);

        }
        notiPurposeDesc.setVisibility(View.VISIBLE);
        notiPurposeHeader.setVisibility(View.VISIBLE);
        notiDate.setVisibility(View.VISIBLE);
        notiDateHeader.setVisibility(View.VISIBLE);
        noti_visit_header_.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);

    }

    public void hide() {

        is_show = false;
        notiVisitStateHeader.setVisibility(View.GONE);
        notiVisitState.setVisibility(View.GONE);
        notiVisitHeader.setVisibility(View.GONE);
        related_condtion_btn.setVisibility(View.GONE);
        notiPurposeDesc.setVisibility(View.GONE);
        notiPurposeHeader.setVisibility(View.GONE);
        notiDate.setVisibility(View.GONE);
        notiDateHeader.setVisibility(View.GONE);
        noti_visit_header_.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);

    }
}
