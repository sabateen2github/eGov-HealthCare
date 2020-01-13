package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionRoutineVisitHeader {

    private View view;

    private TextView routineHeaderTitle;
    private TextView routineHeaderCycleHeader;
    private TextView routineHeaderCycle;
    private TextView routineHeaderActivationHeader;
    private TextView routineHeaderActivationFrom;
    private TextView routineHeaderActivationTo;
    private TextView routineHeaderPurposeHeader;
    private TextView routineHeaderPurpose;

    public CondtionRoutineVisitHeader(LayoutInflater inflater, ViewGroup parent) {
        this.view = inflater.inflate(R.layout.condtion_routine_visit_header,parent,false);
        this.view.setTag(this);
        bind();
    }

    public ConstraintLayout getContainer() {
        return container;
    }

    private ConstraintLayout container;

    public TextView getRoutineHeaderTitle() {
        return routineHeaderTitle;
    }

    public TextView getRoutineHeaderCycleHeader() {
        return routineHeaderCycleHeader;
    }

    public TextView getRoutineHeaderCycle() {
        return routineHeaderCycle;
    }

    public TextView getRoutineHeaderActivationHeader() {
        return routineHeaderActivationHeader;
    }

    public TextView getRoutineHeaderActivationFrom() {
        return routineHeaderActivationFrom;
    }

    public TextView getRoutineHeaderActivationTo() {
        return routineHeaderActivationTo;
    }

    public TextView getRoutineHeaderPurposeHeader() {
        return routineHeaderPurposeHeader;
    }

    public TextView getRoutineHeaderPurpose() {
        return routineHeaderPurpose;
    }

    public void bind() {
        container= (ConstraintLayout) view;
        routineHeaderTitle = (TextView) view.findViewById(R.id.routine_header_title);
        routineHeaderCycleHeader = (TextView) view.findViewById(R.id.routine_header_cycle_header);
        routineHeaderCycle = (TextView) view.findViewById(R.id.routine_header_cycle);
        routineHeaderActivationHeader = (TextView) view.findViewById(R.id.routine_header_activation_header);
        routineHeaderActivationFrom = (TextView) view.findViewById(R.id.routine_header_activation_from);
        routineHeaderActivationTo = (TextView) view.findViewById(R.id.routine_header_activation_to);
        routineHeaderPurposeHeader = (TextView) view.findViewById(R.id.routine_header_purpose_header);
        routineHeaderPurpose = (TextView) view.findViewById(R.id.routine_header_purpose);
    }
}
