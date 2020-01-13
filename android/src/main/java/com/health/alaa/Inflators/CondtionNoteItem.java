package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionNoteItem {

    private View view;
    private TextView condtionNoteTitle;
    private TextView condtionNoteMsg;


    private ConstraintLayout container;

    public CondtionNoteItem(LayoutInflater inflater, ViewGroup parent) {
        this.view = inflater.inflate(R.layout.condtion_note_item,parent,false);
        this.view.setTag(this);
        bind();
    }

    public ConstraintLayout getContainer() {
        return container;
    }

    public TextView getCondtionNoteTitle() {
        return condtionNoteTitle;
    }

    public TextView getCondtionNoteMsg() {
        return condtionNoteMsg;
    }

    public void bind(){
            container= (ConstraintLayout) view;
            condtionNoteTitle = (TextView) view.findViewById(R.id.condtion_note_title);
            condtionNoteMsg = (TextView) view.findViewById(R.id.condtion_note_msg);
        }


}
