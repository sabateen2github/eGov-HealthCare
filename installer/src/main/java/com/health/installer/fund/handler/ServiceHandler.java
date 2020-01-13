/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.health.installer.fund.utils.Globals;
import static com.health.installer.fund.utils.Globals.Services;
import static com.health.installer.fund.utils.Globals.pricetags;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetAvailibleServices;
import java.util.ArrayList;

/**
 *
 * @author Inspiron
 */
public class ServiceHandler {

    public static GetAvailibleServices.Service createService() {

        GetAvailibleServices.Service service = new GetAvailibleServices.Service();
        service.insurances = new ArrayList<>();
        return service;
    }

    public static GetAvailibleServices.Service getService(long id) {

        for (GetAvailibleServices.Service service : Globals.Services) {

            if (service.service_id == id) {
                return service;
            }
        }
        return null;
    }

    public static long save(GetAvailibleServices.Service ser) {

        for (GetAvailibleServices.Service service : Globals.Services) {
            if (service.service_id == ser.service_id || service.service_path.equals(ser.service_path)) {
                service.insurances = ser.insurances;
                service.service_path = ser.service_path;
                service.service_price = ser.service_price;
                return ser.service_id;
            }
        }
        ArrayList<Long> ids = new ArrayList<>();
        for (GetAvailibleServices.Service service : Globals.Services) {
            ids.add(service.service_id);
        }
        long id = Util.generateID(ids);
        ser.service_id = id;
        Globals.Services.add(ser);

        PriceTagHandler.PriceTag tag = ser.pricetag;
        if (tag != null) {

            for (int x = 0; x < pricetags.size(); x++) {
                if (pricetags.get(x).id == tag.id) {
                    ser.pricetag = pricetags.get(x);
                }

            }
        }
        return id;
    }

}
