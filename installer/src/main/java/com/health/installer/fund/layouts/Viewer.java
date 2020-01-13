/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.layouts;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Inspiron
 */
public class Viewer extends javax.swing.JPanel {

    /**
     * Creates new form Viewer
     */
    public Viewer() {
        initComponents();
        jTree1.setEditable(false);
    }

    public JTree getTree() {
        return this.jTree1;
    }

    public void setTitleBtn(String title) {
        sad.setText(title);
    }

    public void setTitle(String title) {
        Title.setText(title);
    }

    private ActionListener lis;

    public void setButtonListener(ActionListener listener) {
        if (lis != null) {
            sad.removeActionListener(lis);
        }
        lis = listener;
        sad.addActionListener(lis);
    }

    public static abstract class DoubleClickListener extends MouseAdapter {

        Viewer v;

        public DoubleClickListener(Viewer viewer) {
            v = viewer;
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            int row = v.getTree().getRowForLocation(event.getX(), event.getY());
            TreePath tree = v.getTree().getPathForRow(row);
            if (((TreeNode) tree.getLastPathComponent()).getChildCount() == 0 && event.getClickCount() == 2) {
                //double click
                onDoubleClick();
            }

        }

        public abstract void onDoubleClick();
    }

    public void reload() {
        List<TreePath> expanded = new ArrayList<>();

        for (int i = 0; i < jTree1.getRowCount() - 1; i++) {
            TreePath currPath = jTree1.getPathForRow(i);
            TreePath nextPath = jTree1.getPathForRow(i + 1);
            if (currPath.isDescendant(nextPath)) {
                expanded.add(currPath);
            }
        }
        ((DefaultTreeModel) jTree1.getModel()).reload();
        for (TreePath path : expanded) {
            jTree1.expandPath(path);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        Title = new javax.swing.JLabel();
        sad = new javax.swing.JButton();

        jScrollPane1.setViewportView(jTree1);

        Title.setText("Double Click the Item to edit");
        Title.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Title.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        sad.setText("jButton1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(Title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(sad, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Title, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sad)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Title;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton sad;
    // End of variables declaration//GEN-END:variables
}
