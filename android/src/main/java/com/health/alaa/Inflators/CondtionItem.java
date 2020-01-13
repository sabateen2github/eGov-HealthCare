package com.health.alaa.Inflators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionItem {

    private View view;

    private TextView condtionTitleHeader;
    private TextView condtionTitle;
    private TextView condtionActivationDateHeader;
    private TextView condtionRemainingDaysHeader;
    private TextView condtionActivationDate;
    private TextView condtionRemainingDays;
    private TextView condtionDescHeader;
    private TextView condtionDesc;


    private Button listItemCondtionButton;




    public CondtionItem(LayoutInflater inflater, ViewGroup parent)
    {



        view=inflater.inflate(R.layout.condtion_item,parent,false);
        bind();

    }


    public Button getListItemCondtionButton() {
        return listItemCondtionButton;
    }

    public TextView getCondtionTitleHeader() {
        return condtionTitleHeader;
    }

    public TextView getCondtionTitle() {
        return condtionTitle;
    }

    public TextView getCondtionActivationDateHeader() {
        return condtionActivationDateHeader;
    }

    public TextView getCondtionRemainingDaysHeader() {
        return condtionRemainingDaysHeader;
    }

    public TextView getCondtionActivationDate() {
        return condtionActivationDate;
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

    public View getContainer()
    {
        return this.view;
    }

    private void bind(){

            condtionTitleHeader = (TextView) view.findViewById(R.id.condtion_title_header);
            condtionTitle = (TextView) view.findViewById(R.id.condtion_title);
            condtionActivationDateHeader = (TextView) view.findViewById(R.id.condtion_activation_date_header);
            condtionRemainingDaysHeader = (TextView) view.findViewById(R.id.condtion_remaining_days_header);
            condtionActivationDate = (TextView) view.findViewById(R.id.condtion_activation_date);
            condtionRemainingDays = (TextView) view.findViewById(R.id.condtion_remaining_days);
            condtionDescHeader = (TextView) view.findViewById(R.id.condtion_desc_header);
            condtionDesc = (TextView) view.findViewById(R.id.condtion_desc);
            listItemCondtionButton=view.findViewById(R.id.listitem_condtion_show);
            view.setTag(this);

    }
}
