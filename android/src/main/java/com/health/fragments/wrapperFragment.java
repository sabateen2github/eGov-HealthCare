package com.health.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.health.project.entry.R;

public class wrapperFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater,parent,state);
        return inflater.inflate(R.layout.rootlayout,parent,false);
    }

    public void onViewCreated(View view,Bundle state)
    {
        super.onViewCreated(view,state);
        getFragmentManager().beginTransaction().add(R.id.root_layout,new SignInFragment()).commit();

    }

}
