package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionServiceItem {

    private View view;

    public CondtionServiceItem(LayoutInflater inflater, ViewGroup parent) {
        this.view = inflater.inflate(R.layout.condtion_service_item,parent,false);
        this.view.setTag(this);
        bind();;
    }

    public LinearLayout getCondtionServiceListContainer() {
        return condtionServiceListContainer;
    }

    public TextView getCondtionServiceListHeader() {
        return condtionServiceListHeader;
    }

    public TextView getCondtionServiceTitle() {
        return condtionServiceTitle;
    }

    public TextView getCondtionServiceInstitution() {
        return condtionServiceInstitution;
    }

    public TextView getCondtionServiceInstitutionHeader() {
        return condtionServiceInstitutionHeader;
    }

    private LinearLayout condtionServiceListContainer;
    private TextView condtionServiceListHeader;
    private TextView condtionServiceTitle;
    private TextView condtionServiceInstitution;
    private TextView condtionServiceInstitutionHeader;

    public ConstraintLayout getContainer() {
        return container;
    }

    private ConstraintLayout container;

    public void bind(){


            condtionServiceListContainer = (LinearLayout) view.findViewById(R.id.condtion_service_list_container);
            condtionServiceListHeader = (TextView) view.findViewById(R.id.condtion_service_list_header);
            condtionServiceTitle = (TextView) view.findViewById(R.id.condtion_service_title);
            condtionServiceInstitution = (TextView) view.findViewById(R.id.condtion_service_institution);
            condtionServiceInstitutionHeader = (TextView) view.findViewById(R.id.condtion_service_institution_header);
            container= (ConstraintLayout) view;



    }
}
