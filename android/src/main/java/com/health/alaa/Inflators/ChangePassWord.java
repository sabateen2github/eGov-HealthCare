package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.health.project.entry.R;

public class ChangePassWord  {

    private ConstraintLayout changePassLayout;
    private TextView changePassHeader;
    private TextView changePassHeaderConfirm;
    private TextView changePassHeaderNew;
    private TextView changePassTitle;
    private TextView changePassWrong;
    private ProgressBar changePassProgress;

    public Button getChangeBtn() {
        return changeBtn;
    }

    private Button changeBtn;

    private View view;


    public  ChangePassWord(LayoutInflater inflater, ViewGroup container) {
         view=inflater.inflate(R.layout.change_pass_layout, container,false);
         bind();
         view.setTag(this);
    }


    public void bind(){
        changePassLayout = (ConstraintLayout) view.findViewById(R.id.change_pass_layout);
        changePassHeader = (TextView) view.findViewById(R.id.change_pass_header);
        changeBtn=view.findViewById(R.id.change_pass_button);
        changePassHeaderConfirm = (TextView) view.findViewById(R.id.change_pass_header_confirm);
        changePassHeaderNew = (TextView) view.findViewById(R.id.change_pass_header_new);
        changePassTitle = (TextView) view.findViewById(R.id.change_pass_title);
        changePassWrong = (TextView) view.findViewById(R.id.change_pass_wrong);
        changePassProgress = (ProgressBar) view.findViewById(R.id.change_pass_progress);

    }
}

