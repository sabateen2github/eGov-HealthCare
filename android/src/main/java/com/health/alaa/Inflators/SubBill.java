package com.health.alaa.Inflators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.project.entry.R;

public class SubBill {


    private View view;

    private TextView subBillTitleId;
    private LinearLayout subBillContainer;
    private TextView subBillAmountHeader;
    private TextView subBillAmount;

    public SubBill(LayoutInflater inflater, ViewGroup parent) {
        this.view=inflater.inflate(R.layout.sub_bill,parent,false);
        view.setTag(this);
        bind();

    }



    public View getContainer()
    {
        return view;
    }


    public TextView getSubBillTitleId() {
        return subBillTitleId;
    }

    public LinearLayout getSubBillContainer() {
        return subBillContainer;
    }

    public TextView getSubBillAmountHeader() {
        return subBillAmountHeader;
    }

    public TextView getSubBillAmount() {
        return subBillAmount;
    }

    public void bind(){



            subBillTitleId = (TextView) view.findViewById(R.id.sub_bill_title_id);
            subBillContainer = (LinearLayout) view.findViewById(R.id.sub_bill_container);
            subBillAmountHeader = (TextView) view.findViewById(R.id.sub_bill_amount_header);
            subBillAmount = (TextView) view.findViewById(R.id.sub_bill_amount);




    }
}
