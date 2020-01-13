package com.health.alaa.Inflators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.project.entry.R;

public class VisitMedicalInstit {


    private View view;


    private FrameLayout headerContainer;
    private TextView visitIntTitle;
    private TextView visitInstNameHeader;
    private TextView visitInstName;
    private TextView visitInstServicesHeader;
    private TextView visitInstServices;
    private TextView visitInstAppointHeader;
    private LinearLayout instVisitAppointContainer;
    private TextView visitInstAppointDayTime;
    private TextView visitInstAppointDate;
    private TextView visitInstAppointPath;
    private TextView visitInstStateHeader;
    private TextView visitInstState;
    private TextView visitInstTurnHeader;
    private TextView visitInstTurn;
    private TextView visitInstQueueHeader;
    private TextView visitInstQueue;
    private LinearLayout visitInstBtnContainer;
    private LinearLayout visitContainer;

    private Button left_btn;
    private Button right_btn;



    public VisitMedicalInstit (LayoutInflater inflater, ViewGroup parent)
    {

        view=inflater.inflate(R.layout.visit_medical_institution,parent,false);
        view.setTag(this);
        bind();
    }

    public Button getLeft_btn() {
        return left_btn;
    }

    public Button getRight_btn() {
        return right_btn;
    }

    public LinearLayout getVisitContainer() {
        return visitContainer;
    }

    public FrameLayout getHeaderContainer() {
        return headerContainer;
    }

    public TextView getVisitIntTitle() {
        return visitIntTitle;
    }

    public TextView getVisitInstNameHeader() {
        return visitInstNameHeader;
    }

    public TextView getVisitInstName() {
        return visitInstName;
    }

    public TextView getVisitInstServicesHeader() {
        return visitInstServicesHeader;
    }

    public TextView getVisitInstServices() {
        return visitInstServices;
    }

    public TextView getVisitInstAppointHeader() {
        return visitInstAppointHeader;
    }

    public LinearLayout getInstVisitAppointContainer() {
        return instVisitAppointContainer;
    }

    public TextView getVisitInstAppointDayTime() {
        return visitInstAppointDayTime;
    }

    public TextView getVisitInstAppointDate() {
        return visitInstAppointDate;
    }

    public TextView getVisitInstAppointPath() {
        return visitInstAppointPath;
    }

    public TextView getVisitInstStateHeader() {
        return visitInstStateHeader;
    }

    public TextView getVisitInstState() {
        return visitInstState;
    }

    public TextView getVisitInstTurnHeader() {
        return visitInstTurnHeader;
    }

    public TextView getVisitInstTurn() {
        return visitInstTurn;
    }

    public TextView getVisitInstQueueHeader() {
        return visitInstQueueHeader;
    }

    public TextView getVisitInstQueue() {
        return visitInstQueue;
    }

    public LinearLayout getVisitInstBtnContainer() {
        return visitInstBtnContainer;
    }


    public void bind() {

        visitContainer = (LinearLayout) view;
        headerContainer = (FrameLayout) view.findViewById(R.id.header_container);
        visitIntTitle = (TextView) view.findViewById(R.id.visit_int_title);
        visitInstNameHeader = (TextView) view.findViewById(R.id.visit_inst_name_header);
        visitInstName = (TextView) view.findViewById(R.id.visit_inst_name);
        visitInstServicesHeader = (TextView) view.findViewById(R.id.visit_inst_services_header);
        visitInstServices = (TextView) view.findViewById(R.id.visit_inst_services);
        visitInstAppointHeader = (TextView) view.findViewById(R.id.visit_inst_appoint_header);
        instVisitAppointContainer = (LinearLayout) view.findViewById(R.id.inst_visit_appoint_container);
        visitInstAppointDayTime = (TextView) view.findViewById(R.id.visit_inst_appoint_day_time);
        visitInstAppointDate = (TextView) view.findViewById(R.id.visit_inst_appoint_date);
        visitInstAppointPath = (TextView) view.findViewById(R.id.visit_inst_appoint_path);
        visitInstStateHeader = (TextView) view.findViewById(R.id.visit_inst_state_header);
        visitInstState = (TextView) view.findViewById(R.id.visit_inst_state);
        visitInstTurnHeader = (TextView) view.findViewById(R.id.visit_inst_turn_header);
        visitInstTurn = (TextView) view.findViewById(R.id.visit_inst_turn);
        visitInstQueueHeader = (TextView) view.findViewById(R.id.visit_inst_queue_header);
        visitInstQueue = (TextView) view.findViewById(R.id.visit_inst_queue);
        visitInstBtnContainer = (LinearLayout) view.findViewById(R.id.visit_inst_btn_container);
        left_btn=(Button)view.findViewById(R.id.visit_inst_left_btn);
        right_btn=(Button)view.findViewById(R.id.visit_inst_right_btn);


    }
}
