/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.widgets;

import com.health.installer.fund.utils.Globals;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import com.health.objects.Types;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Inspiron
 */
public class Procedure extends javax.swing.JPanel {

    /**
     * Creates new form Procedure
     */
    private GetProcedure.Procedure proc1;
    private GetProcedure.Procedure old;

    private GetCondtion.Condtion parent;

    private List<OnOrderChanged> listeners;

    public List<List<Object>> Orders;

    public static final String[] Order_Labels;

    public Procedure() {
        initComponents();
        Remove_btn.setVisible(false);
        ForPatients.setVisible(false);

        name_text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();

            }

            void update() {
                proc1.name = name_text.getText();
                System.out.println(proc1.name);

            }
        });

        activationPeriod.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                update();
            }

            void update() {
                proc1.activation_period = (int) activationPeriod.getValue();
                System.out.println(proc1.activation_period);

            }
        });

        note_patient_text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            void update() {
                proc1.note_patient = note_patient_text.getText();
            }
        });

        note_med_text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            void update() {
                proc1.note_med = note_med_text.getText();
            }
        });

        obj_text.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            void update() {
                proc1.objective = obj_text.getText();
            }
        });

        listeners = new ArrayList<>();
        Orders = new ArrayList<>();

        ForPatients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                proc1.type = ForPatients.isSelected() ? Types.Type_Template_User : Types.Type_Template;
                if (ForPatients.isSelected()) {
                    add_feed_btn.setEnabled(false);
                    for (int i = 0; i < RequestContainer.getComponentCount(); i++) {
                        if (RequestContainer.getComponent(i) instanceof FeedBack) {
                            RequestContainer.getComponent(i).setEnabled(false);
                        }
                    }
                } else {
                    add_feed_btn.setEnabled(true);
                    for (int i = 0; i < RequestContainer.getComponentCount(); i++) {
                        if (RequestContainer.getComponent(i) instanceof FeedBack) {
                            RequestContainer.getComponent(i).setEnabled(true);
                        }
                    }
                }

            }
        });

    }

    public void setRemoveButtonVisibilty(boolean is) {

        Remove_btn.setVisible(is);
    }

    public void setForPatientVis(boolean is) {

        ForPatients.setVisible(is);
    }

    public void setData(GetCondtion.Condtion parent, GetProcedure.Procedure proc, boolean New, GetProcedure.Procedure old) {
        this.proc1 = proc;
        this.old = old;
        this.parent = parent;
        if (!New) {

            name_text.setText(proc.name);
            note_med_text.setText(proc.note_med);
            note_patient_text.setText(proc.note_patient);
            obj_text.setText(proc.objective);
            activationPeriod.setValue(proc.activation_period);
            ForPatients.setSelected(proc.type == Types.Type_Template_User);

            for (int i = 0; i < proc.feedbacks.size(); i++) {
                addExistingFeedBack(proc.feedbacks.get(i));
            }
            for (int i = 0; i < proc.smart_feedbacks.size(); i++) {
                addExsistingSmartFeed(proc.smart_feedbacks.get(i));
            }
            for (int i = 0; i < proc.services.size(); i++) {
                addExistingService(proc.services.get(i));
            }

            if (proc.type == Types.Type_Template_User) {
                ForPatients.setSelected(true);
            } else {
                ForPatients.setSelected(false);
            }

        }

    }

    public List<Integer> getAvailibleOrders(boolean isfeed_back, Object o) {
        ArrayList<Integer> d = new ArrayList<>();
        if (isfeed_back) {
            for (int i = 0; i < Orders.size(); i++) {
                if (Orders.get(i).isEmpty() || Orders.get(i).contains(o)) {
                    d.add(i);
                }
            }
        } else {
            for (int i = 0; i < Orders.size(); i++) {
                if (Orders.get(i).isEmpty() || Orders.get(i).contains(o) || (Orders.get(i).get(0) instanceof GetProcedure.ServicesGroup)) {
                    d.add(i);
                }
            }
        }
        return d;
    }

    public void transferOrder(int from, int to, Object o) {

        Orders.get(from).remove(o);
        Orders.get(to).add(o);

        for (OnOrderChanged i : listeners) {
            i.update();

        }
    }

    public void addOrder(Object o, int order) {
        while (order >= Orders.size()) {
            Orders.add(new ArrayList<>());
        }
        Orders.get(order).add(o);

        for (OnOrderChanged i : listeners) {
            i.update();
        }
    }

    public void removeOrder(Object o) {

        for (int i = 0; i < Orders.size(); i++) {
            List<Object> list = Orders.get(i);
            if (list.contains(o)) {
                list.remove(o);
                if (list.isEmpty()) {
                    Orders.remove(i);
                } else {
                    for (int y = 0; y < Orders.size(); y++) {
                        if (Orders.get(y).isEmpty()) {
                            Orders.remove(y);
                            break;
                        }
                    }
                }
                break;
            }
        }

        System.out.println(Orders.size() + " Size");
        for (OnOrderChanged i : listeners) {
            i.update();
        }

    }

    public GetProcedure.Procedure getProcedure() {
        return proc1;
    }

    static interface OnOrderChanged {

        public void update();
    }

    public void registerListener(OnOrderChanged listener) {

        this.listeners.add(listener);

    }

    public void unregisterListener(OnOrderChanged listener) {
        this.listeners.remove(listener);
    }

    public void addNewFeedBack() {
        FeedBack f = new FeedBack();
        GetProcedure.FeedBack feed = new GetProcedure.FeedBack();
        feed.req_id = Globals.generateRequestId(old == null ? proc1 : old);
        feed.med_team_id = 0;
        feed.med_team_name = "";
        feed.note_med = "";
        feed.state = -1;
        feed.order = Orders.size();
        proc1.feedbacks.add(feed);
        f.setData(proc1, feed, this);
        addOrder(feed, Orders.size());
        RequestContainer.add(f, 0);
        this.revalidate();
        this.repaint();

    }

    public void addExistingFeedBack(GetProcedure.FeedBack feedBack) {
        FeedBack f = new FeedBack();
        f.setData(proc1, feedBack, this);
        addOrder(feedBack, feedBack.order);
        RequestContainer.add(f, 0);
        this.revalidate();
        this.repaint();

    }

    public void addNewSmartFeed() {

        SmartFeedBack f = new SmartFeedBack();
        GetProcedure.SmartFeedBack feed = new GetProcedure.SmartFeedBack();
        feed.req_id = Globals.generateRequestId(old == null ? proc1 : old);
        feed.order = Orders.size();
        feed.services = new ArrayList<>();
        feed.note_med = "";
        feed.state = -1;
        proc1.smart_feedbacks.add(feed);
        f.setData(proc1, feed, this);
        addOrder(feed, Orders.size());
        RequestContainer.add(f, 0);
        this.revalidate();
        this.repaint();
    }

    public void addExsistingSmartFeed(GetProcedure.SmartFeedBack feedBack) {
        SmartFeedBack f = new SmartFeedBack();

        f.setData(proc1, feedBack, this);
        addOrder(feedBack, feedBack.order);
        RequestContainer.add(f, 0);
        this.revalidate();
        this.repaint();
    }

    public void addNewVisit() {

        ServiceRequest f = new ServiceRequest();
        GetProcedure.ServicesGroup service = new GetProcedure.ServicesGroup();
        service.req_id = Globals.generateRequestId(old == null ? proc1 : old);
        service.order = Orders.size();
        service.services = new ArrayList<>();
        service.rooms = new ArrayList<>();
        service.note_med = "";
        service.state = -1;
        proc1.services.add(service);
        f.setData(proc1, service, this);
        addOrder(service, Orders.size());
        RequestContainer.add(f, 0);
        this.revalidate();
        this.repaint();
    }

    public void addExistingService(GetProcedure.ServicesGroup ser) {

        ServiceRequest f = new ServiceRequest();
        f.setData(proc1, ser, this);
        addOrder(ser, ser.order);
        RequestContainer.add(f, 0);
        this.revalidate();
        this.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        name_text = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        note_patient_text = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        note_med_text = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        obj_text = new javax.swing.JTextArea();
        add_feed_btn = new javax.swing.JButton();
        add_smart_btn = new javax.swing.JButton();
        add_visit_btn = new javax.swing.JButton();
        RequestContainer = new javax.swing.JPanel();
        activationPeriod = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        Remove_btn = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        ForPatients = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Name");

        name_text.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N

        jLabel2.setText("activation period");

        jLabel3.setText("note for patient");

        note_patient_text.setColumns(20);
        note_patient_text.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        note_patient_text.setRows(5);
        jScrollPane1.setViewportView(note_patient_text);

        jLabel4.setText("<html>note for medical team members</html>");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel4.setInheritsPopupMenu(false);

        note_med_text.setColumns(20);
        note_med_text.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        note_med_text.setRows(5);
        jScrollPane2.setViewportView(note_med_text);

        jLabel5.setText("Objective");

        obj_text.setColumns(20);
        obj_text.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        obj_text.setRows(5);
        jScrollPane3.setViewportView(obj_text);

        add_feed_btn.setText("add FeedBack");
        add_feed_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddFeedClicked(evt);
            }
        });

        add_smart_btn.setText("add Smart-FeedBack");
        add_smart_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddSmartClicked(evt);
            }
        });

        add_visit_btn.setText("add Visit");
        add_visit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddVisitClicked(evt);
            }
        });

        RequestContainer.setLayout(new javax.swing.BoxLayout(RequestContainer, javax.swing.BoxLayout.Y_AXIS));

        activationPeriod.setModel(new javax.swing.SpinnerNumberModel());
        activationPeriod.setEditor(new javax.swing.JSpinner.NumberEditor(activationPeriod, "##"));

        jLabel6.setText("days");

        Remove_btn.setText("Remove");
        Remove_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Remove_btnActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(0, 0, 255));
        jLabel7.setText("Visit");

        ForPatients.setText("For Patients");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(name_text)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(activationPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(498, 498, 498))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(add_feed_btn)
                                .addGap(18, 18, 18)
                                .addComponent(add_smart_btn)
                                .addGap(18, 18, 18)
                                .addComponent(add_visit_btn))
                            .addComponent(RequestContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 851, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ForPatients, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Remove_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Remove_btn)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ForPatients))))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(name_text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(activationPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add_feed_btn)
                    .addComponent(add_smart_btn)
                    .addComponent(add_visit_btn))
                .addGap(28, 28, 28)
                .addComponent(RequestContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void onAddFeedClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddFeedClicked
        // TODO add your handling code here:
        addNewFeedBack();

    }//GEN-LAST:event_onAddFeedClicked

    private void onAddSmartClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddSmartClicked
        // TODO add your handling code here:
        addNewSmartFeed();

    }//GEN-LAST:event_onAddSmartClicked

    private void onAddVisitClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddVisitClicked
        // TODO add your handling code here:
        addNewVisit();
    }//GEN-LAST:event_onAddVisitClicked

    private void Remove_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Remove_btnActionPerformed
        // TODO add your handling code here:
        if (this.parent != null) {
            this.parent.attached_procedures.remove(this.proc1);
            Container pa = getParent();
            pa.remove(this);
            pa.revalidate();
            pa.repaint();
        }

    }//GEN-LAST:event_Remove_btnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox ForPatients;
    private javax.swing.JButton Remove_btn;
    private javax.swing.JPanel RequestContainer;
    private javax.swing.JSpinner activationPeriod;
    private javax.swing.JButton add_feed_btn;
    private javax.swing.JButton add_smart_btn;
    private javax.swing.JButton add_visit_btn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField name_text;
    private javax.swing.JTextArea note_med_text;
    private javax.swing.JTextArea note_patient_text;
    private javax.swing.JTextArea obj_text;
    // End of variables declaration//GEN-END:variables

    static {

        ArrayList<String> o = new ArrayList<>();
        for (int i = 1; i < 1000; i++) {

            if (i % 10 == 0 || i % 10 == 1) {

                o.add(i + "st");

            } else if (i % 10 == 2) {
                o.add(i + "nd");

            } else if (i % 10 == 3) {
                o.add(i + "rd");

            } else {
                o.add(i + "th");
            }

        }
        Order_Labels = new String[o.size()];

        int x = 0;
        while (!o.isEmpty()) {

            Order_Labels[x] = o.remove(0);
            x++;

        }

    }
}
