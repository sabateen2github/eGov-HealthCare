package com.health.alaa.Inflators;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionServiceListItem {

    private View view;

    private TextView condtionServiceListItem;

    public CondtionServiceListItem(LayoutInflater inflater, ViewGroup parent) {
        this.view = inflater.inflate(R.layout.condtion_service_list_item,parent,false);
        view.setTag(this);
        bind();
    }

    public TextView getCondtionServiceListItem() {
        return condtionServiceListItem;
    }

    public void bind() {
        condtionServiceListItem = (TextView) view.findViewById(R.id.condtion_service_list_item);
    }
}
