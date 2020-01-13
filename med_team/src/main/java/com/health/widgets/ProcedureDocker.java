/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.widgets;

import com.health.objects.DoneRequest;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetProcedure;
import com.health.objects.StopProcedure;
import com.health.objects.Types;
import com.health.project.medteam.Globals;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Inspiron
 */
public class ProcedureDocker extends javax.swing.JPanel implements Globals.Updater {

    /**
     * Creates new form ProcedureDocker
     */
    GetProcedure.Procedure procedure;
    Object current;

    private JTabbedPane parent;

    public ProcedureDocker() {
        initComponents();
    }

    public void setData(GetProcedure.Procedure pro, JTabbedPane tabbedPane) {
        procedure = makeCopy(pro);
        current = null;
        ProcedureView.setData(procedure);
        if (procedure.state == Types.State_Proc_Active) {

            ArrayList<Object> in_progress = new ArrayList<>();
            current = null;
            for (int i = 0; i < procedure.feedbacks.size(); i++) {
                if (procedure.feedbacks.get(i).state == Types.Request_InProgress) {
                    in_progress.add(procedure.feedbacks.get(i));
                    break;
                }
            }
            if (in_progress.isEmpty()) {
                for (int i = 0; i < procedure.smart_feedbacks.size(); i++) {
                    if (procedure.smart_feedbacks.get(i).state == Types.Request_InProgress) {
                        in_progress.add(procedure.smart_feedbacks.get(i));
                        break;
                    }

                }
            }
            if (in_progress.isEmpty()) {
                for (int i = 0; i < procedure.services.size(); i++) {
                    if (procedure.services.get(i).state == Types.Request_InProgress) {
                        in_progress.add(procedure.services.get(i));
                    }
                }
            }
            if (!in_progress.isEmpty()) {

                if (in_progress.get(0) instanceof GetProcedure.FeedBack || in_progress.get(0) instanceof GetProcedure.SmartFeedBack) {
                    UpdateBtn.setVisible(true);
                    current = in_progress.get(0);
                } else {
                    UpdateBtn.setVisible(false);
                    for (int i = 0; i < in_progress.size(); i++) {
                        if (((GetProcedure.ServicesGroup) in_progress.get(i)).appointment.state == Types.Appointment_Waiting_For_Enter) {
                            current = in_progress.get(i);
                            break;
                        }
                    }
                    if (current == null) {
                        UpdateBtn.setVisible(false);
                        ContinueBtn.setVisible(false);
                        StopBtn.setVisible(false);
                    }
                }

            } else {
                UpdateBtn.setVisible(false);
                ContinueBtn.setVisible(false);
                StopBtn.setVisible(false);
            }
        } else {
            UpdateBtn.setVisible(false);
            ContinueBtn.setVisible(false);
            StopBtn.setVisible(false);
        }
    }

