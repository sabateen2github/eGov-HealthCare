package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.project.entry.R;

public class VisitFeedBackWaiting {

    private View view;

    public ConstraintLayout getVisitFeedbackContainer() {
        return visitFeedbackContainer;
    }

    public ImageView getImageIcon() {
        return imageIcon;
    }

    public TextView getMsg() {
        return msg;
    }

    private ConstraintLayout visitFeedbackContainer;
    private ImageView imageIcon;
    private TextView msg;


    public VisitFeedBackWaiting(LayoutInflater inflater, ViewGroup parent)
    {

        view=inflater.inflate(R.layout.visit_feedback_waiting,parent,false);
        view.setTag(this);
        bind();

    }
    public void bind(){

            visitFeedbackContainer = (ConstraintLayout) view.findViewById(R.id.visit_feedback_container);
            imageIcon = (ImageView) view.findViewById(R.id.image_icon);
            msg = (TextView) view.findViewById(R.id.msg);


    }
}
