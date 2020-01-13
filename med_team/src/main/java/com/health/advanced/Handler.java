/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.advanced;

import com.health.installer.fund.handler.CondtionHandler;
import com.health.installer.fund.handler.ProcedureHandler;
import com.health.installer.fund.utils.Util;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import com.health.objects.Types;
import com.health.project.medteam.Globals;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Inspiron
 */
public class Handler {

    public static interface ChoosedProc {

        public void onChoose(GetProcedure.Procedure proc);

    }

    public static interface ChoosedCond {

        public void onChoose(GetCondtion.Condtion cond);

    }

    private GetCondtion.Condtion sel;

    public void chooseCondtion(ChoosedCond li) {
        final CondtionTempViewer vi = new CondtionTempViewer();
        DefaultTreeModel model;
        List<String> paths = new ArrayList<>();
        paths.add("new Condtion");
        for (int i = 0; i < Globals.templatesCondtions.size(); i++) {
            paths.add(Globals.templatesCondtions.get(i).name);
        }

        List<GetCondtion.Condtion> cc = new ArrayList<>();
        cc.add(CondtionHandler.createInstance(null));
        cc.addAll(Globals.templatesCondtions);
        cc.get(0).condtion_id = -1999628;
        sel = cc.get(0);
        model = new DefaultTreeModel(Util.generateNodes(paths, cc));
        vi.getCondList().setModel(model);

        vi.getCondList().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (vi.getCondList().getSelectionPath() == null) {
                    return;
                }
                List<GetCondtion.Condtion> objects = new ArrayList<>();
                Util.getAllObjects((DefaultMutableTreeNode) vi.getCondList().getSelectionPath().getLastPathComponent(), objects);
                if (objects.size() == 1) {
                    sel = objects.get(0);
                    updateCondSel(vi);
                }
            }
        });

        final JFrame frame = Util.showPanel(vi);
        vi.getContinue()
                .addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sel = CondtionHandler.makeCopy(sel);
                        sel.state = Types.Type_Template;

                        if (sel != cc.get(0)) {
                            List<Long> ids = new ArrayList<>();
                            for (int i = 0; i < Globals.templatesCondtions.size(); i++) {
                                ids.add(Globals.templatesCondtions.get(i).condtion_id);
                            }
                            sel.condtion_id = Util.generateID(ids);
                        }
                        li.onChoose(sel);
                        frame.dispose();
                    }
                }
                );
        updateCondSel(vi);
    }

    private void updateCondSel(CondtionTempViewer vi) {

        if (sel.condtion_id == -1999628) {
            vi.getCondtion().setVisible(false);
        } else {
            vi.getCondtion().setVisible(true);
        }
        vi.getCondtion().setData(sel);
    }

    private void updateProcSel(ProcedureTempViewer vi) {

        if (selP.proc_id == -1999628) {
            vi.getProcedure().setVisible(false);
        } else {
            vi.getProcedure().setVisible(true);
        }
        vi.getProcedure().setData(selP);
    }

    private GetProcedure.Procedure selP;

    public void chooseProcedure(ChoosedProc li) {
        final ProcedureTempViewer vi = new ProcedureTempViewer();
        DefaultTreeModel model;
        List<String> paths = new ArrayList<>();
        paths.add("new Procedure");
        for (int i = 0; i < Globals.templatesProcedures.size(); i++) {
            paths.add(Globals.templatesProcedures.get(i).name);
        }

        List<GetProcedure.Procedure> cc = new ArrayList<>();
        cc.add(ProcedureHandler.createInstance(null));
        cc.addAll(Globals.templatesProcedures);
        cc.get(0).proc_id = -1999628;
        selP = cc.get(0);

        model = new DefaultTreeModel(Util.generateNodes(paths, cc));
        vi.getProcList().setModel(model);

        vi.getProcList().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (vi.getProcList().getSelectionPath() == null) {
                    return;
                }
                List<GetProcedure.Procedure> objects = new ArrayList<>();
                Util.getAllObjects((DefaultMutableTreeNode) vi.getProcList().getSelectionPath().getLastPathComponent(), objects);
                if (objects.size() == 1) {
                    selP = objects.get(0);
                    updateProcSel(vi);
                }
            }
        });

        final JFrame frame = Util.showPanel(vi);
        vi.getContinue()
                .addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selP = ProcedureHandler.makeCopy(selP);
                        selP.type = Types.Type_Template;
                        if (selP != cc.get(0)) {
                            List<Long> ids = new ArrayList<>();
                            for (int i = 0; i < Globals.templatesProcedures.size(); i++) {
                                ids.add(Globals.templatesProcedures.get(i).proc_id);
                            }
                            selP.proc_id = Util.generateID(ids);
                        }
                        li.onChoose(selP);
                        frame.dispose();
                    }
                }
                );
        updateProcSel(vi);

    }
}
