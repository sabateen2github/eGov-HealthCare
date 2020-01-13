/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alaa.server;

import com.health.objects.GetCondtion;
import com.health.objects.GetNotifications;
import com.health.objects.GetPersonalData;
import com.health.objects.GetProcedure;
import java.util.List;

/**
 *
 * @author Inspiron
 */
public class Patient {

    public long id;
    public String Pass;
    public GetPersonalData.PersonalData personalData;
    public List<GetCondtion.Condtion> activeCondtions;
    public List<GetCondtion.Condtion> historyCondtions;
    public List<GetProcedure.Procedure> activeProcedures;
    public List<GetProcedure.Procedure> historyProcedures;
    public List<GetNotifications.Notification> notifications;
    public String lastTimeUpdated;
    public long last_time_modified;
    public int lang;

}
