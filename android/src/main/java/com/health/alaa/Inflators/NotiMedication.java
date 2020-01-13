package com.health.alaa.Inflators;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.health.project.entry.R;

public class NotiMedication {

    private View view;

    private ConstraintLayout containerNotiMedication;
    private TextView notiMedicationNoteHeader;
    private TextView notiMedicationNote;
    private TextView notiMedicationDosageHeader;
    private TextView notiMedicationDosage;

    private TextView notiMedicationNameHeader;
    private TextView notiMedicationName;


    private Button related_condtion_btn;


    private ProgressBar progressBar;

    public boolean isIs_show() {
        return is_show;
    }

    private boolean is_show=false;



    public NotiMedication(LayoutInflater inflater, ViewGroup group) {


        view = inflater.inflate(R.layout.noti_mediaction, group, false);
        bind();
        view.setTag(this);
    }


    public ProgressBar getProgressBar() {
        return progressBar;
    }


    public Button getRelated_condtion_btn() {
        return related_condtion_btn;
    }

    public ConstraintLayout getContainerNotiMedication() {
        return containerNotiMedication;
    }

    public TextView getNotiMedicationNoteHeader() {
        return notiMedicationNoteHeader;
    }

    public TextView getNotiMedicationNote() {
        return notiMedicationNote;
    }

    public TextView getNotiMedicationDosageHeader() {
        return notiMedicationDosageHeader;
    }

    public TextView getNotiMedicationDosage() {
        return notiMedicationDosage;
    }


    public TextView getNotiMedicationNameHeader() {
        return notiMedicationNameHeader;
    }

    public TextView getNotiMedicationName() {
        return notiMedicationName;
    }

    public void bind() {


        containerNotiMedication = (ConstraintLayout) view.findViewById(R.id.container_noti_medication);
        notiMedicationNoteHeader = (TextView) view.findViewById(R.id.noti_medication_note_header);
        notiMedicationNote = (TextView) view.findViewById(R.id.noti_medication_note);
        notiMedicationDosageHeader = (TextView) view.findViewById(R.id.noti_medication_dosage_header);
        notiMedicationDosage = (TextView) view.findViewById(R.id.noti_medication_dosage);

        notiMedicationNameHeader = (TextView) view.findViewById(R.id.noti_medication_name_header);
        notiMedicationName = (TextView) view.findViewById(R.id.noti_medication_name);
        related_condtion_btn = (Button) view.findViewById(R.id.noti_related_condtion);
        progressBar = (ProgressBar) view.findViewById(R.id.noti_med_progress);

    }


    public void show(boolean show_related_condtion) {

        is_show=true;
        notiMedicationNoteHeader.setVisibility(View.VISIBLE);
        notiMedicationNote.setVisibility(View.VISIBLE);
        notiMedicationDosageHeader.setVisibility(View.VISIBLE);
        notiMedicationDosage.setVisibility(View.VISIBLE);

        notiMedicationNameHeader.setVisibility(View.VISIBLE);
        notiMedicationName.setVisibility(View.VISIBLE);
        if(show_related_condtion) {
            related_condtion_btn.setVisibility(View.VISIBLE);
        }else{
            related_condtion_btn.setVisibility(View.GONE);


        }
        progressBar.setVisibility(View.GONE);
    }

    public void hide() {
        is_show=false;
        notiMedicationNoteHeader.setVisibility(View.GONE);
        notiMedicationNote.setVisibility(View.GONE);
        notiMedicationDosageHeader.setVisibility(View.GONE);
        notiMedicationDosage.setVisibility(View.GONE);

        notiMedicationNameHeader.setVisibility(View.GONE);
        notiMedicationName.setVisibility(View.GONE);
        related_condtion_btn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


    }
}
