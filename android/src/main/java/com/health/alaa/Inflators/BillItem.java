package com.health.alaa.Inflators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;

public class BillItem {


    public View view;

    private TextView billItemDateHeader;
    private TextView billItemDate;
    private TextView billItemNumberHeader;
    private TextView billItemNumber;

    public BillItem(LayoutInflater inflater, ViewGroup parent) {
        this.view =inflater.inflate(R.layout.bill_item,parent,false);
        view.setTag(this);
        bind();
    }

    public TextView getBillItemDateHeader() {
        return billItemDateHeader;
    }

    public TextView getBillItemDate() {
        return billItemDate;
    }

    public TextView getBillItemNumberHeader() {
        return billItemNumberHeader;
    }

    public TextView getBillItemNumber() {
        return billItemNumber;
    }

    public void bind() {

        billItemDateHeader = (TextView) view.findViewById(R.id.bill_item_date_header);
        billItemDate = (TextView) view.findViewById(R.id.bill_item_date);
        billItemNumberHeader = (TextView) view.findViewById(R.id.bill_item_number_header);
        billItemNumber = (TextView) view.findViewById(R.id.bill_item_number);

    }


}
