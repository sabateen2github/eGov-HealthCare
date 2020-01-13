package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.health.project.entry.R;

public class VisitItem {


    private View view;

    private ConstraintLayout visitMainItem;
    private TextView reamainingDaysHeader;
    private TextView remainingDays;
    private TextView visitTitle;
    private TextView purposeDesc;


    public ConstraintLayout getVisitMainItem() {
        return visitMainItem;
    }

    public TextView getReamainingDaysHeader() {
        return reamainingDaysHeader;
    }

    public TextView getRemainingDays() {
        return remainingDays;
    }

    public TextView getVisitTitle() {
        return visitTitle;
    }

    public TextView getPurposeDesc() {
        return purposeDesc;
    }

    public TextView getPurposeTitle() {
        return purposeTitle;
    }

    public TextView getDate() {
        return date;
    }

    public TextView getDateTitle() {
        return dateTitle;
    }



    private Button cancel;
    private Button ShowVisit;
    private Button ShowRelated;
    private TextView purposeTitle;
    private TextView date;
    private TextView dateTitle;

    public VisitItem(LayoutInflater inflater, ViewGroup parent)
    {
        view=inflater.inflate(R.layout.visit_item,parent,false);
        bind();
        view.setTag(this);
    }



    public Button getCancel() {
        return cancel;
    }

    public Button getShowVisit() {
        return ShowVisit;
    }

    public Button getShowRelated() {
        return ShowRelated;
    }

    public void bind(){


            visitMainItem = (ConstraintLayout) view.findViewById(R.id.visit_main_item);
            reamainingDaysHeader = (TextView) view.findViewById(R.id.reamaining_days_header);
            remainingDays = (TextView) view.findViewById(R.id.remaining_days);
            visitTitle = (TextView) view.findViewById(R.id.visit_title);
            purposeDesc = (TextView) view.findViewById(R.id.purpose_desc);
            purposeTitle = (TextView) view.findViewById(R.id.purpose_title);
            date = (TextView) view.findViewById(R.id.date);
            dateTitle = (TextView) view.findViewById(R.id.date_title);
            cancel=view.findViewById(R.id.visit_cancel_btn);
            ShowVisit=view.findViewById(R.id.show_visit_btn);
            ShowRelated=view.findViewById(R.id.show_condtion_btn);


    }
}
