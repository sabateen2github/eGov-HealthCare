package com.health.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.health.Data.Notifications;
import com.health.alaa.Inflators.NotiActivity;
import com.health.alaa.Inflators.NotiMedication;
import com.health.alaa.Inflators.NotificationItem;
import com.health.alaa.Inflators.NotificationMessage;
import com.health.alaa.Inflators.NotificationVisitItem;
import com.health.objects.GetNotifications;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.project.entry.Application;
import com.health.project.entry.DataChangedListener;
import com.health.project.entry.R;
import com.health.project.entry.Util;
import com.health.project.entry.onNotificationUpdated;


public class NotificationFragment extends Fragment implements onNotificationUpdated, DataChangedListener {


    private static final int Header_Type = -199925;


    private int expanded_pos = -10;

    private static final String Tag = "expanded_pos";

    private Adapter ad;
    private RecyclerView rec;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Application.unRegisterListener(this);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        if (state != null) {

            expanded_pos = state.getInt(Tag, -1);
        }

        ad = new Adapter();
        rec = new RecyclerView(inflater.getContext());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(inflater.getContext());
        ((LinearLayoutManager) manager).setOrientation(LinearLayoutManager.VERTICAL);
        rec.setLayoutManager(manager);
        rec.setAdapter(ad);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
        rec.setItemAnimator(null);
        return rec;
    }

    @Override
    public void onViewCreated(View view, Bundle state) {

        super.onViewCreated(view, state);
        Application.registerListener(this);
    }

    @Override
    public void onUpdated(int pos, long id) {

        if (isDetached() || expanded_pos != pos){ return;}
        Util.safeBeginDelayedTranstion((ViewGroup) getView());
        ad.notifyItemChanged(expanded_pos + 1+1);
    }

    @Override
    public void onNotificationsChanged() {

        Util.safeBeginDelayedTranstion((ViewGroup) getView());
        expanded_pos = -10;
        ad.notifyDataSetChanged();
    }

    @Override
    public void onNotificationInit() {

        Util.safeBeginDelayedTranstion((ViewGroup) getView());
        expanded_pos = -10;
        ad.notifyDataSetChanged();
    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt(Tag, expanded_pos);

    }

    private class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int type;

        private int relatedNotiPos;


        public NotificationHolder(@NonNull View itemView, int type) {
            super(itemView);
            this.type = type;
            if (type == Header_Type) {
                return;
            }

            switch (type) {
                case 1:
                    NotificationItem item = (NotificationItem) itemView.getTag();
                    item.getBtn().setOnClickListener(this);
                    break;
                case (GetNotifications.Notification_Msg + 100):

                    NotificationMessage msg = (NotificationMessage) itemView.getTag();
                    msg.getRelated_condtion_btn().setOnClickListener(this);

                    break;
                case (GetNotifications.Notification_Activity + 100):
                    NotiActivity activity = (NotiActivity) itemView.getTag();
                    activity.getRelated_condtion_btn().setOnClickListener(this);
                    break;
                case (GetNotifications.Notification_Medication + 100):
                    NotiMedication med = (NotiMedication) itemView.getTag();
                    med.getRelated_condtion_btn().setOnClickListener(this);
                    break;
                default:
                    NotificationVisitItem visit = (NotificationVisitItem) itemView.getTag();
                    visit.getRelated_condtion_btn().setOnClickListener(this);
                    break;
            }
        }


        public void update(int i) {

            if (expanded_pos != -10 && i > expanded_pos) {

                relatedNotiPos = i - 1;

            } else {
                relatedNotiPos = i;
            }

            switch (type) {

                case 1: {
                    NotificationItem item = (NotificationItem) itemView.getTag();
                    Notifications.Notification n = Application.Notifications.get(relatedNotiPos);
                    item.getNotiDate().setText(n.Date);
                    item.getNotiIcon().setImageDrawable(n.icon);
                    item.getNotiTitle().setText(n.Title);
                }
                break;
                case (GetNotifications.Notification_Msg + 100): {

                    NotificationMessage msg = (NotificationMessage) itemView.getTag();
                    msg.getNotiMsgDesc().setText(Application.Notifications.get(relatedNotiPos).noti.msg);
                    if (Application.Notifications.get(relatedNotiPos).noti.condtion_id == 0) {

                        msg.getRelated_condtion_btn().setVisibility(View.GONE);
                    } else {
                        msg.getRelated_condtion_btn().setVisibility(View.VISIBLE);
                    }
                }
                break;
                case (GetNotifications.Notification_Activity + 100): {

                    NotiActivity activity = (NotiActivity) itemView.getTag();

                    if (Application.Notifications.get(relatedNotiPos).LocalActivity == null && !Application.Notifications.get(relatedNotiPos).is_updating) {
                        activity.hide();
                        Application.Notifications.get(relatedNotiPos).is_updating = true;
                        final long noti_id = Application.Notifications.get(relatedNotiPos).noti.notification_id;
                        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                            @Override
                            public void run() {
                                Notifications.FullUpdate(noti_id, NotificationFragment.this);
                            }
                        });

                    } else {
                        activity.getActivityDesc().setText(Application.Notifications.get(relatedNotiPos).LocalActivity.description);
                        activity.getObjectiveDesc().setText(Application.Notifications.get(relatedNotiPos).LocalActivity.objective);
                        activity.show();
                    }

                }
                break;
                case (GetNotifications.Notification_Medication + 100): {
                    NotiMedication med = (NotiMedication) itemView.getTag();
                    if (Application.Notifications.get(relatedNotiPos).LocalMedication == null && !Application.Notifications.get(relatedNotiPos).is_updating) {
                        Application.Notifications.get(relatedNotiPos).is_updating = true;

                        med.hide();
                        final long noti_id = Application.Notifications.get(relatedNotiPos).noti.notification_id;
                        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                            @Override
                            public void run() {
                                Notifications.FullUpdate(noti_id, NotificationFragment.this);
                            }
                        });


                    } else {
                        med.getNotiMedicationDosage().setText(Application.Notifications.get(relatedNotiPos).LocalMedication.dosage_amount);
                        med.getNotiMedicationName().setText(Application.Notifications.get(relatedNotiPos).LocalMedication.medication.medication_name);
                        med.getNotiMedicationNote().setText(Application.Notifications.get(relatedNotiPos).LocalMedication.note_patient);
                        if (Application.Notifications.get(relatedNotiPos).noti.condtion_id == 0) {
                            med.show(false);
                        } else {
                            med.show(true);
                        }


                    }
                }
                break;
                default: {
                    NotificationVisitItem visit = (NotificationVisitItem) itemView.getTag();
                    if (Application.Notifications.get(relatedNotiPos).LocalProc == null) {

                        if (!Application.Notifications.get(relatedNotiPos).is_updating) {
                            Application.Notifications.get(relatedNotiPos).is_updating = true;
                            final long noti_id = Application.Notifications.get(relatedNotiPos).noti.notification_id;
                            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                                @Override
                                public void run() {
                                    Notifications.FullUpdate(noti_id, NotificationFragment.this);
                                }
                            });
                        }
                        visit.hide();
                    } else {
                        visit.getNotiDate().setText(Application.Notifications.get(relatedNotiPos).LocalProc.date_activation);
                        visit.getNotiPurposeDesc().setText(Application.Notifications.get(relatedNotiPos).LocalProc.objective);

                        visit.getNotiVisitState().setText(Application.Notifications.get(relatedNotiPos).LocalProc.State_St);
                        visit.getNotiVisitHeader().setText(Application.Notifications.get(relatedNotiPos).LocalProc.name);
                        if (Application.Notifications.get(relatedNotiPos).noti.condtion_id == 0) {
                            visit.show(false);

                        } else {
                            visit.show(true);
                        }
                    }
                }
                break;

            }


        }


        @Override
        public void onClick(View view) {

            boolean expandable = false;
            switch (type) {
                case 1: {

                    switch (Application.Notifications.get(relatedNotiPos).noti.type) {

                        case GetNotifications.Notification_Activity: {
                            expandable = true;
                        }
                        break;
                        case GetNotifications.Notification_Condtion_Added:
                        case GetNotifications.Notification_Condtion_Removed: {
                            expandable = false;
                            CondtionFragment f = new CondtionFragment();
                            f.setID(Application.Notifications.get(relatedNotiPos).noti.condtion_id);
                            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.root_layout, f).addToBackStack(null).commit();
                        }
                        break;
                        case GetNotifications.Notification_Msg: {
                            expandable = true;
                        }
                        break;
                        case GetNotifications.Notification_Medication: {
                            expandable = true;
                        }
                        break;

                        case GetNotifications.Notification_Visit_Appointment_Change:
                        case GetNotifications.Notification_Visit_Created:
                        case GetNotifications.Notification_Visit_Done:
                        case GetNotifications.Notification_Visit_Failed: {
                            if (Application.Notifications.get(relatedNotiPos).LocalProc == null) {
                                expandable = true;
                            } else if (Application.Notifications.get(relatedNotiPos).LocalProc.state == Types.State_Proc_History) {
                                expandable = true;
                            } else {
                                expandable = false;
                                ((MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag(Strings.MAIN_FRAGMENT)).SelectVisits(Application.Notifications.get(relatedNotiPos).LocalProc);
                            }
                        }
                        break;

                    }


                    if (expandable) {
                        if (expanded_pos != -10 && expanded_pos != relatedNotiPos) {
                            Util.safeBeginDelayedTranstion((ViewGroup) getView());
                            int old = expanded_pos;
                            int neW = relatedNotiPos;
                            expanded_pos = -10;
                            expanded_pos = neW;
                            ad.notifyDataSetChanged();

                        } else if (expanded_pos == relatedNotiPos) {
                            Util.safeBeginDelayedTranstion((ViewGroup) getView());
                            int old = expanded_pos;
                            ad.notifyItemRemoved(old+1+1);
                            expanded_pos = -10;

                        } else {
                            Util.safeBeginDelayedTranstion((ViewGroup) getView());
                            expanded_pos = relatedNotiPos;
                            ad.notifyItemInserted(expanded_pos+1+1);

                        }
                    }
                    Log.e(Strings.HELLO_, Strings.EXPANDABLE + expandable);
                }
                break;
                default: {
                    CondtionFragment f = new CondtionFragment();
                    f.setID(Application.Notifications.get(relatedNotiPos).noti.condtion_id);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.root_layout, f).addToBackStack(null).commit();
                }
                break;


            }


        }
    }

    private class Adapter extends RecyclerView.Adapter<NotificationHolder> {

        @NonNull
        @Override
        public NotificationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {

            if (type == Header_Type) {

                return new NotificationHolder(getLayoutInflater().inflate(R.layout.header, viewGroup, false), Header_Type);
            }

            if (type == 1) {

                NotificationItem item = new NotificationItem(getActivity().getLayoutInflater(), viewGroup);
                return new NotificationHolder(item.getNotificationItem(), type);
            } else {
                type -= 100;
                if (type == GetNotifications.Notification_Msg) {

                    NotificationMessage msg = new NotificationMessage(getActivity().getLayoutInflater(), viewGroup);
                    return new NotificationHolder(msg.getNotiMsgItem(), type + 100);
                } else if (type == GetNotifications.Notification_Activity) {
                    NotiActivity activity = new NotiActivity(getActivity().getLayoutInflater(), viewGroup);
                    return new NotificationHolder(activity.getNotiActivityContainer(), type + 100);

                } else if (type == GetNotifications.Notification_Medication) {
                    NotiMedication med = new NotiMedication(getActivity().getLayoutInflater(), viewGroup);
                    return new NotificationHolder(med.getContainerNotiMedication(), type + 100);

                }
                /*(type== GetNotifications.Notification_Visit_Appointment_Change ||  type== GetNotifications.Notification_Visit_Done
                        || type==GetNotifications.Notification_Visit_Created || type==GetNotifications.Notification_Visit_Failed)*/
                else {

                    NotificationVisitItem visit = new NotificationVisitItem(getActivity().getLayoutInflater(), viewGroup);
                    return new NotificationHolder(visit.getNotiVisitMainItem(), type + 100);
                }

            }

        }

        @Override
        public void onBindViewHolder(@NonNull NotificationHolder notificationHolder, int i) {
            if (i == 0)
                return;
            i--;
            notificationHolder.update(i);
        }


        @Override
        public int getItemViewType(int pos) {
            if(pos==0) return Header_Type;
            pos--;
            if (pos != expanded_pos + 1) return 1;
            return Application.Notifications.get(expanded_pos).noti.type + 100;
        }

        @Override
        public int getItemCount() {
            return 1 + (Application.is_initialized ? (Application.Notifications.size() + (expanded_pos != -10 ? 1 : 0)) : 0);
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
