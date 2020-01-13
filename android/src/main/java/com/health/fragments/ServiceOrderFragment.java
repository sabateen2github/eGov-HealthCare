package com.health.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.health.alaa.Inflators.OrderServiceItem;
import com.health.project.entry.Application;
import com.health.project.entry.R;
import com.health.project.entry.Util;

import org.jetbrains.annotations.NonNls;

public class ServiceOrderFragment extends Fragment {





    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup parent, Bundle state)
    {
        super.onCreateView(inflater,parent,state);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));

        View v=inflater.inflate(R.layout.order_layout,parent,false);
        RecyclerView rec=(RecyclerView)v.findViewById(R.id.order_layout_recycler);
        @NonNls TextView title= (TextView) v.findViewById(R.id.order_layout_title);
        View close=v.findViewById(R.id.order_layout_close);

        rec.setAdapter(new adapter());
        rec.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        title.setText(R.string.order_layout_order_layout_title_text);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        return v;

    }




    class adapter extends RecyclerView.Adapter
    {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            OrderServiceItem item=new OrderServiceItem(getLayoutInflater(),viewGroup);

            return new RecyclerView.ViewHolder(item.getServiceOrderItemContainer()) {};
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

            OrderServiceItem item=(OrderServiceItem) viewHolder.itemView.getTag();
            item.getServiceOrderItemName().setText(Application.AttachedProc.get(i).name);
            item.getServiceOrderItemContainer().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    SenderFragment sender=new SenderFragment();
                    sender.startVisit(Application.AttachedProc.get(i));
                    getFragmentManager().beginTransaction().add(R.id.root_layout,sender,null).addToBackStack(null).commit();
                }
            });

        }

        @Override
        public int getItemCount() {
            return Application.AttachedProc.size();
        }
    }

}
