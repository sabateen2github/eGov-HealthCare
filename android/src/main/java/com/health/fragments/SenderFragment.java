package com.health.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.health.objects.EnterTheLine;
import com.health.objects.GetProcedure;
import com.health.objects.StartProcedure;
import com.health.objects.StopProcedure;
import com.health.objects.Strings;
import com.health.project.entry.Application;
import com.health.project.entry.DataChangedListener;
import com.health.project.entry.R;
import com.health.project.entry.Util;

import org.jetbrains.annotations.NonNls;

public class SenderFragment extends Fragment implements View.OnClickListener {

    /*
            for starting procedures (including smart diagnosis one)
            for cancel visistss
            for payments waiting for response (medications  or services)
            for appointment booking response
            for Entering the line
            for switchLanguage

     */

    private static int type = -1;
    private static Object obj1, obj2;
    private static boolean is_handled = false;
    private static boolean is_done = false;

    private static boolean is_failed = false;


    private static final int Type_Start_Visit = 1;
    private static final int Type_Cancel_Visit = 2;
    private static final int Type_Enter_Line = 3;


    public void startVisit(GetProcedure.Procedure procedure) {
        type = Type_Start_Visit;
        obj1 = procedure;
        is_handled = false;
        is_done = false;
    }

    public void setCancelVisit(GetProcedure.Procedure procedure) {
        type = Type_Cancel_Visit;
        obj1 = procedure;
        is_handled = false;
        is_done = false;
        is_failed = false;

    }

    public void setEnterTheLine(GetProcedure.Procedure proc, GetProcedure.ServicesGroup servicesGroup) {
        type = Type_Enter_Line;
        obj1 = proc;
        obj2 = servicesGroup;
        is_handled = false;
        is_done = false;
        is_failed = false;


    }


