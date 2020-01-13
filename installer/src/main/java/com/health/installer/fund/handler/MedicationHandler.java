/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.health.installer.fund.utils.Globals;
import static com.health.installer.fund.utils.Globals.Medications;
import static com.health.installer.fund.utils.Globals.pricetags;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetCondtion;
import java.util.ArrayList;

/**
 *
 * @author Inspiron
 */
public class MedicationHandler {

    public static GetCondtion.Medication createMedication() {

        GetCondtion.Medication med = new GetCondtion.Medication();
        med.medication_name = "";
        med.prescription_unit = "";
        med.route_type = "";
        med.insurances = new ArrayList();
        med.medication_id = Globals.Signature;

        return med;

    }

    public static GetCondtion.Medication getMedication(long id) {

        for (GetCondtion.Medication medication : Globals.Medications) {
            if (medication.medication_id == id) {
                return medication;
            }
        }

        return null;

    }

    public static long saveMedication(GetCondtion.Medication medication) {

        for (GetCondtion.Medication med : Globals.Medications) {
            if (med.medication_id == medication.medication_id || medication.medication_name.equals(med.medication_name)) {
                med.medication_name = medication.medication_name;
                med.prescription_unit = medication.prescription_unit;
                med.route_type = medication.route_type;
                return med.medication_id;
            }
        }

        ArrayList<Long> ids = new ArrayList<>();
        for (GetCondtion.Medication med : Globals.Medications) {
            ids.add(med.medication_id);
        }
        long id = Util.generateID(ids);
        medication.medication_id = id;
        Globals.Medications.add(medication);

        PriceTagHandler.PriceTag tag = medication.priceTag;
        if (tag != null) {
            for (int x = 0; x < pricetags.size(); x++) {
                if (pricetags.get(x).id == tag.id) {
                    medication.priceTag = pricetags.get(x);
                }
            }
        }
        return id;
    }

}
