package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health.project.entry.R;

public class CondtionMedicationItem {

    private View view;


    private ConstraintLayout containerMedication;
    private TextView condtionMedTitle;
    private LinearLayout condtionMedicationDetails;
    private TextView medicationNameHeader;
    private TextView medicationName;
    private TextView medicationFormHeader;
    private TextView medicationForm;
    private TextView medicationAbuseTypeHeader;
    private TextView medicationAbuseType;
    private TextView medicationIndicationHeader;
    private TextView medicationIndication;
    private TextView medicationNumberDosageHeader;
    private TextView medicationNumberDosage;
    private TextView medicationDosageHeader;
    private TextView medicationDosage;
    private TextView medicationActiveHeader;
    private TextView medicationActive;
    private TextView medicationDeactiveHeader;
    private TextView medicationDeactive;
    private TextView medicationNoteHeader;
    private TextView medicationNote;

    public CondtionMedicationItem(LayoutInflater inflater, ViewGroup parent) {
        this.view = inflater.inflate(R.layout.condtion_medication_item,parent,false);
        view.setTag(this);
        bind();
    }

    public ConstraintLayout getContainerMedication() {
        return containerMedication;
    }

    public TextView getCondtionMedTitle() {
        return condtionMedTitle;
    }

    public LinearLayout getCondtionMedicationDetails() {
        return condtionMedicationDetails;
    }

    public TextView getMedicationNameHeader() {
        return medicationNameHeader;
    }

    public TextView getMedicationName() {
        return medicationName;
    }

    public TextView getMedicationFormHeader() {
        return medicationFormHeader;
    }

    public TextView getMedicationForm() {
        return medicationForm;
    }

    public TextView getMedicationAbuseTypeHeader() {
        return medicationAbuseTypeHeader;
    }

    public TextView getMedicationAbuseType() {
        return medicationAbuseType;
    }

    public TextView getMedicationIndicationHeader() {
        return medicationIndicationHeader;
    }

    public TextView getMedicationIndication() {
        return medicationIndication;
    }

    public TextView getMedicationNumberDosageHeader() {
        return medicationNumberDosageHeader;
    }

    public TextView getMedicationNumberDosage() {
        return medicationNumberDosage;
    }

    public TextView getMedicationDosageHeader() {
        return medicationDosageHeader;
    }

    public TextView getMedicationDosage() {
        return medicationDosage;
    }

    public TextView getMedicationActiveHeader() {
        return medicationActiveHeader;
    }

    public TextView getMedicationActive() {
        return medicationActive;
    }

    public TextView getMedicationDeactiveHeader() {
        return medicationDeactiveHeader;
    }

    public TextView getMedicationDeactive() {
        return medicationDeactive;
    }

    public TextView getMedicationNoteHeader() {
        return medicationNoteHeader;
    }

    public TextView getMedicationNote() {
        return medicationNote;
    }

    public void bind()
    {

        containerMedication = (ConstraintLayout) view.findViewById(R.id.container_medication);
        condtionMedTitle = (TextView) view.findViewById(R.id.condtion_med_title);
        condtionMedicationDetails = (LinearLayout) view.findViewById(R.id.condtion_medication_details);
        medicationNameHeader = (TextView) view.findViewById(R.id.medication_name_header);
        medicationName = (TextView) view.findViewById(R.id.medication_name);
        medicationFormHeader = (TextView) view.findViewById(R.id.medication_form_header);
        medicationForm = (TextView) view.findViewById(R.id.medication_form);
        medicationAbuseTypeHeader = (TextView) view.findViewById(R.id.medication_abuse_type_header);
        medicationAbuseType = (TextView) view.findViewById(R.id.medication_abuse_type);
        medicationIndicationHeader = (TextView) view.findViewById(R.id.medication_indication_header);
        medicationIndication = (TextView) view.findViewById(R.id.medication_indication);
        medicationNumberDosageHeader = (TextView) view.findViewById(R.id.medication_number_dosage_header);
        medicationNumberDosage = (TextView) view.findViewById(R.id.medication_number_dosage);
        medicationDosageHeader = (TextView) view.findViewById(R.id.medication_dosage_header);
        medicationDosage = (TextView) view.findViewById(R.id.medication_dosage);
        medicationActiveHeader = (TextView) view.findViewById(R.id.medication_active_header);
        medicationActive = (TextView) view.findViewById(R.id.medication_active);
        medicationDeactiveHeader = (TextView) view.findViewById(R.id.medication_deactive_header);
        medicationDeactive = (TextView) view.findViewById(R.id.medication_deactive);
        medicationNoteHeader = (TextView) view.findViewById(R.id.medication_note_header);
        medicationNote = (TextView) view.findViewById(R.id.medication_note);

    }
}
