package com.health.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.health.objects.Strings;
import com.health.alaa.Inflators.CondtionItem;
import com.health.objects.GetCondtion;
import com.health.project.entry.Application;
import com.health.project.entry.DataChangedListener;
import com.health.project.entry.R;
import com.health.project.entry.Util;

public class CondtionsFragment extends Fragment implements DataChangedListener {

    private static final int Header_Type=-199925;


    private RecyclerView rec;
    private ViewController controller;
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        Application.unRegisterListener(this);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state)
    {
        super.onCreateView(inflater,parent,state);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
        rec=new RecyclerView(inflater.getContext());
        controller=new ViewController();
        LinearLayoutManager manager=new LinearLayoutManager(inflater.getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rec.setLayoutManager(manager);
        rec.setAdapter(controller.adapter);
        return  rec;
    }


    @Override
    public void onViewCreated(View view,Bundle state)
    {
        super.onViewCreated(view,state);
        Application.registerListener(this);
    }




    @Override
    public void onCondtionsChanged() {

        Util.safeBeginDelayedTranstion((ViewGroup) getView());
        controller.adapter.notifyDataSetChanged();

    }


    @Override
    public void onCondtionsInit() {

        Util.safeBeginDelayedTranstion((ViewGroup) getView());
        controller.adapter.notifyDataSetChanged();

    }


    private class ViewController implements View.OnClickListener
    {

        AdapterView adapter=new AdapterView();

        @Override
        public void onClick(View view) {

            int index=(Integer) view.getTag();
            CondtionFragment f=new CondtionFragment();
            f.setID(Application.ActiveCondtions.get(index).condtion_id);
            getFragmentManager().beginTransaction().add(R.id.root_layout,f,null).addToBackStack(null).commit();
        }

        class CondtionHolder extends RecyclerView.ViewHolder
        {
            CondtionItem item;
            public CondtionHolder(@NonNull View itemView) {
                super(itemView);
                item= (CondtionItem) itemView.getTag();
            }
        }


        class AdapterView extends RecyclerView.Adapter<CondtionHolder>
        {

            @NonNull
            @Override
            public CondtionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                if(i==Header_Type)
                {
                        return new CondtionHolder(getLayoutInflater().inflate(R.layout.header,viewGroup,false));

                }

            CondtionItem item=new CondtionItem(getActivity().getLayoutInflater(),viewGroup);
            item.getContainer().setBackgroundColor(Color.WHITE);
            return new CondtionHolder(item.getContainer());
            }

            @Override
            public void onBindViewHolder(@NonNull CondtionHolder condtionHolder, int i) {

                if(i==0)return;
                i--;

                GetCondtion.Condtion cond= Application.ActiveCondtions.get(i);
                condtionHolder.item.getCondtionTitle().setText(cond.name);
                condtionHolder.item.getCondtionDesc().setText(cond.desc);
                condtionHolder.item.getCondtionActivationDate().setText(cond.Date_Act);
                condtionHolder.item.getCondtionRemainingDays().setText(cond.Remaining_days);
                condtionHolder.item.getListItemCondtionButton().setOnClickListener(ViewController.this);
                condtionHolder.item.getListItemCondtionButton().setTag(i);
            }



            @Override
            public int getItemViewType(int pos) {
                if(pos==0){return Header_Type;}
                pos--;
                return 1;
            }


            @Override
            public int getItemCount() {
                return Application.ActiveCondtions.size()+1;
            }
        }



    }





    @Override
    public void onProceduresChanged() {

    }

    @Override
    public void onBillsChanged() {

    }

    @Override
    public void onPersonalDataChanged() {

    }

    @Override
    public void onNotificationInit() {

    }

    @Override
    public void onAttachedProcedureInit() {

    }


    @Override
    public void onProceduresInit() {

    }

    @Override
    public void onBillsInit() {

    }

    @Override
    public void onPersonalDataInit() {

    }


    @Override
    public void onNotificationsChanged() {

    }

    @Override
    public void onAttachedProcedureChanged() {

    }

}
