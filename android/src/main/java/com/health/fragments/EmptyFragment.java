package com.health.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.project.entry.R;
import com.health.project.entry.Util;

public class EmptyFragment extends Fragment {


    public int msg_id = R.string.Empty_Condtions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup g, Bundle state) {
        super.onCreateView(inflater, g, state);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
        View v = inflater.inflate(R.layout.empty, g, false);
        ((TextView) v.findViewById(R.id.empty_msg_text)).setText(msg_id);
        return v;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));

    }




}
