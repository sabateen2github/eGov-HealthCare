package com.health.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.health.alaa.Inflators.VisitMedicalInstitSeperate;
import com.health.objects.BookAppointment;
import com.health.objects.GetProcedure;
import com.health.objects.GetRoomsAppointments;
import com.health.objects.GetTime;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.project.entry.Application;
import com.health.project.entry.DataChangedListener;
import com.health.project.entry.R;
import com.health.project.entry.Util;

import org.jetbrains.annotations.NonNls;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    private static GetProcedure.Procedure proc;
    private static GetProcedure.ServicesGroup group;

    private static List<GetRoomsAppointments.Appointment> appointments = null;


    private static boolean done = false;

    private static boolean done_booking = false;

    public void setData(GetProcedure.Procedure proc, GetProcedure.ServicesGroup group) {
        BookingFragment.proc = proc;
        BookingFragment.group = group;
        done = false;
        done_booking = false;
        appointments = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
    }


    private adapter ad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
        ad = new adapter();
        return inflater.inflate(R.layout.booking_layout, parent, false);

    }


    @Override
    public void onCreate(Bundle state) {

        super.onCreate(state);

        if (done_booking) {
            done_booking = false;
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle state) {
        super.onViewCreated(view, state);

        ((Toolbar) view.findViewById(R.id.booking_toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });


        if (state == null) {
            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {

                    List<Long> longs = new ArrayList<>();
                    for (int i = 0; i < group.rooms.size(); i++) {
                        longs.add(group.rooms.get(i).room_id);

                    }
                    Log.e("MessageAlaa", longs.size() + "");
                    final List<GetRoomsAppointments.Appointment> apps = GetRoomsAppointments.GetRoomsAppointments(longs);
                    if (apps != null && apps.size() > 0) {

                        for (int i = 0; i < apps.size(); i++) {

                            Date date = new Date(apps.get(i).date);
                            apps.get(i).day = date.getDay();
                            apps.get(i).from_to_day = apps.get(i).day + " \n" + Strings.FROM + apps.get(i).from.replace("-", " : ") + Strings.TO + apps.get(i).to.replace("-", " : ");
                            apps.get(i).from_to_day = Util.convertStringToAr(apps.get(i).from_to_day);
                            apps.get(i).date_formatted = Util.generateDateAr(apps.get(i).date);
                        }



                        Application.main_handler.post(new Runnable() {
                            @Override
                            public void run() {


                                done = true;
                                if (isAdded()) {
                                    appointments = apps;
                                    update();
                                } else {
                                    appointments = apps;
                                }
                            }
                        });

                    } else {
                        Application.main_handler.post(new Runnable() {
                            @Override
                            public void run() {
                                done = true;
                                done_booking = true;
                                appointments = null;
                                if (isAdded()) {
                                    Toast.makeText(getActivity(), Strings.Schedule_Empty, Toast.LENGTH_LONG).show();
                                    getFragmentManager().popBackStack();
                                }
                            }
                        });

                    }

                }
            });

        } else {
            update();
        }

    }


    private class adapter extends RecyclerView.Adapter {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            VisitMedicalInstitSeperate item = new VisitMedicalInstitSeperate(getLayoutInflater(), viewGroup);


            return new RecyclerView.ViewHolder(item.getInstVisitAppointContainerS()) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

            VisitMedicalInstitSeperate item = (VisitMedicalInstitSeperate) viewHolder.itemView.getTag();
            item.getVisitInstAppointDayTimeS().setText(appointments.get(i).from_to_day);
            item.getVisitInstAppointDateS().setText(appointments.get(i).date_formatted);
            item.getVisitInstAppointPathS().setText(appointments.get(i).room_path);
            item.getInstVisitAppointContainerS().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Util.safeBeginDelayedTranstion((ViewGroup) getView());
                    ((ViewGroup) getView().findViewById(R.id.booking_content)).getChildAt(0).setVisibility(View.VISIBLE);
                    ((ViewGroup) getView().findViewById(R.id.booking_content)).getChildAt(1).setVisibility(View.GONE);

                    final GetRoomsAppointments.Appointment t = appointments.get(i);
                    done_booking = false;
                    AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
                        @Override
                        public void run() {

                            final boolean is = BookAppointment.bookAppointment(proc.proc_id, t.room_id, t.appointment_id, group.req_id, Application.Patient_ID);

                            Application.registerOneTime(new DataChangedListener() {
                                @Override
                                public void onNotificationsChanged() {

                                }

                                @Override
                                public void onAttachedProcedureChanged() {

                                }

                                @Override
                                public void onCondtionsChanged() {

                                }

                                @Override
                                public void onProceduresChanged() {
                                    done_booking = true;
                                    if (isAdded()) {
                                        Toast.makeText(getActivity(), Strings.APPOINTMENT_BOOKED_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                                        getFragmentManager().popBackStack();
                                    }
                                }

                                @Override
                                public void onBillsChanged() {

                                }

                                @Override
                                public void onPersonalDataChanged() {

                                }

                                @Override
                                public void onNotificationInit() {

                                }

                                @Override
                                public void onAttachedProcedureInit() {

                                }

                                @Override
                                public void onCondtionsInit() {

                                }

                                @Override
                                public void onProceduresInit() {

                                }

                                @Override
                                public void onBillsInit() {

                                }

                                @Override
                                public void onPersonalDataInit() {

                                }
                            });


                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }
    }

    private void update() {
        if (done) {
            Util.safeBeginDelayedTranstion((ViewGroup) getView());
            RecyclerView rec = new RecyclerView(getActivity());
            rec.setLayoutManager(new LinearLayoutManager(getActivity()));
            rec.setAdapter(ad);
            ((ViewGroup) getView().findViewById(R.id.booking_content)).getChildAt(0).setVisibility(View.GONE);
            ((ViewGroup) getView().findViewById(R.id.booking_content)).addView(rec, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        }
    }


    public static class Date {

        public GetTime.Time time;

        public Date(String date) {

            String[] h = date.split("-");
            int year = Integer.parseInt(h[0]);
            int month = Integer.parseInt(h[1]);
            int day = Integer.parseInt(h[2]);
            int hour = Integer.parseInt(h[3]);
            int minute = Integer.parseInt(h[4]);
            time = new GetTime.Time();
            time.year = year;
            time.month = month;
            time.days = day;
            time.hour = hour;
            time.min = minute;

            DateTime t = new DateTime(time.year, time.month, time.days, time.hour, time.min);
            time.day = t.getDayOfWeek();

        }

        public boolean isToday() {
            return (Application.currentTime.year == time.year && Application.currentTime.month == time.month && Application.currentTime.days == time.days);
        }

        @NonNls
        public String getDay() {
            if (isToday()) {
                if (Application.lang == Types.Lang_Arabic) {
                    return "اليوم";


                } else {
                    return "Today";

                }
            }
            if (Application.lang == Types.Lang_Arabic) {
                return DaysAr[(time.day - 1)];
            } else {
                return Days[(time.day - 1)];
            }
        }


    }

    @NonNls
    private static final String[] Days = {
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };


    @NonNls
    private static final String[] DaysAr = {
            "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت", "الأحد"
    };

}
