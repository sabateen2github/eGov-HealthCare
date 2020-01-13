/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alaa.server;

import java.util.List;

/**
 *
 * @author Inspiron
 */
public class MedTeam {

    public long id;
    public String name;
    public String phone;
    public String pass;
    public List<Patient> patients;

    public static class Patient {
        public long patient_id;
        public long procedure_id;
        public long request_id;
    }

}
