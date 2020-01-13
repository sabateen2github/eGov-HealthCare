/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.widgets;

import com.health.baseobjects.JCheckBoxTree2;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.schedule.handlers.RoomsHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ServiceRoomWgt extends javax.swing.JPanel {

    DefaultTreeModel model_services;
    DefaultTreeModel model_rooms;
    DefaultMutableTreeNode root_services;
    DefaultMutableTreeNode root_rooms;

    private List<GetAvailibleRooms.Room> choosed_rooms = new ArrayList<>();
    private List<GetAvailibleServices.Service> choosed_services = new ArrayList<>();

    /**
     * Creates new form ServiceRoomWgt
     *
     * @return
     */
    public List<GetAvailibleServices.Service> getServices() {
        return choosed_services;
    }

    public List<GetAvailibleRooms.Room> getRooms() {

        return choosed_rooms;
    }

    public ServiceRoomWgt() {
        initComponents();
        initTrees();

    }

    private List<onSelectionChanged> listeners = new ArrayList<>();

    public void registerListener(onSelectionChanged lis) {

        listeners.add(lis);

    }

    public void unregisterListener(onSelectionChanged lis) {

        listeners.remove(lis);
    }

    private void initTrees() {

        root_services = Util.getServiceNodes();
        root_rooms = Util.getRoomsNodes();
        model_rooms = new DefaultTreeModel(root_rooms);
        model_services = new DefaultTreeModel(root_services);
        Services.setModel(model_services);
        Rooms.setModel(model_rooms);

        Services.addCheckChangeEventListener(new JCheckBoxTree2.CheckChangeEventListener() {
            @Override
            public void checkStateChanged(JCheckBoxTree2.CheckChangeEvent event) {

                JCheckBoxTree2.Data data = (JCheckBoxTree2.Data) event.getSource();

                if (data.path.getLastPathComponent() == Services.getModel().getRoot()) {
                    Services.toggleRoot();
                    return;
                }

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
                Util.reload(Rooms);
                for (onSelectionChanged d : listeners) {
                    d.newData(choosed_services, choosed_rooms);
                }
            }
        });
        Rooms.addCheckChangeEventListener(new JCheckBoxTree2.CheckChangeEventListener() {
            @Override
            public void checkStateChanged(JCheckBoxTree2.CheckChangeEvent event) {
                JCheckBoxTree2.Data data = (JCheckBoxTree2.Data) event.getSource();
                if (data.path.getLastPathComponent() == Rooms.getModel().getRoot()) {
                    Rooms.toggleRoot();
                    return;
                }
                if (data.is_checked) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getLastPathComponent();
                    Util.getAllObjects(node, choosed_rooms);
                } else {
                    ArrayList<GetAvailibleRooms.Room> rooms = new ArrayList<>();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getLastPathComponent();
                    Util.getAllObjects(node, rooms);
                    choosed_rooms.removeAll(rooms);
                }
                for (onSelectionChanged d : listeners) {
                    d.newData(choosed_services, choosed_rooms);
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

        service_label = new javax.swing.JLabel();
        service_label1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Rooms = new com.health.baseobjects.JCheckBoxTree2();
        jScrollPane4 = new javax.swing.JScrollPane();
        Services = new com.health.baseobjects.JCheckBoxTree2();

        service_label.setText("services");

        service_label1.setText("Matched Rooms");

        jScrollPane3.setViewportView(Rooms);

        jScrollPane4.setViewportView(Services);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(service_label)
                    .addComponent(service_label1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(service_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(service_label1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public static interface onSelectionChanged {

        public void newData(List<GetAvailibleServices.Service> services, List<GetAvailibleRooms.Room> rooms);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public com.health.baseobjects.JCheckBoxTree2 Rooms;
    public com.health.baseobjects.JCheckBoxTree2 Services;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel service_label;
    private javax.swing.JLabel service_label1;
    // End of variables declaration//GEN-END:variables
}
