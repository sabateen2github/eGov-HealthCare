/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.widgets;

import com.health.installer.fund.utils.Util;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetProcedure;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Inspiron
 */
public class SmartFeedBack extends javax.swing.JPanel implements Procedure.OnOrderChanged, ServiceWgt.onSelectionChangedListener {

    /**
     * Creates new form SmartFeedBack
     */
    private GetProcedure.SmartFeedBack mFeedback;
    private GetProcedure.Procedure mProcedure;
    private Procedure mParent;

    private int current_pos = -1;
    private List<Integer> indexes;

    public SmartFeedBack() {
        initComponents();
        rooms_wgt.registerListener(this);
        note.getDocument().addDocumentListener(new DocumentListener() {
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

                mFeedback.note_med = note.getText();
            }
        });

    }

    @Override
    public void update(List<GetAvailibleServices.Service> services) {

        if (mFeedback == null) {
            return;
        }
        mFeedback.services.clear();
        mFeedback.services.addAll(services);
    }

    public void setData(GetProcedure.Procedure procedure, GetProcedure.SmartFeedBack feedBack, Procedure parent) {

        mProcedure = procedure;
        mFeedback = feedBack;
        mParent = parent;
        mParent.registerListener(this);
        note.setText(feedBack.note_med);

        for (int i = 0; i < feedBack.services.size(); i++) {
            List<Object> objs = new ArrayList<>();
            Util.getAllObjects((DefaultMutableTreeNode) rooms_wgt.Services.getModel().getRoot(), objs);

            for (Object d : objs) {
                if (d instanceof GetAvailibleServices.Service) {
                    if (((GetAvailibleServices.Service) d).service_id == feedBack.services.get(i).service_id) {
                        rooms_wgt.Services.toggleNode(Util.findNode((DefaultMutableTreeNode) rooms_wgt.Services.getModel().getRoot(), d));
                    }
                }
            }
        }
        
        
        

    }

    @Override
    public void update() {
        int pos = -1;
        for (int i = 0; i < mParent.Orders.size(); i++) {
            if (mParent.Orders.get(i).contains(mFeedback)) {
                pos = i;
                break;
            }
        }
        this.current_pos = pos;
        indexes = mParent.getAvailibleOrders(true, mFeedback);
        String[] labels = new String[indexes.size()];
        for (int i = 0; i < indexes.size(); i++) {
            labels[i] = Procedure.Order_Labels[indexes.get(i)];
        }

        DefaultComboBoxModel<String> t = new DefaultComboBoxModel<String>(labels);
        order_list.setModel(t);
        order_list.setSelectedIndex(indexes.indexOf(pos));
        mFeedback.order = pos;
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
        rooms_wgt = new com.health.widgets.ServiceWgt();
        remove_btn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        note = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        order_list = new javax.swing.JComboBox<>();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Smart FeedBack");

        remove_btn.setText("Remove");
        remove_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                remove_btnActionPerformed(evt);
            }
        });

        note.setColumns(20);
        note.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        note.setRows(5);
        jScrollPane2.setViewportView(note);

        jLabel6.setText("Note");

        jLabel3.setText("Order");

        order_list.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        order_list.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                order_listActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(rooms_wgt, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)))
                        .addGap(0, 55, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(order_list, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(remove_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(remove_btn))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(order_list, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(rooms_wgt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void remove_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_remove_btnActionPerformed
        // TODO add your handling code here:
        mProcedure.smart_feedbacks.remove(mFeedback);
        mParent.unregisterListener(this);
        mParent.removeOrder(mFeedback);
        Container c = getParent();
        c.remove(this);
        c.revalidate();
        c.repaint();
    }//GEN-LAST:event_remove_btnActionPerformed

    private void order_listActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_order_listActionPerformed
        // TODO add your handling code here:

        int pos = order_list.getSelectedIndex();
        if (pos != indexes.indexOf(current_pos)) {
            mParent.transferOrder(current_pos, indexes.get(pos), mFeedback);
        }
    }//GEN-LAST:event_order_listActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea note;
    private javax.swing.JComboBox<String> order_list;
    private javax.swing.JButton remove_btn;
    private com.health.widgets.ServiceWgt rooms_wgt;
    // End of variables declaration//GEN-END:variables

}
