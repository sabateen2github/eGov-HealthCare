/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.widgets;

import com.health.objects.GetCondtion;

/**
 *
 * @author Inspiron
 */
public class MedicationControllerOrdinary extends javax.swing.JPanel {

    /**
     * Creates new form MedicationControllerOrdinary
     */
    private GetCondtion.Condtion cond;

    public MedicationControllerOrdinary() {
        initComponents();
    }

    public void setData(GetCondtion.Condtion cond, boolean is_new) {
        this.cond = cond;
        if (!is_new) {

            for (int i = 0; i < cond.medication_ordinary.size(); i++) {
                addMedication(cond.medication_ordinary.get(i));
            }
        }
    }

    public void addNewMedication() {

        GetCondtion.MedicationOrdinary med = new GetCondtion.MedicationOrdinary();
        cond.medication_ordinary.add(med);
        MedicationOrdinary p = new MedicationOrdinary();
        p.setData(cond, med, true);
        Container.add(p);
        Container.revalidate();
        Container.repaint();

    }

    public void addMedication(GetCondtion.MedicationOrdinary med) {

        MedicationOrdinary p = new MedicationOrdinary();
        p.setData(cond, med, false);
        Container.add(p);
        Container.revalidate();
        Container.repaint();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Add = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        Container = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        Add.setText("Add");
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });

        jLabel1.setText("Medications");

        Container.setLayout(new javax.swing.BoxLayout(Container, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(63, 63, 63)
                        .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 610, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Add))
                .addGap(18, 18, 18)
                .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        // TODO add your handling code here:
        addNewMedication();
    }//GEN-LAST:event_AddActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JPanel Container;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