    private ConstraintLayout senderContainer;
    private ImageView senderBack;
    private ProgressBar senderProgress;
    @NonNls
    private TextView senderText;
    @NonNls
    private TextView senderService;
    @NonNls
    private TextView senderTitle;
    @NonNls
    private TextView senderResult;
    private Button senderNo;
    private Button sender_Yes;
    private Button sender_Done;
    private View view;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
    }


    private void update() {

        if (is_failed) {

            getFragmentManager().popBackStack();
            return;
        }


        if (getView() != null) {
            Util.safeBeginDelayedTranstion((ViewGroup) getView());
        }
        if (!is_handled && !is_done) {

            sender_Done.setVisibility(View.GONE);
            senderResult.setVisibility(View.GONE);
            senderProgress.setVisibility(View.GONE);
            senderText.setVisibility(View.GONE);
            view.findViewById(R.id.sender_icon).setVisibility(View.GONE);
            senderBack.setVisibility(View.VISIBLE);
            senderTitle.setVisibility(View.VISIBLE);
            sender_Yes.setVisibility(View.VISIBLE);
            senderNo.setVisibility(View.VISIBLE);
            senderService.setVisibility(View.VISIBLE);
            switch (type) {
                case Type_Cancel_Visit: {

                    senderTitle.setText(Strings.Do_You_want_to_cancel_visit);
                    senderService.setText(((GetProcedure.Procedure) obj1).name + " \n\n" + ((GetProcedure.Procedure) obj1).objective);
                }
                break;
                case Type_Enter_Line: {
                    senderTitle.setText(Strings.Do_You_want_to_enter_line);
                    senderService.setText(Strings.You_cant_change_turn);
                }
                break;
                case Type_Start_Visit: {
                    senderTitle.setText(Strings.Do_You_want_to_start_visit);
                    senderService.setText(((GetProcedure.Procedure) obj1).name + " \n\n" + ((GetProcedure.Procedure) obj1).objective);
                }
                break;

            }

        } else if (is_handled && !is_done) {

            sender_Done.setVisibility(View.GONE);
            senderResult.setVisibility(View.GONE);
            senderProgress.setVisibility(View.VISIBLE);
            senderBack.setVisibility(View.GONE);
            senderText.setVisibility(View.VISIBLE);
            view.findViewById(R.id.sender_icon).setVisibility(View.GONE);
            senderTitle.setVisibility(View.GONE);
            sender_Yes.setVisibility(View.GONE);
            senderNo.setVisibility(View.GONE);
            senderService.setVisibility(View.GONE);

            switch (type) {

                case Type_Cancel_Visit: {
                    senderText.setText(Strings.Cancelling_visit);
                }
                break;
                case Type_Enter_Line: {
                    senderText.setText(Strings.registering_turn);

                }
                break;
                case Type_Start_Visit: {
                    senderText.setText(Strings.starting_visit);
                }
                break;

            }


        } else {

            sender_Done.setVisibility(View.VISIBLE);
            senderResult.setVisibility(View.VISIBLE);
            senderProgress.setVisibility(View.GONE);
            senderBack.setVisibility(View.GONE);
            senderText.setVisibility(View.GONE);
            view.findViewById(R.id.sender_icon).setVisibility(View.VISIBLE);
            senderTitle.setVisibility(View.GONE);
            sender_Yes.setVisibility(View.GONE);
            senderNo.setVisibility(View.GONE);
            senderService.setVisibility(View.GONE);

            switch (type) {

                case Type_Cancel_Visit: {
                    senderResult.setText(Strings.your_visit_cancelled);
                }
                break;
                case Type_Enter_Line: {
                    senderResult.setText(Strings.please_wait_turn);
                }
                break;
                case Type_Start_Visit: {
                    senderResult.setText(Strings.your_visit_activated);
                }
                break;

            }


        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));

        view = inflater.inflate(R.layout.sender_layout, parent, false);

        senderContainer = (ConstraintLayout) view.findViewById(R.id.sender_container);
        senderBack = (ImageView) view.findViewById(R.id.sender_back);
        senderNo = (Button) view.findViewById(R.id.sender_no);
        sender_Yes = (Button) view.findViewById(R.id.sesnder_yes);
        sender_Done = (Button) view.findViewById(R.id.sesnder_Done);
        senderProgress = (ProgressBar) view.findViewById(R.id.sender_progress);
        senderText = (TextView) view.findViewById(R.id.sender_progress_text);
        senderService = (TextView) view.findViewById(R.id.sender_service);
        senderTitle = (TextView) view.findViewById(R.id.sender_title);
        senderResult = (TextView) view.findViewById(R.id.sender_result);

        senderBack.setOnClickListener(this);
        view.findViewById(R.id.sender_no).setOnClickListener(this);
        view.findViewById(R.id.sesnder_yes).setOnClickListener(this);
        view.findViewById(R.id.sesnder_Done).setOnClickListener(this);


        update();


        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.sesnder_Done:
            case R.id.sender_back:
            case R.id.sender_no: {
                getFragmentManager().popBackStack();
                if (type == Type_Start_Visit) {
                    getFragmentManager().popBackStack();
                }
            }
            break;
            case R.id.sesnder_yes: {
                is_handled = true;
                //and ither ui shit

                switch (type) {

                    case Type_Cancel_Visit: {

                        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                            @Override
                            public void run() {

                                final boolean is = StopProcedure.StopProcedure(Application.Patient_ID, ((GetProcedure.Procedure) obj1).proc_id);
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
                                        if (is) {

                                            is_done = true;
                                            if (isAdded()) update();
                                        } else {

                                            is_failed = true;
                                            if (isAdded()) update();
                                            Toast.makeText(getActivity(), Strings.FAILED_TO_CONNECT, Toast.LENGTH_LONG).show();
                                        }
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
                    break;

                    case Type_Enter_Line: {

                        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                            @Override
                            public void run() {


                                final boolean is = EnterTheLine.enterTheLine(((GetProcedure.Procedure) obj1).proc_id, ((GetProcedure.ServicesGroup) obj2).req_id, Application.Patient_ID);

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
                                        if (is) {

                                            is_done = true;

                                            if (isAdded()) update();

                                        } else {

                                            is_failed = true;
                                            if (isAdded()) update();
                                            Toast.makeText(getActivity(), Strings.FAILED_TO_CONNECT, Toast.LENGTH_LONG).show();
                                        }
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
                    break;
                    case Type_Start_Visit: {

                        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                            @Override
                            public void run() {

                                final boolean is = StartProcedure.StartProcedure(Application.Patient_ID, ((GetProcedure.Procedure) obj1));

                                Log.e("Start PRocedure",is+"");

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
                                        if (is) {


                                            is_done = true;
                                            if (isAdded()) update();
                                        } else {

                                            is_failed = true;
                                            if (isAdded()) update();
                                            Toast.makeText(getActivity(), Strings.FAILED_TO_CONNECT, Toast.LENGTH_LONG).show();
                                        }
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
                    break;

                }

                update();
            }
            break;

        }
    }
}
