/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.widgets;

import com.health.advanced.Handler;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import com.health.project.medteam.Globals;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Inspiron
 */
public class HealthRecordDocker extends javax.swing.JPanel {

    /**
     * Creates new form HealthRecordDocker
     */
    public HealthRecordDocker() {
        initComponents();

        ProcedureDocker proc = new ProcedureDocker();
        proc.setData(Globals.current_procedure, TabPane);
        ActiveCondtions condtions = new ActiveCondtions();
        condtions.setData();
        PersonalDataViewer personalDataViewer = new PersonalDataViewer();
        personalDataViewer.setData(Globals.patient_current);

        TabPane.add("Personal Data", personalDataViewer);
        if (Globals.current_procedure != null) {
            TabPane.add("Active Visit", proc);
        }
        TabPane.add("Active Condtions", condtions);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TabPane = new javax.swing.JTabbedPane();
        CreateNewCondtion = new javax.swing.JButton();
        CreateNewVisit = new javax.swing.JButton();
        SendMsg = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(32767, 800));
        setPreferredSize(new java.awt.Dimension(1480, 700));
        setRequestFocusEnabled(false);

        CreateNewCondtion.setText("Create New Condtion");
        CreateNewCondtion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateNewCondtionActionPerformed(evt);
            }
        });

        CreateNewVisit.setText("Issue Visit");
        CreateNewVisit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateNewVisitActionPerformed(evt);
            }
        });

        SendMsg.setText("Send Msg");
        SendMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendMsgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CreateNewCondtion, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(CreateNewVisit, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(SendMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(434, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(TabPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(TabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CreateNewVisit)
                    .addComponent(CreateNewCondtion)
                    .addComponent(SendMsg)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void CreateNewCondtionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateNewCondtionActionPerformed
        // TODO add your handling code here:

        Handler d = new Handler();
        d.chooseCondtion(new Handler.ChoosedCond() {
            @Override
            public void onChoose(GetCondtion.Condtion cond) {
                final JFrame frame = new JFrame("Condtion");

                Globals.startCondtion = new WeakReference<>(frame);
                CondtionContainer container = new CondtionContainer();
                container.setData(cond);
                frame.getContentPane().add(container, BorderLayout.CENTER);
                Dimension d = new Dimension(1200, 800);
                frame.setSize(d);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
                frame.pack();
            }
        });


    }//GEN-LAST:event_CreateNewCondtionActionPerformed

    private void CreateNewVisitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateNewVisitActionPerformed
        // TODO add your handling code here:

        Handler d = new Handler();
        d.chooseProcedure(new Handler.ChoosedProc() {
            @Override
            public void onChoose(GetProcedure.Procedure proc) {
                final JFrame frame = new JFrame("Procedure");
                Globals.startProc = new WeakReference<>(frame);

                ProcedureTrigger container = new ProcedureTrigger();
                container.setData(proc);
                frame.getContentPane().add(container, BorderLayout.CENTER);
                Dimension d = new Dimension(1200, 800);
                frame.setSize(d);

                frame.setLocationRelativeTo(null);
                frame.setResizable(false);
                frame.setVisible(true);
                frame.pack();
            }
        });

    }//GEN-LAST:event_CreateNewVisitActionPerformed

    private void SendMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendMsgActionPerformed
        // TODO add your handling code here:
        Globals.showSendMsg("");
    }//GEN-LAST:event_SendMsgActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CreateNewCondtion;
    private javax.swing.JButton CreateNewVisit;
    private javax.swing.JButton SendMsg;
    private javax.swing.JTabbedPane TabPane;
    // End of variables declaration//GEN-END:variables
}
