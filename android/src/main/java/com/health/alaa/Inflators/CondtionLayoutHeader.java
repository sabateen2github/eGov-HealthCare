package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionLayoutHeader {

    private View view;
    private TextView condtionTitleHeader;
    private TextView condtionTitle;
    private TextView condtionDateHeader;
    private TextView condtionDate;
    private TextView condtionRemainingDaysHeader;
    private TextView condtionRemainingDays;
    private TextView condtionDescHeader;
    private TextView condtionDesc;

    public ConstraintLayout getContainer() {
        return container;
    }

    private ConstraintLayout container;

    public CondtionLayoutHeader(LayoutInflater inflater, ViewGroup group) {
        this.view = inflater.inflate(R.layout.condtion_layout_header,group,false);
        view.setTag(this);
        bind();
    }

    public TextView getCondtionTitleHeader() {
        return condtionTitleHeader;
    }

    public TextView getCondtionTitle() {
        return condtionTitle;
    }

    public TextView getCondtionDateHeader() {
        return condtionDateHeader;
    }

    public TextView getCondtionDate() {
        return condtionDate;
    }

    public TextView getCondtionRemainingDaysHeader() {
        return condtionRemainingDaysHeader;
    }

    public TextView getCondtionRemainingDays() {
        return condtionRemainingDays;
    }

    public TextView getCondtionDescHeader() {
        return condtionDescHeader;
    }

    public TextView getCondtionDesc() {
        return condtionDesc;
    }

    public void bind(){

            container= (ConstraintLayout) view;
            condtionTitleHeader = (TextView) view.findViewById(R.id.condtion_title_header);
            condtionTitle = (TextView) view.findViewById(R.id.condtion_title);
            condtionDateHeader = (TextView) view.findViewById(R.id.condtion_date_header);
            condtionDate = (TextView) view.findViewById(R.id.condtion_date);
            condtionRemainingDaysHeader = (TextView) view.findViewById(R.id.condtion_remaining_days_header);
            condtionRemainingDays = (TextView) view.findViewById(R.id.condtion_remaining_days);
            condtionDescHeader = (TextView) view.findViewById(R.id.condtion_desc_header);
            condtionDesc = (TextView) view.findViewById(R.id.condtion_desc);


    }
}
