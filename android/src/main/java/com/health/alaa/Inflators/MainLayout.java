package com.health.alaa.Inflators;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.health.project.entry.R;

public class MainLayout {


    private View view;


    private ConstraintLayout mainLayout;
    private FrameLayout contentLayout;
    private LinearLayout docker;
    private BottomNavigationView navigationView;

    private  Button  order_service_btn;


    public MainLayout(Context context,ViewGroup parent){

        view=((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.main_layout,parent,false);
        bind();

        navigationView.setItemIconTintList(null);
    }



    public Button getOrder_service_btn() {
        return order_service_btn;
    }

    public ConstraintLayout getMainLayout() {
        return mainLayout;
    }

    public FrameLayout getContentLayout() {
        return contentLayout;
    }

    public LinearLayout getDocker() {
        return docker;
    }

    public BottomNavigationView getNavigationView() {
        return navigationView;
    }

    public void bind(){


            mainLayout = (ConstraintLayout) view.findViewById(R.id.main_layout);
            contentLayout = (FrameLayout) view.findViewById(R.id.content_layout);
            docker = (LinearLayout) view.findViewById(R.id.docker);
            navigationView = (BottomNavigationView) view.findViewById(R.id.navigation_view);
            this.order_service_btn= (Button)view.findViewById(R.id.order_Service);



    }
}
