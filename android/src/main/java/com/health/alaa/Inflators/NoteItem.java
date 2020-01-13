package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;

public class NoteItem {


    private View view;

    private ConstraintLayout noteItem;
    private TextView noteTitle;
    private TextView noteDesc;

    public ConstraintLayout getNoteItem() {
        return noteItem;
    }

    public TextView getNoteTitle() {
        return noteTitle;
    }

    public TextView getNoteDesc() {
        return noteDesc;
    }


    public NoteItem(LayoutInflater inflater, ViewGroup parent)
    {

        view=inflater.inflate(R.layout.note_item,parent,false);
        view.setTag(this);
        bind();

    }
    public void bind(){


            noteItem = (ConstraintLayout) view.findViewById(R.id.note_item);
            noteTitle = (TextView) view.findViewById(R.id.note_title);
            noteDesc = (TextView) view.findViewById(R.id.note_desc);


    }
}
