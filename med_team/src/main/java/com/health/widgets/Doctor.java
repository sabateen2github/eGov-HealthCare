/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.widgets;

import com.health.objects.GetActiveFeedbackList;
import com.health.objects.GetProcedure;
import com.health.project.medteam.Globals;
import static com.health.project.medteam.Globals.activeFeedBacks;
import static com.health.project.medteam.Globals.med_id;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Inspiron
 */
public class Doctor extends javax.swing.JPanel implements Globals.Updater {

    /**
     * Creates new form Doctor
     */
    RoomDoctor roomDoctor;
    SmartDoctor smartDoctor;

    public Doctor() {
        initComponents();

        Globals.executor.execute(new Runnable() {
            @Override
            public void run() {

                try {

                    final List<GetActiveFeedbackList.Patient> activeFeedBacks = GetActiveFeedbackList.getActiveFeedBack(Globals.med_id);

                    if (activeFeedBacks.size() != Globals.activeFeedBacks.size()) {

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                Globals.activeFeedBacks = activeFeedBacks;
                                FeedBackContainer.removeAll();
                                if (Globals.activeFeedBacks.size() == 0) {
                                    ActiveFeed.setText("You dont have any feedbacks from any patient right now");
                                }
                                for (int i = 0; i < Globals.activeFeedBacks.size(); i++) {
                                    PatientFeedBack p = new PatientFeedBack();
                                    p.setData(Globals.activeFeedBacks.get(i));
                                    FeedBackContainer.add(p);
                                }
                                FeedBackContainer.revalidate();
                                FeedBackContainer.repaint();
                            }
                        });
                    }

                } catch (Exception e) {
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Doctor.class.getName()).log(Level.SEVERE, null, ex);
                }

                Globals.executor.execute(this);
            }
        });

        if (Globals.activeFeedBacks.size() == 0) {
            ActiveFeed.setText("You dont have any feedbacks from any patient right now");
        }
        for (int i = 0; i < Globals.activeFeedBacks.size(); i++) {
            FeedBackContainer.removeAll();
            PatientFeedBack p = new PatientFeedBack();
            p.setData(Globals.activeFeedBacks.get(i));
            FeedBackContainer.add(p);
        }

        SmartSystem.setSelected(true);

        Container.removeAll();
        smartDoctor = new SmartDoctor();
        Container.add(new SmartDoctor());

        FeedBackContainer.removeAll();
        for (int i = 0; i < Globals.activeFeedBacks.size(); i++) {
            PatientFeedBack p = new PatientFeedBack();
            p.setData(Globals.activeFeedBacks.get(i));
            FeedBackContainer.add(p);
        }

    }

    @Override
    public void update() {
        FeedBackContainer.removeAll();
        for (int i = 0; i < Globals.activeFeedBacks.size(); i++) {
            PatientFeedBack p = new PatientFeedBack();
            p.setData(Globals.activeFeedBacks.get(i));
            FeedBackContainer.add(p);
        }

        revalidate();
        repaint();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        ActiveFeed = new javax.swing.JLabel();
        FeedBackContainer = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        SmartSystem = new javax.swing.JRadioButton();
        Institutions = new javax.swing.JRadioButton();
        Container = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1000, 720));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(1200, 700));

        ActiveFeed.setText("Active FeedBacks");

        FeedBackContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        FeedBackContainer.setLayout(new javax.swing.BoxLayout(FeedBackContainer, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setMinimumSize(new java.awt.Dimension(0, 48));

        buttonGroup1.add(SmartSystem);
        SmartSystem.setText("Smart-System");
        SmartSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SmartSystemActionPerformed(evt);
            }
        });

        buttonGroup1.add(Institutions);
        Institutions.setText("Institutions");
        Institutions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InstitutionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Institutions, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SmartSystem, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SmartSystem)
                    .addComponent(Institutions))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        Container.setMaximumSize(new java.awt.Dimension(0, 0));
        Container.setLayout(new javax.swing.BoxLayout(Container, javax.swing.BoxLayout.Y_AXIS));

        jButton1.setText("Open Patient Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(FeedBackContainer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 948, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 38, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Container, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ActiveFeed, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ActiveFeed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(FeedBackContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Container, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void SmartSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SmartSystemActionPerformed
        // TODO add your handling code here:
        if (smartDoctor == null) {
            smartDoctor = new SmartDoctor();
        }
        Container.removeAll();
        Container.add(new SmartDoctor());
        Container.revalidate();
        Container.repaint();
        System.out.println("dasdsadsadaadsadsa");
    }//GEN-LAST:event_SmartSystemActionPerformed

    private void InstitutionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InstitutionsActionPerformed
        // TODO add your handling code here:
        if (roomDoctor == null) {
            roomDoctor = new RoomDoctor();
        }
        Container.removeAll();
        Container.add(roomDoctor);
        Container.revalidate();
        Container.repaint();
    }//GEN-LAST:event_InstitutionsActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:        
        Globals.openPatientProfile();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ActiveFeed;
    private javax.swing.JPanel Container;
    private javax.swing.JPanel FeedBackContainer;
    private javax.swing.JRadioButton Institutions;
    private javax.swing.JRadioButton SmartSystem;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

}
