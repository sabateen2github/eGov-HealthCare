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
public class AppointmentRoom {

    public long id;
    public String date;
    public String from;
    public String to;
    public List<Patient> Queue;
    public List<Patient> booked;
    public List<Patient> temp_booked;
    public int capacity;
    
    public static class Patient {
        public long patient_id;
        public long procedure_id;
        public long request_id;
        public boolean handeled=false;
    }
}
