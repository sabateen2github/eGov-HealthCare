package com.health.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.health.alaa.Inflators.MainLayout;
import com.health.objects.GetProcedure;
import com.health.objects.Strings;
import com.health.project.entry.Application;
import com.health.project.entry.DataChangedListener;
import com.health.project.entry.R;

public class MainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, DataChangedListener {


    public static final int Fragment_Notification = 1;
    public static final int Fragment_Visit = 2;
    public static final int Fragment_Condtion = 3;
    public static final int Fragment_Account = 4;

    private int currentFragment;
    private boolean is_empty = false;

    private Handler main_handler;

    private MainLayout main_layout;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        main_handler = new Handler();
        Application.LastOneCalled = this;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Application.LastOneCalled = null;
    }


    private GetProcedure.Procedure temp = null;

    public void SelectVisits(GetProcedure.Procedure proc) {
        if (currentFragment == Fragment_Visit) return;
        temp = proc;
        main_layout.getNavigationView().setSelectedItemId(R.id.visits);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(Strings.CURRENT_FRAGMENT, currentFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        main_layout = new MainLayout(inflater.getContext(), parent);
        return main_layout.getMainLayout();
    }

    @Override
    public void onViewCreated(View v, Bundle state) {
        super.onViewCreated(v, state);

        if (state == null) {
            currentFragment = Fragment_Condtion;
        } else {

            currentFragment = state.getInt(Strings.CURRENT_FRAGMENT, Fragment_Condtion);
        }
        switch (currentFragment) {
            case Fragment_Notification:
                main_layout.getNavigationView().setSelectedItemId(R.id.notification);
                break;
            case Fragment_Visit:
                main_layout.getNavigationView().setSelectedItemId(R.id.visits);

                break;
            case Fragment_Condtion:
                main_layout.getNavigationView().setSelectedItemId(R.id.condtions);

                break;
            case Fragment_Account:
                main_layout.getNavigationView().setSelectedItemId(R.id.account);
                break;
        }


        if (Application.is_initialized) {
            main_layout.getContentLayout().removeAllViews();

            switch (currentFragment) {
                case Fragment_Notification:
                    if (state == null) {
                        if (Application.Notifications.size() > 0) {
                            getActivity().getSupportFragmentManager().beginTransaction().add(main_layout.getContentLayout().getId(), new NotificationFragment()).commit();
                            is_empty = false;
                        } else {
                            is_empty = true;
                            EmptyFragment fr = new EmptyFragment();
                            fr.msg_id = R.string.Empty_Noti;
                            getActivity().getSupportFragmentManager().beginTransaction().add(main_layout.getContentLayout().getId(), fr).commit();
                        }
                    }
                    break;
                case Fragment_Visit:
                    if (state == null) {

                        if (Application.ActiveProcedures.size() > 0) {
                            getActivity().getSupportFragmentManager().beginTransaction().add(main_layout.getContentLayout().getId(), new VisitsFragment()).commit();
                            is_empty = false;
                        } else {
                            is_empty = true;
                            EmptyFragment fr = new EmptyFragment();
                            fr.msg_id = R.string.Empty_Visits;
                            getActivity().getSupportFragmentManager().beginTransaction().add(main_layout.getContentLayout().getId(), fr).commit();
                        }
                    }
                    break;
                case Fragment_Condtion:
                    if (state == null) {


                        if (Application.ActiveCondtions.size() > 0) {
                            getActivity().getSupportFragmentManager().beginTransaction().add(main_layout.getContentLayout().getId(), new CondtionsFragment()).commit();
                            is_empty = false;
                        } else {
                            is_empty = true;
                            EmptyFragment fr = new EmptyFragment();
                            fr.msg_id = R.string.Empty_Condtions;
                            getActivity().getSupportFragmentManager().beginTransaction().add(main_layout.getContentLayout().getId(), fr).commit();
                        }
                    }
                    break;
                case Fragment_Account:
                    if (state == null) {
                        is_empty = false;
                        getActivity().getSupportFragmentManager().beginTransaction().add(main_layout.getContentLayout().getId(), new PersonalDataFragment()).commit();
                    }
                    break;
            }
        }
        if (currentFragment == Fragment_Visit) {
            main_layout.getOrder_service_btn().setVisibility(View.VISIBLE);
        } else {
            main_layout.getOrder_service_btn().setVisibility(View.GONE);
        }
        main_layout.getNavigationView().setOnNavigationItemSelectedListener(this);
        main_layout.getOrder_service_btn().setOnClickListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        switch (menuItem.getItemId()) {
            case R.id.notification:
                if (currentFragment == Fragment_Notification) return false;
                currentFragment = Fragment_Notification;
                if (Application.is_initialized) {
                    if (Application.Notifications.size() > 0) {
                        NotificationFragment notificationFragment = new NotificationFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), notificationFragment).commit();
                        is_empty = false;
                    } else {
                        is_empty = true;
                        EmptyFragment fr = new EmptyFragment();
                        fr.msg_id = R.string.Empty_Noti;
                        getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                    }
                }
                break;
            case R.id.visits:
                if (currentFragment == Fragment_Visit) return false;
                currentFragment = Fragment_Visit;

                if (Application.is_initialized) {
                    if (Application.ActiveProcedures.size() > 0) {

                        VisitsFragment visitsFragment = new VisitsFragment();
                        if (temp != null) {
                            visitsFragment.SelectVisitBeforeInit(temp);
                            temp = null;
                        }
                        getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), visitsFragment).commit();
                        is_empty = false;
                    } else {
                        is_empty = true;
                        EmptyFragment fr = new EmptyFragment();
                        fr.msg_id = R.string.Empty_Visits;
                        getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                    }
                }
                break;
            case R.id.condtions:
                if (currentFragment == Fragment_Condtion) return false;
                currentFragment = Fragment_Condtion;

                if (Application.is_initialized) {
                    if (Application.ActiveCondtions.size() > 0) {

                        CondtionsFragment condtionsFragment = new CondtionsFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), condtionsFragment).commit();
                        is_empty = false;
                    } else {
                        is_empty = true;
                        EmptyFragment fr = new EmptyFragment();
                        fr.msg_id = R.string.Empty_Condtions;
                        getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                    }
                }
                break;
            case R.id.account:
                if (currentFragment == Fragment_Account) return false;
                currentFragment = Fragment_Account;
                is_empty = false;
                if (Application.is_initialized) {
                    PersonalDataFragment accountFragment = new PersonalDataFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), accountFragment).commit();
                }
                break;
        }


        if (currentFragment == Fragment_Visit) {

            main_handler.post(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.beginDelayedTransition((ViewGroup) getActivity().findViewById(R.id.root_layout));
                    main_layout.getOrder_service_btn().setVisibility(View.VISIBLE);

                }
            });
        } else {
            main_handler.post(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.beginDelayedTransition((ViewGroup) getActivity().findViewById(R.id.root_layout));
                    main_layout.getOrder_service_btn().setVisibility(View.GONE);
                }
            });
        }

        return true;


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.order_Service:
                onOrderClicked();
                break;
        }
    }


    private void onOrderClicked() {
        if (Application.is_initialized)
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.root_layout, new ServiceOrderFragment()).addToBackStack(Strings.DSAD).commit();
        else Toast.makeText(getActivity(), Strings.STILL_LOADING, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNotificationsChanged() {
        switch (currentFragment) {
            case Fragment_Notification:
                if (Application.Notifications.size() > 0 && is_empty) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), new NotificationFragment()).commit();
                    is_empty = false;
                } else if (Application.Notifications.size() == 0 && !is_empty) {
                    is_empty = true;
                    EmptyFragment fr = new EmptyFragment();
                    fr.msg_id = R.string.Empty_Noti;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                }
                break;
            case Fragment_Visit:
                if (Application.ActiveProcedures.size() > 0 && is_empty) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), new VisitsFragment()).commit();
                    is_empty = false;
                } else if (Application.ActiveProcedures.size() == 0 && !is_empty) {
                    is_empty = true;
                    EmptyFragment fr = new EmptyFragment();
                    fr.msg_id = R.string.Empty_Visits;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                }

                break;
            case Fragment_Condtion:

                if (Application.ActiveCondtions.size() > 0 && is_empty) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), new CondtionsFragment()).commit();
                    is_empty = false;
                } else if (Application.ActiveCondtions.size() == 0 && !is_empty) {
                    is_empty = true;
                    EmptyFragment fr = new EmptyFragment();
                    fr.msg_id = R.string.Empty_Condtions;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                }

                break;
        }
    }

    @Override
    public void onNotificationInit() {
        main_layout.getContentLayout().removeAllViews();
        switch (currentFragment) {
            case Fragment_Notification:
                if (Application.Notifications.size() > 0) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), new NotificationFragment()).commit();
                    is_empty = false;
                } else {
                    is_empty = true;
                    EmptyFragment fr = new EmptyFragment();
                    fr.msg_id = R.string.Empty_Noti;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                }
                break;
            case Fragment_Visit:

                if (Application.ActiveProcedures.size() > 0) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), new VisitsFragment()).commit();
                    is_empty = false;
                } else {
                    is_empty = true;
                    EmptyFragment fr = new EmptyFragment();
                    fr.msg_id = R.string.Empty_Visits;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                }

                break;
            case Fragment_Condtion:

                if (Application.ActiveCondtions.size() > 0) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), new CondtionsFragment()).commit();
                    is_empty = false;
                } else {
                    is_empty = true;
                    EmptyFragment fr = new EmptyFragment();
                    fr.msg_id = R.string.Empty_Condtions;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(main_layout.getContentLayout().getId(), fr).commit();
                }

                break;

            case Fragment_Account:
                is_empty = false;
                getActivity().getSupportFragmentManager().beginTransaction().add(main_layout.getContentLayout().getId(), new PersonalDataFragment()).commit();
                break;
        }

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

    }

    @Override
    public void onPersonalDataChanged() {

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
}
