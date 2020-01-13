package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;

public class BillDetItem {

    private View view;


    private ViewGroup subBillItemKeyValue;
    private TextView subBillKey;
    private TextView subBillValue;

    public BillDetItem(LayoutInflater inflater, ViewGroup parent) {
        this.view = inflater.inflate(R.layout.bill_detail_item,parent,false);
        this.view.setTag(this);
        bind();
    }

    public ViewGroup getSubBillItemKeyValue() {
        return subBillItemKeyValue;
    }

    public TextView getSubBillKey() {
        return subBillKey;
    }

    public TextView getSubBillValue() {
        return subBillValue;
    }

    public void bind() {

            subBillItemKeyValue = view.findViewById(R.id.sub_bill_item_key_value);
            subBillKey = (TextView) view.findViewById(R.id.sub_bill_key);
            subBillValue = (TextView) view.findViewById(R.id.sub_bill_value);
        }




}
