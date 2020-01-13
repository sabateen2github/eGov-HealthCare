package com.health.alaa.Inflators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.project.entry.R;

public class VisitMedicalInstitSeperate {


    private View view;

    private LinearLayout instVisitAppointContainerS;
    private TextView visitInstAppointDayTimeS;

    public VisitMedicalInstitSeperate(LayoutInflater inflater, ViewGroup parent) {
        this.view = inflater.inflate(R.layout.visit_medical_instit_seperate,parent,false);
        this.view.setTag(this);
        bind();
    }

    public LinearLayout getInstVisitAppointContainerS() {
        return instVisitAppointContainerS;
    }

    public TextView getVisitInstAppointDayTimeS() {
        return visitInstAppointDayTimeS;
    }

    public TextView getVisitInstAppointDateS() {
        return visitInstAppointDateS;
    }

    public TextView getVisitInstAppointPathS() {
        return visitInstAppointPathS;
    }

    private TextView visitInstAppointDateS;
    private TextView visitInstAppointPathS;

    public void bind(){

            instVisitAppointContainerS = (LinearLayout) view.findViewById(R.id.inst_visit_appoint_container_s);
            visitInstAppointDayTimeS = (TextView) view.findViewById(R.id.visit_inst_appoint_day_time_s);
            visitInstAppointDateS = (TextView) view.findViewById(R.id.visit_inst_appoint_date_s);
            visitInstAppointPathS = (TextView) view.findViewById(R.id.visit_inst_appoint_path_s);


    }
}
