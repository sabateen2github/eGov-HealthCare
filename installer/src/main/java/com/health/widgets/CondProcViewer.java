/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.widgets;

import com.health.objects.GetProcedure;
import com.health.objects.Types;

/**
 *
 * @author Inspiron
 */
public class CondProcViewer extends javax.swing.JPanel {

    /**
     * Creates new form CondProcViewer
     */
    public CondProcViewer() {
        initComponents();
        ProcedureViewer_Var.setActiveDetailsVisibility(false);
    }

    public void setData(GetProcedure.Procedure proc) {

        ProcedureViewer_Var.setData(proc);
        DelayPeriod.setText(proc.delay_period + " Days");
        if (proc.type == Types.Type_Procedure_Condtion_Routine) {
            routine_checkbox.setSelected(true);
            Frequency.setEnabled(false);
            Frequency_Label.setEnabled(false);
            Routine_Period_Label.setEnabled(false);
            Routine.setEnabled(false);

            Routine.setText("Not Specified");
            Frequency.setText("Not Specified");
            

        } else {

             routine_checkbox.setSelected(false);
            Frequency.setEnabled(true);
            Frequency_Label.setEnabled(true);
            Routine_Period_Label.setEnabled(true);
            Routine.setEnabled(true);
            Routine.setText(proc.routine_activation_period+" Days");
            Frequency.setText((proc.routine_activation_period/proc.cycle_period)+" Days");
            
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        routine_checkbox = new javax.swing.JCheckBox();
        Routine_Period_Label = new javax.swing.JLabel();
        Frequency_Label = new javax.swing.JLabel();
        Frequency = new javax.swing.JLabel();
        Routine = new javax.swing.JLabel();
        Container = new javax.swing.JPanel();
        ProcedureViewer_Var = new com.health.widgets.ProcedureViewer();
        DelayPeriod = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Delay Period");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        routine_checkbox.setSelected(true);
        routine_checkbox.setText("Not Routine");
        routine_checkbox.setEnabled(false);

        Routine_Period_Label.setText("Routine Period");
        Routine_Period_Label.setEnabled(false);

        Frequency_Label.setText("Frequency");
        Frequency_Label.setEnabled(false);

        Frequency.setText("not specified");
        Frequency.setEnabled(false);

        Routine.setText("not specified");
        Routine.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Frequency)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Routine)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                        .addComponent(routine_checkbox)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Frequency_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                        .addComponent(Routine_Period_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                    .addContainerGap(327, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(routine_checkbox)
                    .addComponent(Routine, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(Frequency, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                .addGap(27, 27, 27))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addComponent(Routine_Period_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Frequency_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        Container.setLayout(new javax.swing.BoxLayout(Container, javax.swing.BoxLayout.Y_AXIS));
        Container.add(ProcedureViewer_Var);

        DelayPeriod.setText("20 Days");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(DelayPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DelayPeriod)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Container, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Container;
    private javax.swing.JLabel DelayPeriod;
    private javax.swing.JLabel Frequency;
    private javax.swing.JLabel Frequency_Label;
    private com.health.widgets.ProcedureViewer ProcedureViewer_Var;
    private javax.swing.JLabel Routine;
    private javax.swing.JLabel Routine_Period_Label;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JCheckBox routine_checkbox;
    // End of variables declaration//GEN-END:variables
}
