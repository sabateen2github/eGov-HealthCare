package com.health.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.health.alaa.Inflators.CondtionActivity;
import com.health.alaa.Inflators.CondtionLayoutHeader;
import com.health.alaa.Inflators.CondtionMedicationItem;
import com.health.alaa.Inflators.CondtionNoteItem;
import com.health.alaa.Inflators.CondtionRoutineVisitHeader;
import com.health.alaa.Inflators.CondtionServiceItem;
import com.health.alaa.Inflators.CondtionServiceListItem;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.project.entry.Application;
import com.health.project.entry.R;
import com.health.project.entry.Util;

import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.List;

public class CondtionFragment extends Fragment {


    private List<Item> items = null;

    private long condtion_id;

    public void setID(long cond_id) {
        this.condtion_id = cond_id;
    }

    private static final int Header_Type = -199925;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        TransitionManager.beginDelayedTransition((ViewGroup) getActivity().findViewById(R.id.root_layout));

        if (state != null) {

            condtion_id = state.getLong("ID_Cond", 0);
        }


        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {

                final List<Item> items = new ArrayList<>();

                GetCondtion.Condtion conds = null;
                for (int i = 0; i < Application.ActiveCondtions.size(); i++) {
                    if (Application.ActiveCondtions.get(i).condtion_id == condtion_id) {
                        conds = Application.ActiveCondtions.get(i);
                        break;
                    }
                }

                if (conds == null) {
                    conds = GetCondtion.getCondtion(Application.Patient_ID, condtion_id);
                    conds.Date_Act = Util.generateDateAr(conds.date_activation);
                    if (conds.state == Types.State_Cond_History) {
                        conds.Date_deactive = Util.generateDateAr(conds.date_deactivation);
                    } else {
                        conds.Remaining_days = Util.convertStringToAr(conds.remaining_days + Strings.DAYS);
                    }
                }


                Item header = new Item();
                header.type = Item.Type_Header;
                header.data = conds;


                header.date_ac = conds.Date_Act;

                if (conds.state == Types.State_Cond_History) {
                    header.date_de = conds.Date_deactive;
                } else {
                    header.date_de = conds.Remaining_days;
                }

                items.add(header);
                if (conds.note_patient != null && conds.note_patient.length() > 0) {


                    Item note = new Item();
                    note.type = Item.Type_Note;
                    note.data = conds.note_patient;
                    items.add(note);

                }

                Util.Date d = new Util.Date(conds.date_activation);

                if (conds.medications_routine != null && conds.medications_routine.size() > 0) {

                    Item div = new Item();
                    div.type = Item.Type_Divider;
                    div.data = Strings.Routine_MEds;
                    items.add(div);

                    for (int i = 0; i < conds.medications_routine.size(); i++) {
                        Item medication_r = new Item();
                        medication_r.type = Item.Type_Medication_Routine;
                        medication_r.data = conds.medications_routine.get(i);
                        medication_r.date_ac = Util.generateDateAr(d.withDays(conds.medications_routine.get(i).delay_period));
                        medication_r.date_de = Util.generateDateAr(d.withDays(conds.medications_routine.get(i).delay_period + conds.medications_routine.get(i).activation_period));
                        items.add(medication_r);
                    }
                }

                if (conds.activities != null && conds.activities.size() > 0) {
                    Item div = new Item();
                    div.type = Item.Type_Divider;
                    div.data = Strings.Routine_Activities;
                    items.add(div);

                    for (int i = 0; i < conds.activities.size(); i++) {
                        Item activity_r = new Item();
                        activity_r.type = Item.Type_Activity;
                        activity_r.data = conds.activities.get(i);
                        activity_r.date_ac = Util.generateDateAr(d.withDays(conds.activities.get(i).delay_period));
                        activity_r.date_de = Util.generateDateAr(d.withDays(conds.activities.get(i).delay_period + conds.activities.get(i).activation_period));
                        items.add(activity_r);

                    }
                }


                if (conds.medication_ordinary != null && conds.medication_ordinary.size() > 0) {

                    Item div = new Item();
                    div.type = Item.Type_Divider;
                    div.data = Strings.Medications;
                    items.add(div);
                    for (int i = 0; i < conds.medication_ordinary.size(); i++) {
                        Item medication_r = new Item();
                        medication_r.type = Item.Type_Medication;
                        medication_r.data = conds.medication_ordinary.get(i);
                        medication_r.date_ac = Util.generateDateAr(d.withDays(conds.medication_ordinary.get(i).delay_period));
                        medication_r.date_de = Util.generateDateAr(d.withDays(conds.medication_ordinary.get(i).delay_period + conds.medication_ordinary.get(i).activation_period));

                        items.add(medication_r);

                    }
                }

                if (conds.procedures != null && conds.procedures.size() > 0) {
                    Item div = new Item();
                    div.type = Item.Type_Divider;
                    div.data = Strings.Visits;
                    items.add(div);
                    for (int i = 0; i < conds.procedures.size(); i++) {
                        Item visit_r = new Item();
                        visit_r.type = Item.Type_Visit;
                        visit_r.data = conds.procedures.get(i);
                        visit_r.date_ac = Util.generateDateAr(d.withDays(conds.procedures.get(i).delay_period));
                        visit_r.date_de = Util.generateDateAr(d.withDays(conds.procedures.get(i).delay_period + conds.procedures.get(i).activation_period));
                        conds.procedures.get(i).Routine_period_String = Util.convertStringToAr(Strings.EVERY + conds.procedures.get(i).cycle_period + Strings.DAYS);
                        items.add(visit_r);


                        if (conds.procedures.get(i).services != null) {
                            for (int x = 0; x < conds.procedures.get(i).services.size(); x++) {
                                Item visit_l = new Item();
                                visit_l.type = Item.Type_Service;

                                StringBuilder builder = new StringBuilder();
                                for (int o = 0; o < conds.procedures.get(i).services.get(x).rooms.size(); o++) {

                                    builder.append(conds.procedures.get(i).services.get(x).rooms.get(o).room_path.replace("/", " - ") + "\n\n\n");
                                }

                                visit_l.data = builder.toString();
                                items.add(visit_l);
                                for (int y = 0; y < conds.procedures.get(i).services.get(x).services.size(); y++) {

                                    Item visit_z = new Item();
                                    visit_z.type = Item.Type_Service_Desc;

                                    String[] split = conds.procedures.get(i).services.get(x).services.get(y).service_path.split("/");
                                    visit_z.data = split[split.length - 1];
                                    items.add(visit_z);
                                }

                            }
                        }

                    }
                }


                Application.main_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isResumed()) {
                            Util.safeBeginDelayedTranstion((ViewGroup) getView());
                            if (getView() == null) return;
                            ((ViewGroup) getView()).removeViewAt(0);
                            CondtionFragment.this.items = items;
                            RecyclerView rec = new RecyclerView(getContext());

                            rec.setLayoutManager(new LinearLayoutManager(getContext()));
                            rec.setAdapter(new adapter());
                            ((ViewGroup) getView()).addView(rec, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);

                        }
                    }
                });
            }
        });

        return inflater.inflate(R.layout.condtion_layout, parent, false);
    }


    private class adapter extends RecyclerView.Adapter {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            if (i == Header_Type) {
                return new RecyclerView.ViewHolder(getLayoutInflater().inflate(R.layout.header, viewGroup, false)) {
                };
            }

            switch (i) {

                case Item.Type_Divider: {

                    ConstraintLayout ite = (ConstraintLayout) getLayoutInflater().inflate(R.layout.condtion_divider, viewGroup, false);
                    return new RecyclerView.ViewHolder(ite) {
                    };
                }

                case Item.Type_Activity: {

                    CondtionActivity activity = new CondtionActivity(getLayoutInflater(), viewGroup);
                    return new RecyclerView.ViewHolder(activity.getContainer()) {
                    };

                }
                case Item.Type_Medication: {

                    CondtionMedicationItem ite = new CondtionMedicationItem(getLayoutInflater(), viewGroup);
                    ite.getMedicationNumberDosage().setVisibility(View.GONE);
                    ite.getMedicationNumberDosageHeader().setVisibility(View.GONE);
                    ite.getMedicationAbuseTypeHeader().setVisibility(View.GONE);
                    ite.getMedicationAbuseType().setVisibility(View.GONE);
                    return new RecyclerView.ViewHolder(ite.getContainerMedication()) {
                    };

                }
                case Item.Type_Medication_Routine: {
                    CondtionMedicationItem ite = new CondtionMedicationItem(getLayoutInflater(), viewGroup);
                    ite.getMedicationIndication().setVisibility(View.GONE);
                    ite.getMedicationIndicationHeader().setVisibility(View.GONE);
                    return new RecyclerView.ViewHolder(ite.getContainerMedication()) {
                    };

                }
                case Item.Type_Note: {
                    CondtionNoteItem ite = new CondtionNoteItem(getLayoutInflater(), viewGroup);
                    return new RecyclerView.ViewHolder(ite.getContainer()) {
                    };
                }
                case Item.Type_Service: {

                    CondtionServiceItem ite = new CondtionServiceItem(getLayoutInflater(), viewGroup);
                    return new RecyclerView.ViewHolder(ite.getContainer()) {
                    };

                }
                case Item.Type_Visit: {
                    CondtionRoutineVisitHeader ite = new CondtionRoutineVisitHeader(getLayoutInflater(), viewGroup);
                    return new RecyclerView.ViewHolder(ite.getContainer()) {
                    };
                }
                case Item.Type_Header: {
                    CondtionLayoutHeader header = new CondtionLayoutHeader(getLayoutInflater(), viewGroup);
                    return new RecyclerView.ViewHolder(header.getContainer()) {
                    };
                }
                case Item.Type_Service_Desc: {
                    CondtionServiceListItem ite = new CondtionServiceListItem(getLayoutInflater(), viewGroup);
                    return new RecyclerView.ViewHolder(ite.getCondtionServiceListItem()) {
                    };
                }

            }

            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            if (i == 0) return;
            i = i - 1;

            switch (items.get(i).type) {

                case Item.Type_Divider: {
                    ConstraintLayout ite = (ConstraintLayout) viewHolder.itemView;
                    TextView t = (TextView) ite.getChildAt(0);
                    t.setText((String) items.get(i).data);
                }
                break;
                case Item.Type_Activity: {

                    CondtionActivity activity = (CondtionActivity) viewHolder.itemView.getTag();
                    GetCondtion.Activity act = (GetCondtion.Activity) items.get(i).data;
                    activity.getCondtionActivityDesc().setText(act.description);
                    activity.getCondtionActivityObjective().setText(act.objective);

                }
                break;
                case Item.Type_Medication: {

                    CondtionMedicationItem ite = (CondtionMedicationItem) viewHolder.itemView.getTag();

                    GetCondtion.MedicationOrdinary med = (GetCondtion.MedicationOrdinary) items.get(i).data;

                    ite.getMedicationIndication().setText(med.indications);
                    ite.getMedicationName().setText(med.medication.medication_name);
                    ite.getMedicationActive().setText(items.get(i).date_ac.toString());
                    ite.getMedicationDeactive().setText(items.get(i).date_de.toString());
                    ite.getMedicationDosage().setText(med.dosage_amount);
                    ite.getMedicationNote().setText(med.note_patient);

                }
                break;
                case Item.Type_Medication_Routine: {
                    CondtionMedicationItem ite = (CondtionMedicationItem) viewHolder.itemView.getTag();

                    GetCondtion.MedicationRoutine med = (GetCondtion.MedicationRoutine) items.get(i).data;

                    ite.getMedicationNumberDosage().setText(Util.convertStringintoAr("" + (int) med.dosage_cycle) + Strings.Times + Strings.Types_dosage[med.cycle_unit]);
                    ite.getMedicationName().setText(med.medication.medication_name);
                    ite.getMedicationActive().setText(items.get(i).date_ac.toString());
                    ite.getMedicationDeactive().setText(items.get(i).date_de.toString());
                    ite.getMedicationDosage().setText(med.dosage_amount);
                    ite.getMedicationNote().setText(med.note_patient);
                    ite.getMedicationAbuseType().setText(Strings.Routine_Type);
                }
                break;
                case Item.Type_Note: {
                    CondtionNoteItem ite = (CondtionNoteItem) viewHolder.itemView.getTag();

                    ite.getCondtionNoteMsg().setText((String) items.get(i).data);

                }
                break;
                case Item.Type_Service: {

                    CondtionServiceItem ite = (CondtionServiceItem) viewHolder.itemView.getTag();
                    ite.getCondtionServiceInstitution().setText((String) items.get(i).data);
                }
                break;
                case Item.Type_Visit: {
                    CondtionRoutineVisitHeader ite = (CondtionRoutineVisitHeader) viewHolder.itemView.getTag();

                    ite.getRoutineHeaderTitle().setText(Strings.VISIT);
                    ite.getRoutineHeaderActivationFrom().setText(items.get(i).date_ac);
                    ite.getRoutineHeaderActivationTo().setText(items.get(i).date_de);
                    ite.getRoutineHeaderPurpose().setText(((GetProcedure.Procedure) items.get(i).data).objective);

                    if (((GetProcedure.Procedure) items.get(i).data).type == Types.Type_Procedure_Condtion_Routine) {
                        ite.getRoutineHeaderCycleHeader().setVisibility(View.VISIBLE);
                        ite.getRoutineHeaderCycle().setVisibility(View.VISIBLE);
                        ite.getRoutineHeaderCycle().setText(((GetProcedure.Procedure) items.get(i).data).Routine_period_String);
                    } else {
                        ite.getRoutineHeaderCycle().setVisibility(View.INVISIBLE);
                        ite.getRoutineHeaderCycleHeader().setVisibility(View.INVISIBLE);
                    }
                }
                break;
                case Item.Type_Header: {
                    CondtionLayoutHeader header = (CondtionLayoutHeader) viewHolder.itemView.getTag();

                    GetCondtion.Condtion cond = (GetCondtion.Condtion) items.get(i).data;
                    header.getCondtionDate().setText(items.get(i).date_ac);
                    header.getCondtionDesc().setText(cond.desc);
                    header.getCondtionTitle().setText(cond.name);
                    if (cond.state == Types.State_Cond_Active) {
                        header.getCondtionRemainingDaysHeader().setText(Strings.REMAINING_DAYS);
                    } else {
                        header.getCondtionRemainingDaysHeader().setText(Strings.DATE_OF_DEACTIVTION);

                    }
                    header.getCondtionRemainingDays().setText(items.get(i).date_de);


                }
                break;
                case Item.Type_Service_Desc: {
                    CondtionServiceListItem ite = (CondtionServiceListItem) viewHolder.itemView.getTag();
                    ite.getCondtionServiceListItem().setText((String) items.get(i).data);
                }
                break;

            }


        }

        @Override
        public int getItemViewType(int pos) {
            if (pos == 0) {
                return Header_Type;
            }
            return items.get(pos - 1).type;
        }

        @Override
        public int getItemCount() {
            return items.size() + 1;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        b.putLong("ID_Cond", condtion_id);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            TransitionManager.beginDelayedTransition((ViewGroup) getActivity().findViewById(R.id.root_layout));
        } catch (Exception e) {
        }
    }


    private static class Item {
        public int type;
        @NonNls
        public Object data;

        public String date_ac;
        @NonNls
        public String date_de;


        public static final int Type_Header = 1;
        public static final int Type_Visit = 2;
        public static final int Type_Medication = 3;
        public static final int Type_Divider = 4;
        public static final int Type_Note = 5;
        public static final int Type_Activity = 6;
        public static final int Type_Service = 7;
        public static final int Type_Service_Desc = 8;
        public static final int Type_Medication_Routine = 9;


    }


}
