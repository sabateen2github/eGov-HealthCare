/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.health.installer.fund.handler.InsuranceHandler;
import com.health.installer.fund.handler.MedTeamHandler;
import com.health.installer.fund.handler.PatientsHandler;
import com.health.installer.fund.handler.PriceTagHandler;
import com.health.installer.fund.handler.RoomTagHandler;
import com.health.installer.fund.handler.ServiceHandler;
import com.health.objects.GetAvailibleInsurances;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetCondtion;
import com.health.objects.GetPersonalData;
import com.health.objects.GetProcedure;
import com.health.objects.InstallCondtionsTemplate;
import com.health.objects.InstallInsurances;
import com.health.objects.InstallMedTeamAccounts;
import com.health.objects.InstallMedication;
import com.health.objects.InstallPatientsAccounts;
import com.health.objects.InstallProceduresTemplates;
import com.health.objects.InstallRooms;
import com.health.objects.InstallServices;
import com.health.objects.Reset;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Inspiron
 */
public class Globals {

    public static WeakReference<JFrame> Msg;

    public static final long Signature = -1999628l;

    @Deprecated
    public static final String Insurance_Path = "./Insurance/";
    @Deprecated
    public static final String Insurance_IDs = "./Insurance/ids.id";
    @Deprecated
    public static final String Service_Path = "./Services/";
    @Deprecated
    public static final String Service_IDs = "./Services/ids.id";
    @Deprecated
    public static final String Medication_Path = "./Medication/";
    @Deprecated
    public static final String Medication_IDs = "./Medications/ids.id";
    @Deprecated
    public static final String Room_Path = "./Rooms/";
    @Deprecated
    public static final String Room_IDs = "./Rooms/ids.id";

    public static List<GetAvailibleInsurances.Insurance> Insurances = new ArrayList<>();
    public static List<GetCondtion.Medication> Medications = new ArrayList<>();
    public static List<GetAvailibleServices.Service> Services = new ArrayList<>();
    public static List<GetAvailibleRooms.Room> Rooms = new ArrayList<>();
    public static List<PriceTagHandler.PriceTag> pricetags = new ArrayList<>();
    public static List<RoomTagHandler.RoomTag> roomtags = new ArrayList<>();
    public static List<GetCondtion.Condtion> condtions = new ArrayList<>();
    public static List<GetProcedure.Procedure> templateProc = new ArrayList<>();
    public static List<PatientsHandler.Patient> patients = new ArrayList<>();
    public static List<MedTeamHandler.MedTeamData> medteam = new ArrayList<>();

    private static class Save {

        public List<GetAvailibleInsurances.Insurance> Insurances;
        public List<GetCondtion.Medication> Medications;
        public List<GetAvailibleServices.Service> Services;
        public List<GetAvailibleRooms.Room> Rooms;
        public List<PriceTagHandler.PriceTag> pricetags;
        public List<RoomTagHandler.RoomTag> roomtags;
        public List<GetCondtion.Condtion> condtions;
        public List<GetProcedure.Procedure> templateProc;
        public List<PatientsHandler.Patient> patients;
        public List<MedTeamHandler.MedTeamData> medteam;
    }

