package com.health.alaa.Inflators;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.project.entry.R;

public class BillContainer {


    private ConstraintLayout billContainer;
    private TextView billMainTitle;

    private LinearLayout billSubContainer;
    private TextView billTotalAmountHeader;
    private TextView billTotalAmountCoveredHeader;
    private TextView billAmountReqCoveredHeader;
    private TextView billAmountReqCovered;

    private TextView billTotalAmount;
    private TextView billTotalAmountCovered;

    public Button getPay() {
        return pay;
    }

    private Button pay;

    private View view;

    public BillContainer(LayoutInflater inflater, ViewGroup parent) {

        view=inflater.inflate(R.layout.bill_container,parent,false);
        view.setTag(this);
        bind();
    }

    public ConstraintLayout getBillContainer() {
        return billContainer;
    }

    public TextView getBillMainTitle() {
        return billMainTitle;
    }

    public LinearLayout getBillSubContainer() {
        return billSubContainer;
    }

    public TextView getBillTotalAmountHeader() {
        return billTotalAmountHeader;
    }

    public TextView getBillTotalAmountCoveredHeader() {
        return billTotalAmountCoveredHeader;
    }

    public TextView getBillAmountReqCoveredHeader() {
        return billAmountReqCoveredHeader;
    }

    public TextView getBillAmountReqCovered() {
        return billAmountReqCovered;
    }

    public TextView getBillTotalAmount() {
        return billTotalAmount;
    }

    public TextView getBillTotalAmountCovered() {
        return billTotalAmountCovered;
    }

    public void bind() {

        billContainer = (ConstraintLayout) view.findViewById(R.id.bill_container);
        billMainTitle = (TextView) view.findViewById(R.id.bill_main_title);
        billSubContainer = (LinearLayout) view.findViewById(R.id.bill_sub_container);
        billTotalAmountHeader = (TextView) view.findViewById(R.id.bill_total_amount_header);
        billTotalAmountCoveredHeader = (TextView) view.findViewById(R.id.bill_total_amount_covered_header);
        billAmountReqCoveredHeader = (TextView) view.findViewById(R.id.bill__amount_req_covered_header);
        billAmountReqCovered = (TextView) view.findViewById(R.id.bill__amount_req_covered);
        billTotalAmount = (TextView) view.findViewById(R.id.bill_total_amount);
        billTotalAmountCovered = (TextView) view.findViewById(R.id.bill_total_amount_covered);
        pay=view.findViewById(R.id.bill_pay_btn);
    }


}
