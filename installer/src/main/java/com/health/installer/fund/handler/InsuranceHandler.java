/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.health.installer.fund.utils.Globals;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetAvailibleInsurances;
import java.util.ArrayList;

/**
 *
 * @author Inspiron
 */
public class InsuranceHandler {

    public static GetAvailibleInsurances.Insurance createInsurance() {

        GetAvailibleInsurances.Insurance insurance = new GetAvailibleInsurances.Insurance();
        return insurance;
    }

    public static GetAvailibleInsurances.Insurance getInsurance(long id) {

        for (GetAvailibleInsurances.Insurance insurance : Globals.Insurances) {
            if (insurance.insurance_id == id) {
                return insurance;
            }
        }

        return null;
    }

    public static long saveInsurance(GetAvailibleInsurances.Insurance insurance) {
        for (GetAvailibleInsurances.Insurance tag : Globals.Insurances) {
            if (tag.insurance_id == insurance.insurance_id || tag.insurance_path.equals(insurance.insurance_path)) {
                tag.insurance_path = insurance.insurance_path;
                tag.percentage = insurance.percentage;
                return insurance.insurance_id;
            }
        }

        ArrayList<Long> ids = new ArrayList<>();
        for (GetAvailibleInsurances.Insurance tag : Globals.Insurances) {
            ids.add(tag.insurance_id);
        }
        long id = Util.generateID(ids);
        insurance.insurance_id = id;
        Globals.Insurances.add(insurance);
        return id;
    }

}
