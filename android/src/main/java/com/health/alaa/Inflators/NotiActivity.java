package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.health.project.entry.R;

public class NotiActivity {


    private View view;


    private ConstraintLayout notiActivityContainer;
    private TextView activityDescHeader;
    private TextView activityDesc;
    private TextView objectiveDescHeader;
    private TextView objectiveDesc;


    private ProgressBar bar;

    private Button related_condtion_btn;

    public boolean isIs_show() {
        return is_show;
    }

    private boolean is_show=false;


    public NotiActivity(LayoutInflater inflater, ViewGroup group) {

        view = inflater.inflate(R.layout.noti_activity, group, false);
        view.setTag(this);
        bind();

    }


    public ConstraintLayout getNotiActivityContainer() {
        return notiActivityContainer;
    }

    public ProgressBar getBar() {
        return bar;
    }

    public Button getRelated_condtion_btn() {
        return related_condtion_btn;
    }

    public TextView getActivityDescHeader() {
        return activityDescHeader;
    }

    public TextView getActivityDesc() {
        return activityDesc;
    }

    public TextView getObjectiveDescHeader() {
        return objectiveDescHeader;
    }

    public TextView getObjectiveDesc() {
        return objectiveDesc;
    }

    public void bind() {

        this.related_condtion_btn = (Button) view.findViewById(R.id.related_condtion);
        activityDescHeader = (TextView) view.findViewById(R.id.activity_desc_header);
        activityDesc = (TextView) view.findViewById(R.id.activity_desc);
        objectiveDescHeader = (TextView) view.findViewById(R.id.objective_desc_header);
        objectiveDesc = (TextView) view.findViewById(R.id.objective_desc);
        bar = view.findViewById(R.id.noti_progress_activity);
        notiActivityContainer = (ConstraintLayout) view;

    }

    public void show() {


        is_show=true;
        this.related_condtion_btn.setVisibility(View.VISIBLE);
        activityDescHeader.setVisibility(View.VISIBLE);
        activityDesc.setVisibility(View.VISIBLE);
        objectiveDescHeader.setVisibility(View.VISIBLE);
        objectiveDesc.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
    }

    public void hide() {
        is_show=false;
        this.related_condtion_btn.setVisibility(View.GONE);
        activityDescHeader.setVisibility(View.GONE);
        activityDesc.setVisibility(View.GONE);
        objectiveDescHeader.setVisibility(View.GONE);
        objectiveDesc.setVisibility(View.GONE);
        bar.setVisibility(View.VISIBLE);


    }
}
