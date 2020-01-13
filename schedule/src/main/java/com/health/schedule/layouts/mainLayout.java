/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.schedule.layouts;

import com.health.baseobjects.JCheckBoxTree2;
import com.health.objects.GetAffectedAppointments;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetRoomsAppointments;
import com.health.objects.InstallSchedules;
import com.health.schedule.handlers.RoomsHandler;
import com.health.schedule.handlers.ScheduleHandler;
import com.health.schedule.main.Util;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Inspiron
 */
public class mainLayout extends javax.swing.JPanel {

    DefaultTreeModel model_services;
    DefaultTreeModel model_rooms;
    DefaultMutableTreeNode root_services;
    DefaultMutableTreeNode root_rooms;

    private List<GetAvailibleRooms.Room> choosed_rooms = new ArrayList<>();
    private List<GetAvailibleServices.Service> choosed_services = new ArrayList<>();

    /**
     * Creates new form mainLayout
     */
    public mainLayout() {
        initComponents();
        initTrees();

        Install.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String From = FromDate.getText();
                String To = ToDate.getText();
                if (From != null && From.split("/").length == 3) {

                    From = From.replace("/", "-");
                    try {
                        com.health.schedule.handlers.Util.Date d = new com.health.schedule.handlers.Util.Date(From + "-0-0");
                    } catch (Exception exe) {
                        exe.printStackTrace();
                        Util.showMsg("Please Enter A Valid From Format");
                        return;
                    }
                } else {
                    Util.showMsg("Please Enter A Valid From Format");
                    return;
                }

                if (To != null && To.split("/").length == 3) {
                    To = To.replace("/", "-");
                    try {
                        com.health.schedule.handlers.Util.Date d = new com.health.schedule.handlers.Util.Date(To + "-0-0");
                    } catch (Exception exe) {
                        Util.showMsg("Please Enter A Valid To Format");
                        return;
                    }
                } else {
                    Util.showMsg("Please Enter A Valid To Format");
                    return;
                }

                if (!callCheckers()) {
                    return;
                }

                ScheduleHandler.setFrom(From);
                ScheduleHandler.setTo(To);

                List<GetAffectedAppointments.AffectedAppointment> affected = ScheduleHandler.getAffected(choosed_rooms);
                if (affected.size() > 0) {

                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < affected.size(); i++) {
                        GetAffectedAppointments.AffectedAppointment a = affected.get(i);
                        builder.append("\n");
                        builder.append("Room : " + a.room_path);
                        builder.append("\n");
                        builder.append("Date : " + a.date);
                        builder.append("\n");
                        builder.append("From : " + a.from);
                        builder.append("\n");
                        builder.append("To : " + a.to);
                        builder.append("\n");
                        builder.append("Capacity : " + a.capacity);
                        builder.append("\n");
                        builder.append("Patients Count : " + a.patientsCount);
                        builder.append("\n");
                        builder.append("\n");
                    }

                    String msg = builder.toString();
                    Util.showMsg(msg, new Runnable() {
                        @Override
                        public void run() {
                            install();
                        }
                    });

                } else {
                    install();
                }

            }
        }
        );

        AddDay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewDay();
                if (children.size() == 7) {
                    AddDay.setEnabled(false);
                }
            }
        });
    }

    private HashMap<String, Appointment> children = new HashMap<>();

    private void updatechildren() {
        String[] h = ScheduleHandler.getUnusedDaysS();
        for (int i = 1; i < 8; i++) {
            String key = ScheduleHandler.Days.get(i);
            Appointment f = children.get(key);
            if (f != null) {
                f.getComboBox().setModel(new DefaultComboBoxModel<String>(Util.addToArray(key, h)));
                f.getComboBox().setSelectedIndex(h.length);
            }
        }
    }

    private void addNewDay() {

        String[] h1 = ScheduleHandler.getUnusedDaysS();
        String day = h1[0];
        ScheduleHandler.setDay(ScheduleHandler.DaysInv.get(day), 20, "8:00", "14:00");
        final Appointment appointment = new Appointment();
        children.put(day, appointment);

        final Checker checker = new Checker() {
            @Override
            public boolean check() {
                int cap = 0;
                try {
                    cap = Integer.parseInt(appointment.getCapacity().getText());
                } catch (Exception exe) {
                    Util.showMsg("Please Enter A Valid Capacity (Numbers Only)");
                    return false;
                }

                if (cap == 0) {
                    Util.showMsg("Please Enter A Valid Capacity (Numbers Only)");
                    return false;
                }

                String From = appointment.getFrom().getText();
                String To = appointment.getTo().getText();

                try {
                    String[] split = From.split(":");
                    if (split.length != 2) {
                        throw new Exception();
                    }

                    Integer.parseInt(split[0]);
                    Integer.parseInt(split[1]);

                } catch (Exception exe) {
                    Util.showMsg("Please Enter A Valid (From Time) Format (HH:MM)");
                    return false;
                }
                try {
                    String[] split = To.split(":");
                    if (split.length != 2) {
                        throw new Exception();
                    }

                    Integer.parseInt(split[0]);
                    Integer.parseInt(split[1]);

                } catch (Exception exe) {
                    Util.showMsg("Please Enter A Valid (To Time) Format (HH:MM)");
                    return false;
                }
                return true;
            }
        };

        registerChecker(checker);

        appointment.getComboBox().addActionListener(new ActionListener() {
            String old = null;

            @Override
            public void actionPerformed(ActionEvent e) {
                String day = (String) appointment.getComboBox().getSelectedItem();
                if (!day.equals(old)) {
                    if (old != null) {
                        ScheduleHandler.Day d = ScheduleHandler.getDay(ScheduleHandler.DaysInv.get(old));
                        ScheduleHandler.removeDay(ScheduleHandler.DaysInv.get(old));
                        ScheduleHandler.setDay(ScheduleHandler.DaysInv.get(day), d.capacity, d.from, d.to);
                        children.remove(old);
                        children.put(day, appointment);
                        updatechildren();
                    }
                    old = day;
                }
            }
        });

        appointment.getRemove().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String day = (String) appointment.getComboBox().getSelectedItem();
                ScheduleHandler.removeDay(ScheduleHandler.DaysInv.get(day));
                children.remove(day);
                DayContainer.remove(appointment);
                DayContainer.revalidate();
                DayContainer.repaint();
                removeChecker(checker);
                AddDay.setEnabled(true);
                updatechildren();
            }
        });

        DocumentListener docLis = new DocumentListener() {
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

            private void update() {
                String day = (String) appointment.getComboBox().getSelectedItem();

                int cap = 0;
                try {
                    cap = Integer.parseInt(appointment.getCapacity().getText());
                } catch (Exception exe) {
                }
                ScheduleHandler.setDay(ScheduleHandler.DaysInv.get(day), cap, appointment.getFrom().getText().replace(":", "-"), appointment.getTo().getText().replace(":", "-"));
            }

        };

        appointment.getFrom().getDocument().addDocumentListener(docLis);
        appointment.getTo().getDocument().addDocumentListener(docLis);
        appointment.getCapacity().getDocument().addDocumentListener(docLis);

        updatechildren();
        DayContainer.add(appointment);
        DayContainer.revalidate();
        DayContainer.repaint();

    }

    private List<Checker> checkers;

    {
        checkers = new ArrayList<>();
    }

    private boolean callCheckers() {

        for (int y = 0; y < checkers.size(); y++) {
            if (!checkers.get(y).check()) {
                return false;
            }
        }

        return true;
    }

    private static interface Checker {

        public boolean check();
    }

    private void registerChecker(Checker check) {
        checkers.add(check);
    }

    private void removeChecker(Checker check) {
        checkers.remove(check);
    }

    private void install() {
        for (int i = 0; i < choosed_rooms.size(); i++) {
            GetAvailibleRooms.Room room = choosed_rooms.get(i);
            List<GetRoomsAppointments.Appointment> apps = ScheduleHandler.translateSchedule(room);
            InstallSchedules.InstallSchedule(apps, room.room_id);
        }
        Util.showMsg("Schedule Installed Succesfully");
    }

    private void initTrees() {

        root_services = Util.getServiceNodes();
        root_rooms = Util.getRoomsNodes();
        model_rooms = new DefaultTreeModel(root_rooms);
        model_services = new DefaultTreeModel(root_services);
        ServicesTree.setModel(model_services);
        RoomsTree.setModel(model_rooms);

        ServicesTree.addCheckChangeEventListener(new JCheckBoxTree2.CheckChangeEventListener() {
            @Override
            public void checkStateChanged(JCheckBoxTree2.CheckChangeEvent event) {
                JCheckBoxTree2.Data data = (JCheckBoxTree2.Data) event.getSource();

                if (data.is_checked) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getLastPathComponent();
                    Util.getAllObjects(node, choosed_services);
                } else {
                    ArrayList<GetAvailibleServices.Service> services = new ArrayList<>();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getLastPathComponent();
                    Util.getAllObjects(node, services);
                    choosed_services.removeAll(services);
                }

                List<GetAvailibleRooms.Room> matched = RoomsHandler.getRoomsForServices(choosed_services);
                List<GetAvailibleRooms.Room> current = new ArrayList<>();
                Util.getAllObjects(root_rooms, current);

                for (int i = 0; i < current.size(); i++) {
                    if (!matched.contains(current.get(i))) {
                        Util.remove_node(root_rooms, current.get(i));
                        choosed_rooms.remove(current.get(i));
                    }
                }
                for (int i = 0; i < matched.size(); i++) {
                    if (!current.contains(matched.get(i))) {
                        Util.add_node(root_rooms, matched.get(i).room_path, matched.get(i));
                    }
                }
                Util.reload(RoomsTree);
            }
        });
        RoomsTree.addCheckChangeEventListener(new JCheckBoxTree2.CheckChangeEventListener() {
            @Override
            public void checkStateChanged(JCheckBoxTree2.CheckChangeEvent event) {
                JCheckBoxTree2.Data data = (JCheckBoxTree2.Data) event.getSource();
                if (data.is_checked) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getLastPathComponent();
                    Util.getAllObjects(node, choosed_rooms);
                } else {
                    ArrayList<GetAvailibleRooms.Room> rooms = new ArrayList<>();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getLastPathComponent();
                    Util.getAllObjects(node, rooms);
                    choosed_rooms.removeAll(rooms);
                }
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Install = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        RoomsTree = new com.health.baseobjects.JCheckBoxTree2();
        jScrollPane3 = new javax.swing.JScrollPane();
        ServicesTree = new com.health.baseobjects.JCheckBoxTree2();
        jScrollPane1 = new javax.swing.JScrollPane();
        DayContainer = new javax.swing.JPanel();
        AddDay = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        FromDate = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ToDate = new javax.swing.JTextField();

        Install.setText("Install Schedule");

        jLabel1.setText("Services");

        jLabel2.setText("Rooms");

        jScrollPane2.setViewportView(RoomsTree);

        jScrollPane3.setViewportView(ServicesTree);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        DayContainer.setLayout(new javax.swing.BoxLayout(DayContainer, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(DayContainer);

        AddDay.setText("Add Day to Schedule");

        jLabel3.setText("From  (yyyy/mm/dd)");

        jLabel4.setText("To  (yyyy/mm/dd)");
        jLabel4.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 520, Short.MAX_VALUE)
                        .addComponent(AddDay, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                                .addComponent(jScrollPane2))
                            .addComponent(Install, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(FromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(ToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(AddDay))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addComponent(Install))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddDay;
    private javax.swing.JPanel DayContainer;
    private javax.swing.JTextField FromDate;
    private javax.swing.JButton Install;
    private com.health.baseobjects.JCheckBoxTree2 RoomsTree;
    private com.health.baseobjects.JCheckBoxTree2 ServicesTree;
    private javax.swing.JTextField ToDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
