package com.health.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.health.alaa.Inflators.BillContainer;
import com.health.alaa.Inflators.BillDetItem;
import com.health.alaa.Inflators.SubBill;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetProcedure;
import com.health.objects.PayForService;
import com.health.objects.Strings;
import com.health.project.entry.Application;
import com.health.project.entry.DataChangedListener;
import com.health.project.entry.R;
import com.health.project.entry.Util;

public class BillingFragment extends Fragment {

    public static final String B = "#08979B";
    private static GetProcedure.Procedure procedure;
    private static GetProcedure.ServicesGroup serviceGroup;

    private boolean quit = false;
    private boolean is_clicked = false;

    public void setData(GetProcedure.Procedure proc, GetProcedure.ServicesGroup group) {
        procedure = proc;
        serviceGroup = group;
        quit = false;
        is_clicked = false;
    }

    private BillContainer container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {

        super.onCreateView(inflater, parent, state);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));

        NestedScrollView nested = new NestedScrollView(inflater.getContext());
        nested.setLayoutParams(new NestedScrollView.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        container = new BillContainer(getLayoutInflater(), parent);

        float total_b = 0;
        float coverd = 0;
        float req = 0;

        for (int i = 0; i < serviceGroup.services.size(); i++) {

            GetAvailibleServices.Service service = serviceGroup.services.get(i);
            SubBill sub = new SubBill(inflater, parent);

            sub.getSubBillAmountHeader().setText(Strings.TOTAL_AMOUNT);
            sub.getSubBillTitleId().setText(Strings.SERVICE);


            BillDetItem name = new BillDetItem(inflater, parent);

            name.getSubBillKey().setText(Strings.SERVICE_NAME);
            String[] split = service.service_path.split("/");
            name.getSubBillValue().setText(split[split.length - 1]);
            sub.getSubBillContainer().addView(name.getSubBillItemKeyValue());


            BillDetItem location = new BillDetItem(inflater, parent);

            location.getSubBillKey().setText(Strings.LOCATION);
            location.getSubBillValue().setText(serviceGroup.appointment.appointment_id == 0 ? Strings.NOT_DETERMINED_YET : serviceGroup.appointment.room_path);
            sub.getSubBillContainer().addView(location.getSubBillItemKeyValue());

            BillDetItem amount = new BillDetItem(inflater, parent);
            amount.getSubBillKey().setText(Strings.AMOUNT_IN_JOD);
            amount.getSubBillValue().setText(Util.generateFloat(service.service_price, 2) + Strings.JOD);
            sub.getSubBillContainer().addView(amount.getSubBillItemKeyValue());

            total_b = +service.service_price;


            BillDetItem insurance = new BillDetItem(inflater, parent);

            insurance.getSubBillKey().setText(Strings.COVERED_AMOUNT_BY_INSURANCE);
            insurance.getSubBillValue().setText(Util.generateFloat(service.insurances.get(0).percentage / 100 * service.service_price, 2) + Strings.JOD)
            ;
            sub.getSubBillContainer().addView(insurance.getSubBillItemKeyValue());
            coverd += service.insurances.get(0).percentage / 100 * service.service_price;

            float total = service.service_price * (1 - service.insurances.get(0).percentage / 100);
            req += total;
            sub.getSubBillAmount().setText(Util.generateFloat(total, 2) + Strings.JOD);
            container.getBillSubContainer().addView(sub.getContainer());
        }

        container.getBillAmountReqCovered().setText(Util.generateFloat(req, 2) + Strings.JOD);
        container.getBillTotalAmount().setText(Util.generateFloat(total_b, 2) + Strings.JOD);
        container.getBillTotalAmountCovered().setText(Util.generateFloat(coverd, 2) + Strings.JOD);

        nested.addView(container.getBillContainer(), FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        container.getPay().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                is_clicked = true;
                Util.safeBeginDelayedTranstion((ViewGroup) getView());
                container.getPay().setVisibility(View.GONE);
                AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {

                        final boolean is = PayForService.PayForService(procedure.proc_id, serviceGroup.req_id, Application.Patient_ID);
                        Application.registerOneTime(new DataChangedListener() {
                            @Override
                            public void onNotificationsChanged() {

                            }

                            @Override
                            public void onAttachedProcedureChanged() {

                            }

                            @Override
                            public void onCondtionsChanged() {

                            }

                            @Override
                            public void onProceduresChanged() {

                            }

                            @Override
                            public void onBillsChanged() {
                                if (isAdded()) {
                                    if (is) {
                                        Toast.makeText(getActivity(), Strings.Payment_confirmed, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), Strings.Payment_error, Toast.LENGTH_LONG).show();
                                    }
                                    getFragmentManager().popBackStack();
                                } else {
                                    quit = true;
                                }
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
                            public void onCondtionsInit() {

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
                        });

                    }
                });
            }
        });

        nested.setBackgroundColor(Color.parseColor(B));
        return nested;
    }


    @Override
    public void onViewCreated(View v, Bundle state) {
        super.onViewCreated(v, state);

        if (is_clicked) {
            container.getPay().setVisibility(View.GONE);
        }
        if (quit) {
            quit = false;
            getFragmentManager().popBackStack();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
    }


}
