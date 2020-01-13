package com.health.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.health.Data.Procedures;
import com.health.alaa.Inflators.NoteItem;
import com.health.alaa.Inflators.VisitFeedBackWaiting;
import com.health.alaa.Inflators.VisitItem;
import com.health.alaa.Inflators.VisitMedicalInstit;
import com.health.objects.GetProcedure;
import com.health.objects.Strings;
import com.health.objects.Types;
import com.health.project.entry.Application;
import com.health.project.entry.DataChangedListener;
import com.health.project.entry.R;
import com.health.project.entry.Util;

import org.jetbrains.annotations.NonNls;

public class VisitsFragment extends Fragment implements DataChangedListener {


    private static final int Header_Type = -199925;


    private int expanded_proc = -1;
    private long id;


    private GetProcedure.Procedure attached;

    private Adapter ad;
    private RecyclerView rec;
    private LinearLayoutManager manager;

    public void SelectVisitBeforeInit(GetProcedure.Procedure proc) {
        attached = proc;
    }

    private void selectVisit(int pos, long id) {
        this.expanded_proc = pos;
        this.id = id;
        Util.safeBeginDelayedTranstion((ViewGroup) getView());
        ad.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        super.onCreateView(inflater, parent, state);

        Application.registerListener(this);


        this.rec = new RecyclerView(inflater.getContext());
        this.rec.setPadding((int) (5 * getResources().getDisplayMetrics().density), 0, (int) (5 * getResources().getDisplayMetrics().density), 0);
        this.manager = new LinearLayoutManager(inflater.getContext());
        ad = new Adapter();
        this.rec.setLayoutManager(manager);
        this.rec.setAdapter(ad);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));
        return rec;
    }

    @Override
    public void onViewCreated(View view, @NonNls Bundle state) {
        super.onViewCreated(view, state);


        if (attached != null) {
            int ex = -1;
            long id = 0;
            for (int i = 0; i < Application.ActiveProcedures.size(); i++) {
                if (Application.ActiveProcedures.get(i).proc_id == attached.proc_id) {
                    id = attached.proc_id;
                    ex = i;
                    break;
                }
            }
            attached = null;
            if (ex != -1) selectVisit(ex, id);
        } else if (state != null) {
            int pos = state.getInt(Strings.EXPANDED_PROC, -1);
            if (pos != -1) {

                try {
                    id = Application.ActiveProcedures.get(pos).proc_id;
                } catch (Exception e) {
                    pos = -1;
                }
            }
            selectVisit(pos, id);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
        b.putInt(Strings.EXPANDED_PROC, expanded_proc);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Application.unRegisterListener(this);
        Util.safeBeginDelayedTranstion((ViewGroup) getActivity().findViewById(R.id.root_layout));

    }


    @Override
    public void onProceduresChanged() {
        this.expanded_proc = -1;

        if (id > 0) {

            for (int i = 0; i < Application.ActiveProcedures.size(); i++) {

                if (Application.ActiveProcedures.get(i).proc_id == id) {

                    this.expanded_proc = i;
                    break;
                }
            }

        }

        Application.main_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.safeBeginDelayedTranstion((ViewGroup) getView());
                ad.notifyDataSetChanged();
            }
        }, 1000);

    }

    @Override
    public void onProceduresInit() {
        this.expanded_proc = -1;

        if (id > 0) {
            for (int i = 0; i < Application.ActiveProcedures.size(); i++) {

                if (Application.ActiveProcedures.get(i).proc_id == id) {

                    this.expanded_proc = i;
                    break;
                }
            }

        }
        Util.safeBeginDelayedTranstion((ViewGroup) getView());
        ad.notifyDataSetChanged();

    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        int type;

        public ViewHolder(@NonNull View itemView, int type) {
            super(itemView);
            this.type = type;
        }

    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {

            if (type == Header_Type) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.header, viewGroup, false), Header_Type) {
                };
            }

            if (type == 100) {

                VisitItem item = new VisitItem(getActivity().getLayoutInflater(), viewGroup);
                ViewHolder holder = new ViewHolder(item.getVisitMainItem(), type);
                return holder;

            } else {

                switch (type) {
                    case Procedures.Type_Divider: {
                        TextView textView = (TextView) getActivity().getLayoutInflater().inflate(R.layout.divider_visit, viewGroup, false);
                        textView.setTag(textView);
                        ViewHolder holder = new ViewHolder(textView, type);
                        return holder;
                    }
                    case Procedures.Type_FeedBack: {
                        VisitFeedBackWaiting item = new VisitFeedBackWaiting(getActivity().getLayoutInflater(), viewGroup);
                        ViewHolder holder = new ViewHolder(item.getVisitFeedbackContainer(), type);
                        return holder;


                    }
                    case Procedures.Type_Visit: {
                        VisitMedicalInstit item = new VisitMedicalInstit(getActivity().getLayoutInflater(), viewGroup);
                        ViewHolder holder = new ViewHolder(item.getVisitContainer(), type);
                        return holder;


                    }
                    case Procedures.Type_Note: {

                        NoteItem item = new NoteItem(getActivity().getLayoutInflater(), viewGroup);
                        ViewHolder holder = new ViewHolder(item.getNoteItem(), type);
                        return holder;

                    }

                }
            }

            return new ViewHolder(new Button(viewGroup.getContext()), -1);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            if (i == 0) return;

            i--;
            final int i1 = i;

            if (viewHolder.type == 100) {

                if (expanded_proc == -1) {


                    final Procedures.Procedure proc = Application.Procedures.get(i);
                    VisitItem item = (VisitItem) viewHolder.itemView.getTag();
                    item.getCancel().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SenderFragment s = new SenderFragment();
                            s.setCancelVisit(proc.procedure);
                            getFragmentManager().beginTransaction().add(R.id.root_layout, s, null).addToBackStack(null).commit();

                        }
                    });

                    if (proc.LocalCondtion != null) {

                        item.getShowRelated().setVisibility(View.VISIBLE);
                        item.getShowRelated().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CondtionFragment f = new CondtionFragment();
                                f.setID(proc.procedure.condtion_id);
                                getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();
                            }
                        });

                    } else {
                        item.getShowRelated().setVisibility(View.GONE);
                    }
                    item.getShowVisit().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectVisit(i1, proc.procedure.proc_id);
                        }
                    });


                    item.getDate().setText(proc.procedure.date_activation);
                    item.getRemainingDays().setText(proc.remaining_days);
                    item.getPurposeDesc().setText(proc.procedure.objective);
                    //#186DB2   #E91E63
                    item.getShowVisit().setBackgroundTintList(ColorStateList.valueOf(Color.rgb(24, 109, 178)));
                    item.getVisitTitle().setText(proc.procedure.name);

                } else {
                    VisitItem item = (VisitItem) viewHolder.itemView.getTag();

                    if (i < expanded_proc) {
                        final Procedures.Procedure proc = Application.Procedures.get(i);

                        item.getCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SenderFragment s = new SenderFragment();
                                s.setCancelVisit(proc.procedure);
                                getFragmentManager().beginTransaction().add(R.id.root_layout, s, null).addToBackStack(null).commit();
                            }
                        });

                        if (proc.LocalCondtion != null) {

                            item.getShowRelated().setVisibility(View.VISIBLE);
                            item.getShowRelated().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CondtionFragment f = new CondtionFragment();
                                    f.setID(proc.procedure.condtion_id);
                                    getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();
                                }
                            });

                        } else {
                            item.getShowRelated().setVisibility(View.GONE);
                        }
                        item.getShowVisit().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectVisit(i1, proc.procedure.proc_id);

                            }
                        });

                        item.getVisitTitle().setText(proc.procedure.name);
                        item.getDate().setText(proc.procedure.date_activation);
                        item.getRemainingDays().setText(proc.remaining_days);
                        item.getPurposeDesc().setText(proc.procedure.objective);
                        //#186DB2   #E91E63
                        item.getShowVisit().setBackgroundTintList(ColorStateList.valueOf(Color.rgb(24, 109, 178)));


                    } else if (i == expanded_proc) {
                        final Procedures.Procedure proc = Application.Procedures.get(i);

                        item.getCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SenderFragment s = new SenderFragment();
                                s.setCancelVisit(proc.procedure);
                                getFragmentManager().beginTransaction().add(R.id.root_layout, s, null).addToBackStack(null).commit();
                            }
                        });

                        if (proc.LocalCondtion != null) {

                            item.getShowRelated().setVisibility(View.VISIBLE);
                            item.getShowRelated().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CondtionFragment f = new CondtionFragment();
                                    f.setID(proc.procedure.condtion_id);
                                    getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();
                                }
                            });

                        } else {
                            item.getShowRelated().setVisibility(View.GONE);
                        }
                        item.getShowVisit().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectVisit(-1, 0);
                            }
                        });
                        item.getVisitTitle().setText(proc.procedure.name);
                        item.getDate().setText(proc.procedure.date_activation);
                        item.getRemainingDays().setText(proc.remaining_days);
                        item.getPurposeDesc().setText(proc.procedure.objective);
                        //#186DB2   #E91E63
                        item.getShowVisit().setBackgroundTintList(ColorStateList.valueOf(Color.rgb(233, 30, 99)));
                    } else {
                        final int newPos = i - Application.Procedures.get(expanded_proc).items.size();
                        final Procedures.Procedure proc = Application.Procedures.get(newPos);

                        item.getCancel().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SenderFragment s = new SenderFragment();
                                s.setCancelVisit(proc.procedure);
                                getFragmentManager().beginTransaction().add(R.id.root_layout, s, null).addToBackStack(null).commit();
                            }
                        });

                        if (proc.LocalCondtion != null) {

                            item.getShowRelated().setVisibility(View.VISIBLE);
                            item.getShowRelated().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CondtionFragment f = new CondtionFragment();
                                    f.setID(proc.procedure.condtion_id);
                                    getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();
                                }
                            });

                        } else {
                            item.getShowRelated().setVisibility(View.GONE);
                        }

                        item.getShowVisit().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectVisit(newPos, proc.procedure.proc_id);
                            }
                        });
                        item.getVisitTitle().setText(proc.procedure.name);
                        item.getDate().setText(proc.procedure.date_activation);
                        item.getRemainingDays().setText(proc.remaining_days);
                        item.getPurposeDesc().setText(proc.procedure.objective);
                        //#186DB2   #E91E63
                        item.getShowVisit().setBackgroundTintList(ColorStateList.valueOf(Color.rgb(24, 109, 178)));
                    }

                }

            } else {

                switch (viewHolder.type) {
                    case Procedures.Type_Divider: {
                        TextView textView = (TextView) viewHolder.itemView.getTag();
                        textView.setText((String) Application.Procedures.get(expanded_proc).items.get(i - (expanded_proc + 1)).object);
                    }
                    break;
                    case Procedures.Type_FeedBack: {
                        VisitFeedBackWaiting item = (VisitFeedBackWaiting) viewHolder.itemView.getTag();
                        //item set Message

                    }
                    break;

                    case Procedures.Type_Visit: {
                        VisitMedicalInstit item = (VisitMedicalInstit) viewHolder.itemView.getTag();
                        final Procedures.Procedure proc = Application.Procedures.get(expanded_proc);
                        int pos = i - (expanded_proc + 1);
                        final GetProcedure.ServicesGroup servicesGroup = ((GetProcedure.ServicesGroup) proc.items.get(pos).object);


                        switch (servicesGroup.state) {

                            case Types.Request_NotProccesse:
                            case Types.Request_Done: {
                                item.getInstVisitAppointContainer().setVisibility(View.GONE);
                                item.getVisitInstAppointHeader().setVisibility(View.GONE);
                                item.getVisitInstQueueHeader().setVisibility(View.GONE);
                                item.getVisitInstQueue().setVisibility(View.GONE);
                                item.getVisitInstTurn().setVisibility(View.GONE);
                                item.getVisitInstTurnHeader().setVisibility(View.GONE);
                                item.getLeft_btn().setVisibility(View.GONE);
                                item.getRight_btn().setVisibility(View.GONE);
                            }
                            break;
                            case Types.Request_InProgress: {

                                if (servicesGroup.appointment.appointment_id == 0) {
                                    item.getInstVisitAppointContainer().setVisibility(View.GONE);
                                    item.getVisitInstAppointHeader().setVisibility(View.GONE);
                                    item.getVisitInstQueueHeader().setVisibility(View.GONE);
                                    item.getVisitInstQueue().setVisibility(View.GONE);
                                    item.getVisitInstTurn().setVisibility(View.GONE);
                                    item.getVisitInstTurnHeader().setVisibility(View.GONE);


                                    item.getLeft_btn().setVisibility(servicesGroup.appointment.state == Types.Appointment_Not_Booked ? View.GONE : View.VISIBLE);
                                    item.getRight_btn().setVisibility(View.VISIBLE);

                                    item.getRight_btn().setText(Strings.BOOK_APPOINTMENT);
                                    item.getLeft_btn().setText(Strings.PAY);

                                    item.getLeft_btn().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            BillingFragment f = new BillingFragment();
                                            f.setData(proc.procedure, servicesGroup);

                                            getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();

                                        }
                                    });

                                    item.getRight_btn().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            BookingFragment f = new BookingFragment();
                                            f.setData(proc.procedure, servicesGroup);
                                            getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();
                                        }
                                    });

                                    break;

                                } else {

                                    item.getInstVisitAppointContainer().setVisibility(View.VISIBLE);
                                    item.getVisitInstAppointHeader().setVisibility(View.VISIBLE);

                                    item.getVisitInstAppointDate().setText(proc.items.get(pos).date);
                                    item.getVisitInstAppointDayTime().setText(proc.items.get(pos).from_to_day);
                                    item.getVisitInstAppointPath().setText(proc.items.get(pos).room_path);
                                    item.getVisitInstQueue().setText(proc.items.get(pos).queueAmount);
                                    item.getVisitInstTurn().setText(proc.items.get(pos).turn);


                                    switch (servicesGroup.appointment.state) {

                                        case Types.Appointment_Invoice_Not_paid: {
                                            item.getVisitInstQueueHeader().setVisibility(View.GONE);
                                            item.getVisitInstQueue().setVisibility(View.GONE);
                                            item.getVisitInstTurn().setVisibility(View.GONE);
                                            item.getVisitInstTurnHeader().setVisibility(View.GONE);

                                            item.getLeft_btn().setVisibility(View.VISIBLE);
                                            item.getRight_btn().setVisibility(View.VISIBLE);
                                            item.getRight_btn().setText(Strings.CHANGE_APPOINTMENT);
                                            item.getLeft_btn().setText(Strings.PAY);

                                            item.getLeft_btn().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {


                                                    BillingFragment f = new BillingFragment();
                                                    f.setData(proc.procedure, servicesGroup);
                                                    getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();

                                                }
                                            });

                                            item.getRight_btn().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    BookingFragment f = new BookingFragment();
                                                    f.setData(proc.procedure, servicesGroup);
                                                    getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();
                                                }
                                            });


                                        }
                                        break;
                                        case Types.Appointment_Waiting_For_Date: {
                                            item.getVisitInstQueueHeader().setVisibility(View.GONE);
                                            item.getVisitInstQueue().setVisibility(View.GONE);
                                            item.getVisitInstTurn().setVisibility(View.GONE);
                                            item.getVisitInstTurnHeader().setVisibility(View.GONE);
                                            item.getRight_btn().setVisibility(View.VISIBLE);
                                            item.getLeft_btn().setVisibility(View.GONE);
                                            item.getRight_btn().setText(Strings.CHANGE_APPOINTMENT);

                                            item.getRight_btn().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    BookingFragment f = new BookingFragment();
                                                    f.setData(proc.procedure, servicesGroup);
                                                    getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();
                                                }
                                            });
                                        }
                                        break;
                                        case Types.Appointment_Waiting_For_Enter_Line: {
                                            item.getVisitInstTurn().setVisibility(View.GONE);
                                            item.getVisitInstTurnHeader().setVisibility(View.GONE);
                                            item.getVisitInstQueueHeader().setVisibility(View.VISIBLE);
                                            item.getVisitInstQueue().setVisibility(View.VISIBLE);

                                            item.getLeft_btn().setVisibility(View.VISIBLE);
                                            item.getRight_btn().setVisibility(View.VISIBLE);
                                            item.getRight_btn().setText(Strings.CHANGE_APPOINTMENT);
                                            item.getLeft_btn().setText(Strings.ENTER_THE_LINE);

                                            item.getLeft_btn().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    SenderFragment s = new SenderFragment();
                                                    s.setEnterTheLine(proc.procedure, servicesGroup);
                                                    getFragmentManager().beginTransaction().add(R.id.root_layout, s, null).addToBackStack(null).commit();
                                                }
                                            });

                                            item.getRight_btn().setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    BookingFragment f = new BookingFragment();
                                                    f.setData(proc.procedure, servicesGroup);
                                                    getFragmentManager().beginTransaction().add(R.id.root_layout, f, null).addToBackStack(null).commit();
                                                }
                                            });

                                        }
                                        break;
                                        case Types.Appointment_Waiting_For_Enter: {
                                            item.getVisitInstTurn().setVisibility(View.VISIBLE);
                                            item.getVisitInstTurnHeader().setVisibility(View.VISIBLE);
                                            item.getVisitInstQueueHeader().setVisibility(View.VISIBLE);
                                            item.getVisitInstQueue().setVisibility(View.VISIBLE);

                                            item.getLeft_btn().setVisibility(View.GONE);
                                            item.getRight_btn().setVisibility(View.GONE);
                                        }
                                        break;
                                    }


                                }

                            }
                            break;
                        }

                        item.getVisitInstServices().setText(proc.items.get(pos).Service_Name);
                        item.getVisitInstName().setText(proc.items.get(pos).instit_name);
                        item.getVisitInstState().setText(proc.items.get(pos).state_s);


                    }
                    break;

                    case Procedures.Type_Note: {

                        NoteItem item = (NoteItem) viewHolder.itemView.getTag();
                        item.getNoteDesc().setText((String) ((Application.Procedures.get(expanded_proc).items.get(i - (expanded_proc + 1)).object)));
                    }
                    break;


                }


            }

        }


        @Override
        public int getItemViewType(int pos) {
            if (pos == 0) {
                return Header_Type;
            }
            pos--;
            if (expanded_proc == -1) return 100;
            if (pos > expanded_proc && pos <= expanded_proc + Application.Procedures.get(expanded_proc).items.size())
                return Application.Procedures.get(expanded_proc).items.get(pos - (expanded_proc + 1)).type;
            else return 100;
        }

        @Override
        public int getItemCount() {
            return (Application.is_initialized ? (Application.Procedures.size() + (expanded_proc != -1 ? Application.Procedures.get(expanded_proc).items.size() : 0)) : 0) + 1;
        }
    }


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
    public void onBillsInit() {

    }

    @Override
    public void onPersonalDataInit() {

    }
}