    public static boolean init() {

        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();
        int i1 = 0;
        File f = new File("./Data/");
        String[] d1 = f.list();
        if (d1 != null) {
            i1 = d1.length - 1;
        }

        RandomAccessFile file = Util.getFile("rw", "./Data/hello" + i1 + ".ti");
        try {

            int size = file.readInt();
            byte[] data = new byte[size];
            file.read(data);
            String ins = new String(data);

            Save s = gson.fromJson(ins, Save.class);
            file.close();
            Insurances = s.Insurances;
            Medications = s.Medications;
            Services = s.Services;
            Rooms = s.Rooms;
            pricetags = s.pricetags;
            roomtags = s.roomtags;
            condtions = s.condtions;
            templateProc = s.templateProc;
            patients = s.patients;
            medteam = s.medteam;

            for (int i = 0; i < Medications.size(); i++) {

                PriceTagHandler.PriceTag tag = Medications.get(i).priceTag;
                if (tag != null) {
                    for (int x = 0; x < pricetags.size(); x++) {
                        if (pricetags.get(x).id == tag.id) {
                            Medications.get(i).priceTag = pricetags.get(x);
                        }
                    }
                }

            }

            for (int i = 0; i < Services.size(); i++) {
                PriceTagHandler.PriceTag tag = Services.get(i).pricetag;
                if (tag != null) {

                    for (int x = 0; x < pricetags.size(); x++) {
                        if (pricetags.get(x).id == tag.id) {
                            Services.get(i).pricetag = pricetags.get(x);
                        }

                    }
                }
            }
            for (int i = 0; i < Rooms.size(); i++) {
                RoomTagHandler.RoomTag tag = Rooms.get(i).roomtag;
                Rooms.get(i).services.clear();
                if (tag != null) {
                    for (int x = 0; x < roomtags.size(); x++) {
                        if (roomtags.get(x).id == tag.id) {
                            Rooms.get(i).roomtag = roomtags.get(x);
                        }
                    }
                    for (int y = 0; y < tag.services.size(); y++) {
                        Rooms.get(i).services.add(ServiceHandler.getService(tag.services.get(y)));
                    }
                }
            }

            for (int i = 0; i < templateProc.size(); i++) {

                GetProcedure.Procedure pr = templateProc.get(i);
                for (int x = 0; x < pr.smart_feedbacks.size(); x++) {
                    GetProcedure.SmartFeedBack d = pr.smart_feedbacks.get(x);
                    for (int y = 0; y < d.services.size(); y++) {
                        for (int u = 0; u < Services.size(); u++) {
                            if (Services.get(u).service_id == d.services.get(y).service_id) {
                                d.services.set(y, Services.get(u));
                            }
                        }
                    }
                }

                for (int x = 0; x < pr.services.size(); x++) {
                    GetProcedure.ServicesGroup d = pr.services.get(x);
                    for (int y = 0; y < d.services.size(); y++) {
                        for (int u = 0; u < Services.size(); u++) {
                            if (Services.get(u).service_id == d.services.get(y).service_id) {
                                d.services.set(y, Services.get(u));
                            }
                        }
                    }
                    for (int y = 0; y < d.rooms.size(); y++) {
                        for (int u = 0; u < Rooms.size(); u++) {
                            if (Rooms.get(u).room_id == d.rooms.get(y).room_id) {
                                d.rooms.set(y, Rooms.get(u));
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < condtions.size(); i++) {

                GetCondtion.Condtion cond = condtions.get(i);
                for (int y = 0; y < cond.medication_ordinary.size(); y++) {
                    for (int u = 0; u < Medications.size(); u++) {
                        if (Medications.get(u).medication_id == cond.medication_ordinary.get(y).medication.medication_id) {
                            cond.medication_ordinary.get(y).medication = Medications.get(u);
                        }
                    }
                }

                for (int y = 0; y < cond.medications_routine.size(); y++) {
                    for (int u = 0; u < Medications.size(); u++) {
                        if (Medications.get(u).medication_id == cond.medications_routine.get(y).medication.medication_id) {
                            cond.medications_routine.get(y).medication = Medications.get(u);
                        }
                    }
                }
                for (int y1 = 0; y1 < cond.attached_procedures.size(); y1++) {

                    GetProcedure.Procedure pr = cond.attached_procedures.get(y1);
                    for (int x = 0; x < pr.smart_feedbacks.size(); x++) {

                        GetProcedure.SmartFeedBack d = pr.smart_feedbacks.get(x);
                        for (int y = 0; y < d.services.size(); y++) {
                            for (int u = 0; u < Services.size(); u++) {
                                if (Services.get(u).service_id == d.services.get(y).service_id) {
                                    d.services.set(y, Services.get(u));
                                }
                            }
                        }
                    }

                    for (int x = 0; x < pr.services.size(); x++) {
                        GetProcedure.ServicesGroup d = pr.services.get(x);
                        for (int y = 0; y < d.services.size(); y++) {
                            for (int u = 0; u < Services.size(); u++) {
                                if (Services.get(u).service_id == d.services.get(y).service_id) {
                                    d.services.set(y, Services.get(u));
                                }
                            }
                        }

                        for (int y = 0; y < d.rooms.size(); y++) {
                            for (int u = 0; u < Rooms.size(); u++) {
                                if (Rooms.get(u).room_id == d.rooms.get(y).room_id) {
                                    d.rooms.set(y, Rooms.get(u));
                                }
                            }
                        }
                    }
                }
                for (int y1 = 0; y1 < cond.procedures.size(); y1++) {

                    GetProcedure.Procedure pr = cond.procedures.get(y1);
                    for (int x = 0; x < pr.smart_feedbacks.size(); x++) {

                        GetProcedure.SmartFeedBack d = pr.smart_feedbacks.get(x);
                        for (int y = 0; y < d.services.size(); y++) {
                            for (int u = 0; u < Services.size(); u++) {
                                if (Services.get(u).service_id == d.services.get(y).service_id) {
                                    d.services.set(y, Services.get(u));
                                }
                            }
                        }
                    }

                    for (int x = 0; x < pr.services.size(); x++) {
                        GetProcedure.ServicesGroup d = pr.services.get(x);
                        for (int y = 0; y < d.services.size(); y++) {
                            for (int u = 0; u < Services.size(); u++) {
                                if (Services.get(u).service_id == d.services.get(y).service_id) {
                                    d.services.set(y, Services.get(u));
                                }
                            }
                        }

                        for (int y = 0; y < d.rooms.size(); y++) {
                            for (int u = 0; u < Rooms.size(); u++) {
                                if (Rooms.get(u).room_id == d.rooms.get(y).room_id) {
                                    d.rooms.set(y, Rooms.get(u));
                                }
                            }
                        }
                    }
                }
            }

            /*
            for (int i = 0; i < condtions.size(); i++) {
                for (int x = 0; x < condtions.get(i).procedures.size(); x++) {

                    System.out.println(condtions.get(i).procedures.get(x).name + "-------- name");
                    for (int u = 0; u < condtions.get(i).procedures.get(x).services.size(); u++) {
                        System.out.println(condtions.get(i).procedures.get(x).services.get(u).rooms + " Rooms");
                        System.out.println(condtions.get(i).procedures.get(x).services.get(u).services + " Services");

                    }
                }
            }
             */
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            try {
                file.close();
            } catch (IOException ex1) {
                Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
        return false;
    }

    public static boolean save() {
        Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC).create();

        Save s = new Save();
        s.Insurances = Insurances;
        s.Medications = Medications;
        s.Rooms = Rooms;
        s.Services = Services;
        s.pricetags = pricetags;
        s.roomtags = roomtags;
        s.condtions = condtions;
        s.templateProc = templateProc;
        s.patients = patients;
        s.medteam = medteam;

        int i = 0;
        File f = new File("./Data/");
        /*
        String[] d = f.list();
        if (d != null) {
            i = d.length;
        }*/

        String ins = gson.toJson(s);
        System.out.println(ins);
        RandomAccessFile file = Util.getFile("rw", "./Data/hello" + i + ".ti");
        try {
            file.setLength(0);

            byte[] data = ins.getBytes();
            int size = data.length;
            file.writeInt(size);
            file.write(data);
            file.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex);
            try {
                file.close();
            } catch (IOException ex1) {
                Logger.getLogger(Globals.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return false;
    }

    public static boolean install() {

        resetInstallation();
        if (!Reset.reset()) {
            System.out.println("failed");
            return false;
        }

        try {
            //install Insurances
            ArrayList<GetAvailibleInsurances.Insurance> installed = new ArrayList<>();
            ArrayList<GetAvailibleInsurances.Insurance> newInsurance = new ArrayList<>();

            for (int i = 0; i < Insurances.size(); i++) {
                if (Insurances.get(i).installed) {
                    installed.add(Insurances.get(i));
                } else {
                    newInsurance.add(Insurances.get(i));
                }
            }
            InstallInsurances.InstallInsurances(installed);

            ArrayList<Long> old = new ArrayList<>();
            for (int i = 0; i < newInsurance.size(); i++) {
                old.add(newInsurance.get(i).insurance_id);
                newInsurance.get(i).insurance_id = Globals.Signature;
            }

            List<Long> newIds = InstallInsurances.InstallInsurances(newInsurance);

            for (int i = 0; i < newInsurance.size(); i++) {
                newInsurance.get(i).installed = true;
                newInsurance.get(i).insurance_id = newIds.get(i);

                for (int x = 0; x < pricetags.size(); x++) {
                    PriceTagHandler.PriceTag tag = pricetags.get(x);
                    for (int y = 0; y < tag.insurances.size(); y++) {
                        PriceTagHandler.InsuranceGroup g = tag.insurances.get(y);
                        for (int c = 0; c < g.insurances.size(); c++) {
                            int idx = old.indexOf(g.insurances.get(c));
                            if (idx != -1) {
                                g.insurances.set(c, newIds.get(idx));
                            }
                        }
                    }
                }

                for (int x = 0; x < patients.size(); x++) {
                    int idx = old.indexOf(patients.get(x).insurance);

                    if (idx != -1) {
                        patients.get(x).insurance = newIds.get(idx);
                    }
                }
            }

            save();

            //Medications
            ArrayList<GetCondtion.Medication> installedMed = new ArrayList<>();
            ArrayList<GetCondtion.Medication> newMed = new ArrayList<>();

            for (int i = 0; i < Medications.size(); i++) {
                if (Medications.get(i).installed) {
                    installedMed.add(Medications.get(i));
                } else {
                    newMed.add(Medications.get(i));
                }
                Medications.get(i).price = Medications.get(i).priceTag.price;
                for (int y = 0; y < Medications.get(i).priceTag.insurances.size(); y++) {
                    PriceTagHandler.InsuranceGroup g = Medications.get(i).priceTag.insurances.get(y);
                    for (int u = 0; u < g.insurances.size(); u++) {
                        GetAvailibleInsurances.Insurance in = InsuranceHandler.getInsurance(g.insurances.get(u));
                        GetAvailibleInsurances.Insurance in2 = InsuranceHandler.createInsurance();
                        in2.insurance_id = in.insurance_id;
                        in2.insurance_path = in.insurance_path;
                        in2.percentage = g.percentage;
                        Medications.get(i).insurances.add(in2);
                    }
                }
            }

            InstallMedication.InstallMedication(installedMed);
            old.clear();

            for (int i = 0; i < newMed.size(); i++) {
                old.add(newMed.get(i).medication_id);
                newMed.get(i).medication_id = Globals.Signature;
            }
            newIds = InstallMedication.InstallMedication(newMed);
            for (int i = 0; i < newMed.size(); i++) {
                newMed.get(i).installed = true;
                newMed.get(i).medication_id = newIds.get(i);
            }

            save();

            //Services
            ArrayList<GetAvailibleServices.Service> installedSer = new ArrayList<>();
            ArrayList<GetAvailibleServices.Service> newSer = new ArrayList<>();

            for (int i = 0; i < Services.size(); i++) {
                if (Services.get(i).installed) {
                    installedSer.add(Services.get(i));
                } else {
                    newSer.add(Services.get(i));
                }

                Services.get(i).service_price = Services.get(i).pricetag.price;
                for (int y = 0; y < Services.get(i).pricetag.insurances.size(); y++) {
                    PriceTagHandler.InsuranceGroup g = Services.get(i).pricetag.insurances.get(y);
                    for (int u = 0; u < g.insurances.size(); u++) {
                        GetAvailibleInsurances.Insurance in = InsuranceHandler.getInsurance(g.insurances.get(u));
                        GetAvailibleInsurances.Insurance in2 = InsuranceHandler.createInsurance();
                        in2.insurance_id = in.insurance_id;
                        in2.insurance_path = in.insurance_path;
                        in2.percentage = g.percentage;
                        Services.get(i).insurances.add(in2);
                    }
                }
            }
            InstallServices.InstallServices(installedSer);
            old.clear();

            for (int i = 0; i < newSer.size(); i++) {
                old.add(newSer.get(i).service_id);
                newSer.get(i).service_id = Globals.Signature;
            }
            newIds = InstallServices.InstallServices(newSer);
            for (int i = 0; i < newSer.size(); i++) {
                newSer.get(i).installed = true;
                newSer.get(i).service_id = newIds.get(i);

                for (int y = 0; y < roomtags.size(); y++) {
                    RoomTagHandler.RoomTag tag = roomtags.get(y);
                    int idx = tag.services.indexOf(old.get(i));
                    if (idx != -1) {
                        tag.services.set(idx, newIds.get(i));
                    }
                }
            }

            save();

            //Rooms
            ArrayList<GetAvailibleRooms.Room> installedRoom = new ArrayList<>();
            ArrayList<GetAvailibleRooms.Room> newRoom = new ArrayList<>();

            for (int i = 0; i < Rooms.size(); i++) {
                if (Rooms.get(i).installed) {
                    installedRoom.add(Rooms.get(i));
                } else {
                    newRoom.add(Rooms.get(i));
                }
                if (Rooms.get(i).roomtag != null) {
                    for (int x = 0; x < Rooms.get(i).roomtag.services.size(); x++) {
                        Rooms.get(i).services.add(translateService(ServiceHandler.getService(Rooms.get(i).roomtag.services.get(x))));
                    }
                }
            }
            InstallRooms.InstallProceduresTemplates(installedRoom);
            old.clear();

            for (int i = 0; i < newRoom.size(); i++) {
                old.add(newRoom.get(i).room_id);
                newRoom.get(i).room_id = Globals.Signature;
            }
            newIds = InstallRooms.InstallProceduresTemplates(newRoom);
            for (int i = 0; i < newRoom.size(); i++) {
                newRoom.get(i).installed = true;
                newRoom.get(i).room_id = newIds.get(i);

                for (int y = 0; y < roomtags.size(); y++) {
                    RoomTagHandler.RoomTag tag = roomtags.get(y);
                    int idx = tag.rooms.indexOf(old.get(i));
                    if (idx != -1) {
                        tag.rooms.set(idx, newIds.get(i));
                    }
                }
            }
            save();

            //Procedures
            List<GetProcedure.Procedure> newProc = new ArrayList<>();
            for (int i = 0; i < templateProc.size(); i++) {
                if (!templateProc.get(i).installed) {
                    newProc.add(templateProc.get(i));
                }
            }

            if (InstallProceduresTemplates.InstallProceduresTemplates(newProc)) {
                for (int i = 0; i < templateProc.size(); i++) {
                    if (!templateProc.get(i).installed) {
                        templateProc.get(i).installed = true;
                    }
                }
            } else {
                return false;
            }

            save();

            //Condtions
            List<GetCondtion.Condtion> newCond = new ArrayList<>();
            for (int i = 0; i < condtions.size(); i++) {
                if (!condtions.get(i).installed) {
                    newCond.add(condtions.get(i));
                }
            }

            if (InstallCondtionsTemplate.InstallCondtionsTemplate(newCond)) {

                for (int i = 0; i < condtions.size(); i++) {
                    if (!condtions.get(i).installed) {
                        condtions.get(i).installed = true;
                    }
                }
            } else {

                return false;
            }

            save();

            //MedTeam
            List<InstallMedTeamAccounts.MedTeam> newMedTeam = new ArrayList<>();
            List<Long> ids = new ArrayList<>();
            for (int i = 0; i < medteam.size(); i++) {
                if (!medteam.get(i).installed) {

                    InstallMedTeamAccounts.MedTeam m = new InstallMedTeamAccounts.MedTeam();
                    ids.add(medteam.get(i).id);
                    m.name = medteam.get(i).name;
                    m.pass = medteam.get(i).pass;
                    m.phone = medteam.get(i).Phone;
                    newMedTeam.add(m);
                }
            }

            if (InstallMedTeamAccounts.InstallMedTeamAccounts(newMedTeam, ids)) {
                for (int i = 0; i < medteam.size(); i++) {
                    if (!medteam.get(i).installed) {
                        medteam.get(i).installed = true;
                    }
                }
            } else {
                return false;
            }

            save();

            //Patients
            List<GetPersonalData.PersonalData> newPersonal = new ArrayList<>();
            List<String> pass = new ArrayList<>();
            List<Long> natIDs = new ArrayList<>();
            for (int i = 0; i < patients.size(); i++) {
                if (!patients.get(i).installed) {

                    pass.add(patients.get(i).pass);
                    natIDs.add(patients.get(i).id);
                    GetPersonalData.PersonalData m = new GetPersonalData.PersonalData();
                    m.name = patients.get(i).name;
                    m.name_ar = patients.get(i).name_ar;
                    m.height = (float) patients.get(i).height;
                    m.weight = (float) patients.get(i).weight;
                    m.phone = patients.get(i).Phone;
                    m.gender = patients.get(i).gender;
                    m.insurance_id = patients.get(i).insurance;
                    m.insurance_path = InsuranceHandler.getInsurance(patients.get(i).insurance).insurance_path;
                    m.date_of_birth = patients.get(i).date;
                    m.address = patients.get(i).Address;
                    newPersonal.add(m);
                }
            }

            if (InstallPatientsAccounts.InstallPatientsAccounts(newPersonal, natIDs, pass)) {
                for (int i = 0; i < patients.size(); i++) {
                    if (!patients.get(i).installed) {
                        patients.get(i).installed = true;
                    }
                }
            } else {
                return false;
            }

            for (int i = 0; i < Medications.size(); i++) {
                Medications.get(i).insurances.clear();
            }
            for (int i = 0; i < Services.size(); i++) {
                Services.get(i).insurances.clear();
            }
            for (int i = 0; i < Rooms.size(); i++) {
                Rooms.get(i).services.clear();
            }
            save();

            Util.showMsg("Installed successfully");

            return true;

        } catch (Exception e) {
            e.printStackTrace();;
            return false;
        }

    }

    public static void SaveWithUi() {

        save();
        Util.showMsg("Saved successfully");

    }

    /*
        generate insurance and stuff as new Instance
    
     */
    private static GetAvailibleServices.Service translateService(GetAvailibleServices.Service service) {
        GetAvailibleServices.Service s = ServiceHandler.createService();
        s.installed = service.installed;
        s.pricetag = service.pricetag;
        s.service_id = service.service_id;
        s.service_path = service.service_path;
        s.service_price = service.service_price;

        s.service_price = s.pricetag.price;
        for (int y = 0; y < s.pricetag.insurances.size(); y++) {
            PriceTagHandler.InsuranceGroup g = s.pricetag.insurances.get(y);
            for (int u = 0; u < g.insurances.size(); u++) {
                GetAvailibleInsurances.Insurance in = InsuranceHandler.getInsurance(g.insurances.get(u));
                GetAvailibleInsurances.Insurance in2 = InsuranceHandler.createInsurance();
                in2.insurance_id = in.insurance_id;
                in2.insurance_path = in.insurance_path;
                in2.percentage = g.percentage;
                s.insurances.add(in2);
            }
        }
        return s;
    }

    public static void resetInstallation() {

        for (int i = 0; i < Rooms.size(); i++) {
            Rooms.get(i).installed = false;
        }
        for (int i = 0; i < Services.size(); i++) {
            Services.get(i).installed = false;
        }
        for (int i = 0; i < Medications.size(); i++) {
            Medications.get(i).installed = false;
        }
        for (int i = 0; i < Insurances.size(); i++) {
            Insurances.get(i).installed = false;
        }

        for (int i = 0; i < condtions.size(); i++) {
            condtions.get(i).installed = false;
        }

        for (int i = 0; i < templateProc.size(); i++) {
            templateProc.get(i).installed = false;
        }

        for (int i = 0; i < patients.size(); i++) {
            patients.get(i).installed = false;
        }

        for (int i = 0; i < medteam.size(); i++) {
            medteam.get(i).installed = false;
        }
        save();

    }

    public static long generateRequestId(GetProcedure.Procedure proc) {

        ArrayList<Long> ids = new ArrayList<>();

        for (GetProcedure.FeedBack f : proc.feedbacks) {

            ids.add(f.req_id);
        }

        for (GetProcedure.SmartFeedBack f : proc.smart_feedbacks) {
            ids.add(f.req_id);

        }

        for (GetProcedure.ServicesGroup f : proc.services) {
            ids.add(f.req_id);

        }

        long id = (long) (Math.random() * Long.MAX_VALUE);

        while (ids.contains(id)) {
            id = (long) (Math.random() * Long.MAX_VALUE);
        }

        return id;
    }

    public static long generateActivityId(GetCondtion.Condtion proc) {

        ArrayList<Long> ids = new ArrayList<>();

        for (GetCondtion.Activity f : proc.activities) {

            ids.add(f.activity_id);
        }

        long id = (long) (Math.random() * Long.MAX_VALUE);

        while (ids.contains(id)) {
            id = (long) (Math.random() * Long.MAX_VALUE);
        }

        return id;
    }

}
