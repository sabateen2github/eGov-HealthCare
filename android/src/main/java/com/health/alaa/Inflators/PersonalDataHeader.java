package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.health.project.entry.R;

public class PersonalDataHeader {

    private View view;
    private ConstraintLayout personalContainer;
    private TextView personalTitle;

    private Button personal_btn;




    public  PersonalDataHeader (View v)
    {

        view=v;
        bind();
    }
    public Button getPersonal_btn() {
        return personal_btn;
    }

    public ConstraintLayout getPersonalContainer() {
        return personalContainer;
    }

    public TextView getPersonalTitle() {
        return personalTitle;
    }

    public void bind(){



            personalContainer = (ConstraintLayout) view.findViewById(R.id.personal_container);
            personal_btn=(Button) view.findViewById(R.id.personl_btn);
            personalTitle = (TextView) view.findViewById(R.id.personal_title);

    }
}
