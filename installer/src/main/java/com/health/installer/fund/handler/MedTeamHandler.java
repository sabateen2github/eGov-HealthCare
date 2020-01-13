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
public class MedTeamHandler {

    public static class MedTeamData {

        public String name;
        public String Speciality;
        public String Phone;
        public String Address;
        public String pass;
        public String gender;
        public String date;
        public long id;
        public transient boolean installed = false;
    }

    public static MedTeamData createInstant() {

        MedTeamData p = new MedTeamData();
        p.Address = "";
        p.Phone = "";
        p.gender = "";
        p.name = "";
        p.pass = "";
        p.Speciality = "";
        p.id = 0;
        p.date = "";
        return p;
    }

    public static void saveMed(MedTeamData p) {

        for (int i = 0; i < Globals.medteam.size(); i++) {
            if (Globals.medteam.get(i).id == p.id) {
                Globals.medteam.set(i, p);
                return;
            }
        }

        Globals.medteam.add(p);

    }

}
