package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionActivity {




    private TextView condtionActivityTitle;
    private TextView condtionActivityDesc;
    private TextView condtionActivityDescHeader;



    private TextView condtionActivityObjective;
    private TextView condtionActivityObjectiveHeader;
    public ConstraintLayout getContainer() {
        return container;
    }

    private ConstraintLayout container;

    public CondtionActivity(LayoutInflater inflater , ViewGroup parent) {

        view=inflater.inflate(R.layout.condtion_activity,parent,false);
        view.setTag(this);
        bind();
    }

    public TextView getCondtionActivityTitle() {
        return condtionActivityTitle;
    }

    public TextView getCondtionActivityDesc() {
        return condtionActivityDesc;
    }

    public TextView getCondtionActivityDescHeader() {
        return condtionActivityDescHeader;
    }

    private View view;

    public TextView getCondtionActivityObjective() {
        return condtionActivityObjective;
    }

    public TextView getCondtionActivityObjectiveHeader() {
        return condtionActivityObjectiveHeader;
    }

    public void bind(){
        container= (ConstraintLayout) view;
        condtionActivityTitle = (TextView) view.findViewById(R.id.condtion_activity_title);
        condtionActivityDesc = (TextView) view.findViewById(R.id.condtion_activity_desc);
        condtionActivityDescHeader = (TextView) view.findViewById(R.id.condtion_activity_desc_header);
        condtionActivityObjective=(TextView)view.findViewById(R.id.condtion_activity_objective);
        condtionActivityObjectiveHeader=(TextView)view.findViewById(R.id.condtion_activity_objective_head);
    }


}