    public GetProcedure.Procedure makeCopy(GetProcedure.Procedure act) {

        if (act == null) {
            GetProcedure.Procedure ac = new GetProcedure.Procedure();
            ac.feedbacks = new ArrayList<>();
            ac.smart_feedbacks = new ArrayList<>();
            ac.services = new ArrayList<>();
            ac.name = "";
            ac.activation_period = 0;
            ac.condtion_id = 0;
            ac.cycle_period = 0;
            ac.delay_period = 0;
            ac.note_med = "";
            ac.note_patient = "";
            ac.objective = "";
            ac.routine_activation_period = 0;
            ac.type = 0;
            ac.proc_id = 0;
            return ac;

        } else {

            GetProcedure.Procedure ac = new GetProcedure.Procedure();
            ac.feedbacks = new ArrayList<>();
            ac.smart_feedbacks = new ArrayList<>();
            ac.services = new ArrayList<>();
            ac.name = act.name;
            ac.activation_period = act.activation_period;
            ac.condtion_id = act.condtion_id;
            ac.cycle_period = act.cycle_period;
            ac.delay_period = act.delay_period;
            ac.note_med = act.note_med;
            ac.note_patient = act.note_patient;
            ac.objective = act.objective;
            ac.routine_activation_period = act.routine_activation_period;
            ac.type = act.type;
            ac.proc_id = act.proc_id;
            ac.date_activation = act.date_activation;
            ac.date_deactivation = act.date_deactivation;
            ac.state = act.state;
            ac.remaining_days = act.remaining_days;

            for (GetProcedure.FeedBack feed : act.feedbacks) {
                GetProcedure.FeedBack f = new GetProcedure.FeedBack();

                f.med_team_id = feed.med_team_id;
                f.med_team_name = feed.med_team_name;
                f.note_med = feed.note_med;
                f.order = feed.order;
                f.req_id = feed.req_id;
                f.state = feed.state;
                ac.feedbacks.add(f);

            }

            for (GetProcedure.SmartFeedBack feed : act.smart_feedbacks) {
                GetProcedure.SmartFeedBack f = new GetProcedure.SmartFeedBack();
                f.note_med = feed.note_med;
                f.order = feed.order;
                f.req_id = feed.req_id;
                f.state = feed.state;
                f.session_id = feed.session_id;

                List<GetAvailibleServices.Service> service = new ArrayList<>();
                for (GetAvailibleServices.Service ser : feed.services) {
                    service.add(ser);
                }

                f.services = service;
                ac.smart_feedbacks.add(f);

            }

            for (GetProcedure.ServicesGroup feed : act.services) {
                GetProcedure.ServicesGroup f = new GetProcedure.ServicesGroup();

                f.note_med = feed.note_med;
                f.order = feed.order;
                f.req_id = feed.req_id;
                f.count = feed.count;
                f.note_res = feed.note_res;
                f.state = feed.state;

                if (feed.appointment != null) {
                    GetProcedure.Appointment app = new GetProcedure.Appointment();
                    app.appointment_id = feed.appointment.appointment_id;
                    app.date = feed.appointment.date;
                    app.from = feed.appointment.from;
                    app.to = feed.appointment.to;
                    app.room_path = feed.appointment.room_path;
                    app.state = feed.appointment.state;
                    app.queue_amount = feed.appointment.queue_amount;
                    app.room_id = feed.appointment.room_id;
                    app.order = feed.appointment.order;
                    f.appointment = app;
                }
                List<GetAvailibleServices.Service> service = new ArrayList<>();
                for (GetAvailibleServices.Service ser : feed.services) {
                    service.add(ser);
                }
                f.services = service;
                List<GetAvailibleRooms.Room> room = new ArrayList<>();
                for (GetAvailibleRooms.Room ro : feed.rooms) {
                    room.add(ro);
                }
                f.rooms = room;
                ac.services.add(f);
            }

            return ac;

        }
    }

    private void deleteDoneAndFeed(GetProcedure.Procedure proc) {
        for (int i = 0; i < proc.feedbacks.size(); i++) {
            GetProcedure.FeedBack f = proc.feedbacks.get(i);
            if (f.state == Types.Request_Done || f.state == Types.Request_InProgress) {
                proc.feedbacks.remove(i);
                i--;
            }

        }
        for (int i = 0; i < proc.smart_feedbacks.size(); i++) {
            GetProcedure.SmartFeedBack f = proc.smart_feedbacks.get(i);
            if (f.state == Types.Request_Done || f.state == Types.Request_InProgress) {
                proc.smart_feedbacks.remove(i);
                i--;
            }
        }
        for (int i = 0; i < proc.services.size(); i++) {
            GetProcedure.ServicesGroup f = proc.services.get(i);
            if (f.state == Types.Request_Done) {
                proc.services.remove(i);
                i--;
            }
        }

        reOrder(proc);
    }

    private void reOrder(GetProcedure.Procedure proc) {

        ArrayList<ArrayList<Object>> items = new ArrayList<>();

        for (int i = 0; i < proc.feedbacks.size(); i++) {
            addOrder(proc.feedbacks.get(i), proc.feedbacks.get(i).order, items);
        }

        for (int i = 0; i < proc.smart_feedbacks.size(); i++) {
            addOrder(proc.smart_feedbacks.get(i), proc.smart_feedbacks.get(i).order, items);
        }

        for (int i = 0; i < proc.services.size(); i++) {
            addOrder(proc.services.get(i), proc.services.get(i).order, items);
        }

        for (int i = 0; i < items.size(); i++) {

            if (items.get(i).isEmpty()) {
                items.remove(i);
                i--;
            }

        }

        for (int i = 0; i < items.size(); i++) {

            if (items.get(i).get(0) instanceof GetProcedure.FeedBack) {

                ((GetProcedure.FeedBack) items.get(i).get(0)).order = i;

            } else if (items.get(i).get(0) instanceof GetProcedure.SmartFeedBack) {
                ((GetProcedure.SmartFeedBack) items.get(i).get(0)).order = i;

            } else {//ServiceGroup
                for (int x = 0; x < items.get(i).size(); x++) {
                    ((GetProcedure.ServicesGroup) items.get(i).get(x)).order = i;
                }
            }
        }
    }

