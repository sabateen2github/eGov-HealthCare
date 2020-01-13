package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.health.project.entry.R;

public class OrderServiceItem {

    private View view;
    private ConstraintLayout serviceOrderItemContainer;
    private TextView serviceOrderItemName;
    private ImageView serviceOrderItemIcon;

    public ConstraintLayout getServiceOrderItemContainer() {
        return serviceOrderItemContainer;
    }

    public TextView getServiceOrderItemName() {
        return serviceOrderItemName;
    }

    public ImageView getServiceOrderItemIcon() {
        return serviceOrderItemIcon;
    }


    public OrderServiceItem(LayoutInflater inflater, ViewGroup parent)
    {


        view=inflater.inflate(R.layout.order_service_item,parent,false);
        view.setTag(this);
        bind();
    }

    public void bind(){



            serviceOrderItemContainer = (ConstraintLayout) view.findViewById(R.id.service_order_item_container);
            serviceOrderItemName = (TextView) view.findViewById(R.id.service_order_item_name);
            serviceOrderItemIcon = (ImageView) view.findViewById(R.id.service_order_item_icon);
        }


    }

