package com.health.Data;

import com.health.fragments.BookingFragment;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.project.entry.Util;

import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Procedures {


    public static final int Type_Note = 1;
    public static final int Type_Visit = 2;
    public static final int Type_FeedBack = 3;
    public static final int Type_Divider = 4;


    @NonNls
    public static String[] days = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    @NonNls
    public static String[] DaysAr = {
            "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"
    };


    public static List<Procedure> getProcedures(List<GetProcedure.Procedure> procs, List<GetCondtion.Condtion> Condtions) {


        ArrayList<Procedure> pro = new ArrayList<>();

        for (GetProcedure.Procedure g : procs) {

            g.date_activation = Util.generateDateAr(g.date_activation);
            Procedure q = new Procedure();
            q.procedure = g;
            q.items = new ArrayList<>();
            if (g.condtion_id != 0) {

                for (GetCondtion.Condtion cond : Condtions) {
                    if (cond.condtion_id == g.condtion_id) {
                        q.LocalCondtion = cond;
                        break;
                    }
                }
            }

            q.remaining_days = Util.convertStringToAr(g.remaining_days + Strings.DAYS);

            boolean is_FeedBack = false;

            for (int i = 0; i < q.procedure.feedbacks.size(); i++) {

                if (q.procedure.feedbacks.get(i).state == Types.Request_InProgress) {
                    Item t = new Item();
                    t.object = q.procedure.feedbacks.get(i);
                    t.type = Type_FeedBack;
                    t.state = q.procedure.feedbacks.get(i).state;
                    q.items.add(t);
                    is_FeedBack = true;
                    break;
                }
            }


            if (!is_FeedBack) {

                for (int i = 0; i < q.procedure.smart_feedbacks.size(); i++) {
                    if (q.procedure.smart_feedbacks.get(i).state == Types.Request_InProgress) {
                        Item t = new Item();
                        t.object = q.procedure.smart_feedbacks.get(i);
                        t.type = Type_FeedBack;
                        t.state = q.procedure.smart_feedbacks.get(i).state;
                        q.items.add(t);
                        is_FeedBack = true;
                        break;
                    }
                }
            }
            if (!is_FeedBack) {
                if (q.procedure.note_patient != null && q.procedure.note_patient.length() > 0) {

                    Item t = new Item();
                    t.object = q.procedure.note_patient;
                    t.type = Type_Note;
                    q.items.add(0, t);
                }

                HashMap<Integer, ArrayList<Item>> map = new HashMap<Integer, ArrayList<Item>>();
                for (int i = 0; i < q.procedure.services.size(); i++) {

                    Item t = new Item();
                    t.object = q.procedure.services.get(i);
                    t.state = q.procedure.services.get(i).state;
                    t.type = Type_Visit;
                    String services = "";

                    for (GetAvailibleServices.Service service : ((GetProcedure.ServicesGroup) t.object).services) {
                        String[] d = service.service_path.split("/");
                        services += d[d.length - 1] + "\n";
                    }
                    t.Service_Name = services;

                    String rooms = "";
                    for (GetAvailibleRooms.Room room : ((GetProcedure.ServicesGroup) t.object).rooms) {
                        String[] d = room.room_path.split("/");
                        rooms += d[d.length - 1] + "\n";
                    }
                    t.instit_name = rooms;


                    if (((GetProcedure.ServicesGroup) t.object).appointment.state != Types.Appointment_Not_Booked && ((GetProcedure.ServicesGroup) t.object).state == Types.Request_InProgress && ((GetProcedure.ServicesGroup) t.object).appointment.appointment_id != 0) {
                        BookingFragment.Date d = new BookingFragment.Date(((GetProcedure.ServicesGroup) t.object).appointment.date);
                        t.date = d.time.days + " - " + d.time.month + " - " + d.time.year;

                        t.from_to_day = d.getDay() + "\n" + Strings.FROM + (((GetProcedure.ServicesGroup) t.object).appointment).from.replace("-", " : ") + Strings.TO + (((GetProcedure.ServicesGroup) t.object).appointment).to.replace("-", " : ");
                        t.from_to_day = Util.convertStringToAr(t.from_to_day);
                        t.room_path = ((GetProcedure.ServicesGroup) t.object).appointment.room_path;
                        t.from_to_day = Util.convertStringToAr(t.from_to_day);
                        t.date = Util.convertStringToAr(t.date);

                    }

                    if (((GetProcedure.ServicesGroup) t.object).state == Types.Request_NotProccesse) {
                        t.state_s = Strings.waiting_for_earlier_procedures;
                    } else if (((GetProcedure.ServicesGroup) t.object).state == Types.Request_InProgress) {

                        switch (((GetProcedure.ServicesGroup) t.object).appointment.state) {
                            case Types.Appointment_Invoice_Not_paid: {
                                t.state_s = Strings.Please_Pay_For_Services;
                            }
                            break;
                            case Types.Appointment_Not_Booked: {
                                t.state_s = Strings.Please_book;
                            }
                            break;
                            case Types.Appointment_Waiting_For_Date: {
                                t.state_s = Strings.waiting_for_app_start;
                            }
                            break;
                            case Types.Appointment_Waiting_For_Enter: {
                                t.state_s = Strings.waiting_for_turn;
                            }
                            break;
                            case Types.Appointment_Waiting_For_Enter_Line: {
                                t.state_s = Strings.waiting_for_enter_line;
                            }
                            break;
                        }

                        if (((GetProcedure.ServicesGroup) t.object).appointment.room_id != 0) {
                            t.turn = Util.convertStringToAr(((GetProcedure.ServicesGroup) t.object).appointment.order + "");
                            t.queueAmount = Util.convertStringToAr(((GetProcedure.ServicesGroup) t.object).appointment.queue_amount + "");
                        }
                    } else if (((GetProcedure.ServicesGroup) t.object).state == Types.Request_Done) {
                        t.state_s = Strings.Done;
                    } else {
                        t.state_s = Strings.Failed;
                    }

                    if (map.containsKey(q.procedure.services.get(i).order)) {
                        map.get(q.procedure.services.get(i).order).add(t);
                    } else {
                        ArrayList<Item> f = new ArrayList<>();
                        map.put(q.procedure.services.get(i).order, f);
                        f.add(t);
                    }
                }

                Object[] objects = map.keySet().toArray();
                for (int i = 0; i < objects.length; i++) {
                    for (int x = i + 1; x < objects.length; x++) {
                        int x1 = (int) objects[i];
                        int x2 = (int) objects[x];
                        if (x1 > x2) {
                            objects[i] = x2;
                            objects[x] = x1;
                        }
                    }
                }

                for (int i = 0; i < objects.length; i++) {
                    q.items.addAll(map.get(objects[i]));

                }

                int done = -1;
                for (int i = 0; i < q.items.size(); i++) {

                    if (q.items.get(i).state == Types.Request_Done) {
                        done = i;
                        break;
                    }

                }

                if (done != -1) {

                    Item t = new Item();
                    t.object = Strings.Done_Visits;
                    t.type = Type_Divider;
                    t.state = Types.Request_Done;
                    q.items.add(done, t);

                }

                int in_progress = -1;
                for (int i = 0; i < q.items.size(); i++) {

                    if (q.items.get(i).state == Types.Request_InProgress) {

                        in_progress = i;
                        break;
                    }

                }

                if (in_progress != -1) {
                    Item t = new Item();
                    t.object = Strings.Visits_inProgress;
                    t.type = Type_Divider;
                    t.state = Types.Request_InProgress;
                    q.items.add(in_progress, t);

                }

                int upComing = -1;
                for (int i = 0; i < q.items.size(); i++) {
                    if (q.items.get(i).state == Types.Request_NotProccesse) {
                        upComing = i;
                        break;
                    }
                }

                if (upComing != -1) {
                    Item t = new Item();
                    t.object = Strings.Upcoming_visits;
                    t.type = Type_Divider;
                    t.state = Types.Request_NotProccesse;
                    q.items.add(upComing, t);
                }

            }
            pro.add(q);
        }

        return pro;

    }


    public static class Procedure {
        public GetProcedure.Procedure procedure;
        public List<Item> items;
        public GetCondtion.Condtion LocalCondtion;
        public String remaining_days;

    }

    public static class Item {
        public int type;
        public int state;
        @NonNls
        public Object object;

        public String Service_Name;
        public String instit_name;

        public String room_path;
        public String from_to_day;
        public String date;
        @NonNls
        public String state_s;
        public String turn, queueAmount;


    }


}