    public void addOrder(Object o, int order, ArrayList<ArrayList<Object>> Orders) {
        while (order >= Orders.size()) {
            Orders.add(new ArrayList<>());
        }
        Orders.get(order).add(o);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ProcedureView = new com.health.widgets.ProcedureViewer();
        StopBtn = new javax.swing.JButton();
        ContinueBtn = new javax.swing.JButton();
        UpdateBtn = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(32767, 700));

        jScrollPane1.setViewportView(ProcedureView);

        StopBtn.setText("Stop");
        StopBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopBtnActionPerformed(evt);
            }
        });

        ContinueBtn.setText("Continue Procedures");
        ContinueBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContinueBtnActionPerformed(evt);
            }
        });

        UpdateBtn.setText("Update");
        UpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ContinueBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(UpdateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(UpdateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ContinueBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(StopBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void UpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateBtnActionPerformed
        // TODO add your handling code here:
        GetProcedure.Procedure proc = makeCopy(procedure);
        deleteDoneAndFeed(proc);
        final JFrame frame = new JFrame("Update Procedure");
        Globals.updateProc = new WeakReference<>(frame);
        ProcedureUpdate updater = new ProcedureUpdate();
        updater.setData(proc, this, procedure);
        frame.getContentPane().add(updater, BorderLayout.CENTER);
        Dimension d = new Dimension(1200, 800);
        frame.setSize(d);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
    }//GEN-LAST:event_UpdateBtnActionPerformed

    private void ContinueBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContinueBtnActionPerformed
        // TODO add your handling code here:

        ContinueBtn.setText("Please wait...");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (current instanceof GetProcedure.FeedBack) {
                    DoneRequest.DoneRequest(Globals.patien_id, procedure.proc_id, ((GetProcedure.FeedBack) current).req_id);
                } else if (current instanceof GetProcedure.SmartFeedBack) {
                    DoneRequest.DoneRequest(Globals.patien_id, procedure.proc_id, ((GetProcedure.SmartFeedBack) current).req_id);
                } else {//ServiceGroup
                    DoneRequest.DoneRequest(Globals.patien_id, procedure.proc_id, ((GetProcedure.ServicesGroup) current).req_id);
                }
                ContinueBtn.setText("Continue");

            }
        });

        GetProcedure.Procedure c = GetProcedure.getProcedure(Globals.patien_id, procedure.proc_id);
        setData(c, parent);

        Globals.showMsg("Done Successfully");
    }//GEN-LAST:event_ContinueBtnActionPerformed

    private void StopBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopBtnActionPerformed
        // TODO add your handling code here:        

        StopBtn.setText("Please wait...");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StopProcedure.StopProcedure(Globals.patien_id, procedure.proc_id);
                GetProcedure.Procedure c = GetProcedure.getProcedure(Globals.patien_id, procedure.proc_id);
                setData(c, parent);
                parent.remove(ProcedureDocker.this);
                parent.revalidate();
                parent.repaint();
                Globals.showMsg("Stopped SuccessFully");
                StopBtn.setText("Stop");

            }
        });


    }//GEN-LAST:event_StopBtnActionPerformed

    @Override
    public void update() {
        try {
            GetProcedure.Procedure c = GetProcedure.getProcedure(Globals.patien_id, procedure.proc_id);
            setData(c, parent);
        } catch (Exception ee) {

        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ContinueBtn;
    private com.health.widgets.ProcedureViewer ProcedureView;
    private javax.swing.JButton StopBtn;
    private javax.swing.JButton UpdateBtn;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
