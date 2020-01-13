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
import java.util.List;

/**
 *
 * @author Inspiron
 */
public class PriceTagHandler {
    
    public static PriceTag createPriceTagInstance() {
        PriceTag tag = new PriceTag();
        tag.insurances = new ArrayList<>();
        //generate id

        //end
        return tag;
    }
    
    public static InsuranceGroup createInsuranceGroupInstance() {
        InsuranceGroup g = new InsuranceGroup();
        g.insurances = new ArrayList<>();
        return g;
        
    }
    
    public static PriceTag getPriceTag(long id) {
        
        for (PriceTagHandler.PriceTag tag : Globals.pricetags) {
            if (tag.id == id) {
                return tag;
            }
        }
        return null;
    }
    
    public static long savePriceTag(PriceTag t) {
        
        for (PriceTagHandler.PriceTag tag : Globals.pricetags) {
            if (tag.id == t.id || tag.name.equals(t.name)) {
                tag.insurances = t.insurances;
                tag.name = t.name;
                tag.price = t.price;
                return t.id;
            }
        }
        ArrayList<Long> ids = new ArrayList<>();
        for (PriceTagHandler.PriceTag tag : Globals.pricetags) {
            ids.add(tag.id);
        }
        long id = Util.generateID(ids);
        t.id = id;
        Globals.pricetags.add(t);
        return t.id;
    }
    
    public static class PriceTag {
        
        private PriceTag() {
        }
        
        public long id = Globals.Signature;
        public String name;
        public float price;
        public List<InsuranceGroup> insurances;
    }
    
    public static class InsuranceGroup {
        
        private InsuranceGroup() {
        }
        
        public float percentage;
        public long id = Globals.Signature;
        public List<Long> insurances;
    }
    
}
