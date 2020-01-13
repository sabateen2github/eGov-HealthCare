/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alaa.shit;

import com.health.installer.fund.utils.Globals;
import com.health.objects.GetCondtion;
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
public class Methods {

    public static List<GetCondtion.Medication> getSortedMedications(String value) {
        ArrayList<GetCondtion.Medication> meds = new ArrayList<>();
        ArrayList<Double> d = new ArrayList<>();
        for (GetCondtion.Medication med : Globals.Medications) {

            SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
            StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
            double score = service.score(value, med.medication_name); // Score is 0.90

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
