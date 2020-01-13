/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.project.medteam;

import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import com.health.objects.Types;
import java.util.ArrayList;
import java.util.List;
import net.ricecode.similarity.LevenshteinDistanceStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

/**
 *
 * @author Inspiron
 */
public class Util {

    public static boolean checkProcedure(GetProcedure.Procedure proc) {

        if (proc.name.length() == 0
                || proc.activation_period <= 0 || proc.objective.length() <= 0
                || proc.delay_period < 0 || (proc.type == Types.Type_Procedure_Condtion_Routine && proc.cycle_period < 0)) {
            return false;
        }

        for (int i = 0; i < proc.feedbacks.size(); i++) {
            if (proc.feedbacks.get(i).med_team_id == 0) {
                Globals.showMsg("Please Enter All Fields Correctly in the FeedBack Order : #" + proc.smart_feedbacks.get(i).order);
                return false;
            }
        }

        for (int i = 0; i < proc.smart_feedbacks.size(); i++) {
            if (proc.smart_feedbacks.get(i).services == null || proc.smart_feedbacks.get(i).services.size() == 0) {
                Globals.showMsg("Please Enter All Fields Correctly in the Smart_FeedBack Order : #" + proc.smart_feedbacks.get(i).order);
                return false;
            }
        }

        for (int i = 0; i < proc.services.size(); i++) {

            if (proc.services.get(i).services == null || proc.services.get(i).services.size() == 0) {
                Globals.showMsg("Please Enter Services That you want to acheive during the visit Service Order : #" + proc.services.get(i).order);
                return false;
            }

            if (proc.services.get(i).rooms == null || proc.services.get(i).rooms.size() == 0) {
                Globals.showMsg("Please Enter The Rooms That You want the patient to choose from to acheive the visit Service Order : #" + proc.services.get(i).order);
                return false;
            }

            if (proc.services.get(i).count == 0) {
                Globals.showMsg("Please Enter the Quantity of the Services (eg. (2) for two x-rays images Or (1) if it is just an ordinary visit to an ordinary clinic)  Service Order : #" + proc.services.get(i).order);
                return false;
            }

            proc.services.get(i).appointment = new GetProcedure.Appointment();
            proc.services.get(i).appointment.state = Types.Appointment_Invoice_Not_paid;

        }

        if (proc.name == null || proc.name.length() == 0) {
            Globals.showMsg("Please enter all fields correctly");
            return false;
        }
        if (proc.note_med == null || proc.note_med.length() == 0) {
            Globals.showMsg("Please enter all fields correctly");

            return false;
        }
        if (proc.note_patient == null || proc.note_patient.length() == 0) {
            Globals.showMsg("Please enter all fields correctly");

            return false;
        }
        if (proc.objective == null || proc.objective.length() == 0) {
            Globals.showMsg("Please enter all fields correctly");

            return false;
        }

        if (proc.activation_period < 1) {
            Globals.showMsg("Please enter all fields correctly");

            return false;
        }

        for (int i = 0; i < proc.services.size(); i++) {
            if (proc.services.get(i).count < 1) {
                Globals.showMsg("Please enter all fields correctly");

                return false;
            }
            if (proc.services.get(i).services.size() < 1 || proc.services.get(i).rooms.size() < 1) {
                Globals.showMsg("Please enter all fields correctly");

                return false;
            }
            if (proc.services.get(i).note_med == null || proc.services.get(i).note_med.length() == 0) {
                Globals.showMsg("Please enter all fields correctly");

                return false;
            }
        }

        for (int i = 0; i < proc.smart_feedbacks.size(); i++) {
            if (proc.smart_feedbacks.get(i).services.size() < 1) {
                Globals.showMsg("Please enter all fields correctly");

                return false;
            }
            if (proc.smart_feedbacks.get(i).note_med == null || proc.smart_feedbacks.get(i).note_med.length() == 0) {
                Globals.showMsg("Please enter all fields correctly");

                return false;
            }
        }

        if (proc.type == Types.Type_Template || proc.type == Types.Type_Template_User) {
            proc.feedbacks.clear();
        } else if (proc.type == Types.Type_Procedure_Condtion || proc.type == Types.Type_Procedure_Independent) {
            proc.cycle_period = 1;
            proc.routine_activation_period = 1;
        } else if (proc.cycle_period < 1) {
            Globals.showMsg("Please enter all fields correctly");
            return false;
        }

        return true;

    }

    public static List<GetCondtion.Medication> getSortedMedications(String token) {

        ArrayList<GetCondtion.Medication> meds = new ArrayList<>();
        ArrayList<Double> d = new ArrayList<>();
        for (GetCondtion.Medication med : Globals.medications) {

            SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
            StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
            double score = service.score(token, med.medication_name); // Score is 0.90

            meds.add(med);
            d.add(score);
        }

        for (int i = 0; i < d.size(); i++) {

            int idx = i;
            for (int x = i + 1; x < d.size(); x++) {
                if (d.get(x) > d.get(i)) {
                    idx = x;
                }
            }
            if (idx != i) {
                Object tmp = meds.get(idx);
                double temp_d = d.get(idx);
                meds.set(idx, meds.get(i));
                d.set(idx, d.get(i));
                meds.set(i, (GetCondtion.Medication) tmp);
                d.set(i, temp_d);
            }

        }

        return meds;

    }

}
