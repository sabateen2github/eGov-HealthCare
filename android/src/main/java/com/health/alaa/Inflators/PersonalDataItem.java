package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.health.project.entry.R;

public class PersonalDataItem {

    private View view;




    public PersonalDataItem (View v)
    {

        view=v;
        bind();

    }

    public ConstraintLayout getPersonalItem() {
        return personalItem;
    }

    public TextView getPersonalItemHeader() {
        return personalItemHeader;
    }

    public TextView getPersonalItemData() {
        return personalItemData;
    }



    public Button getPersonal_btn() {
        return personal_btn;
    }

    private ConstraintLayout personalItem;
    private TextView personalItemHeader;
    private TextView personalItemData;
    private Button personal_btn;

    public void bind(){


            personalItem = (ConstraintLayout) view.findViewById(R.id.personal_item);
            personalItemHeader = (TextView) view.findViewById(R.id.personal_item_header);
            personalItemData = (TextView) view.findViewById(R.id.personal_item_data);
            personal_btn=(Button) view.findViewById(R.id.personl_item_btn);



    }
}
