/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.handler;

import com.health.installer.fund.utils.Globals;

/**
 *
 * @author Inspiron
 */
public class PatientsHandler {

    public static class Patient {

        public String name;
        public String name_ar;
        public String Phone;
        public double height;
        public double weight;
        public long insurance = Globals.Signature;
        public String Address;
        public String pass;
        public String gender;
        public long id;
        public String date;
        public transient boolean installed = false;
    }

    public static Patient createInstant() {
        Patient p = new Patient();
        p.Address = "";
        p.Phone = "";
        p.date = "";
        p.gender = "";
        p.name = "";
        p.pass = "";
        p.id = 0;
        return p;
    }

    public static void savePatient(Patient p) {

        for (int i = 0; i < Globals.patients.size(); i++) {
            if (Globals.patients.get(i).id == p.id) {
                Globals.patients.set(i, p);
                return;
            }
        }

        Globals.patients.add(p);

    }

}
