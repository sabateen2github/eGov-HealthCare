/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.health.installer.fund.layouts;

import com.health.advanced.Handler;
import com.health.baseobjects.JCheckBoxTree2;
import com.health.installer.fund.handler.CondtionHandler;
import com.health.installer.fund.handler.InsuranceHandler;
import com.health.installer.fund.handler.MedTeamHandler;
import com.health.installer.fund.handler.MedicationHandler;
import com.health.installer.fund.handler.PatientsHandler;
import com.health.installer.fund.handler.PriceTagHandler;
import com.health.installer.fund.handler.ProcedureHandler;
import com.health.installer.fund.handler.RoomHandler;
import com.health.installer.fund.handler.RoomTagHandler;
import com.health.installer.fund.handler.ServiceHandler;
import com.health.installer.fund.utils.Globals;
import com.health.installer.fund.utils.Util;
import com.health.installer.fund.utils.Util.Obj;
import com.health.objects.GetAvailibleInsurances;
import com.health.objects.GetAvailibleRooms;
import com.health.objects.GetAvailibleServices;
import com.health.objects.GetCondtion;
import com.health.objects.GetProcedure;
import com.health.objects.Types;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Inspiron
 */
public class main extends javax.swing.JPanel {

    private Viewer medications;
    private Viewer insurances;
    private Viewer rooms;
    private Viewer services;
    private Viewer pricetags;
    private Viewer roomstags;
    private Viewer condtions;
    private Viewer procedures;
    private Viewer patients;
    private Viewer medteams;

    private DefaultTreeModel medications_model;
    private DefaultTreeModel insurances_model;
    private DefaultTreeModel rooms_model;
    private DefaultTreeModel services_model;
    private DefaultTreeModel roomstags_model;
    private DefaultTreeModel pricetags_model;
    private DefaultTreeModel condtions_model;
    private DefaultTreeModel procedures_model;
    private DefaultTreeModel patients_model;
    private DefaultTreeModel medteams_model;

    /*
     * Creates new form main
     */
    public main() {

        initComponents();
        medications = new Viewer();
        insurances = new Viewer();
        rooms = new Viewer();
        services = new Viewer();
        pricetags = new Viewer();
        roomstags = new Viewer();
        condtions = new Viewer();
        procedures = new Viewer();
        patients = new Viewer();
        medteams = new Viewer();

        ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < Globals.Medications.size(); i++) {
            paths.add(Globals.Medications.get(i).medication_name);
        }
        medications_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.Medications));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.Insurances.size(); i++) {
            paths.add(Globals.Insurances.get(i).insurance_path);
        }
        insurances_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.Insurances));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.Rooms.size(); i++) {
            paths.add(Globals.Rooms.get(i).room_path);
        }
        rooms_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.Rooms));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.Services.size(); i++) {
            paths.add(Globals.Services.get(i).service_path);
        }
        services_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.Services));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.pricetags.size(); i++) {
            paths.add(Globals.pricetags.get(i).name);
        }
        pricetags_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.pricetags));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.roomtags.size(); i++) {
            paths.add(Globals.roomtags.get(i).name);
        }
        roomstags_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.roomtags));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.condtions.size(); i++) {
            paths.add(Globals.condtions.get(i).name);
        }
        condtions_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.condtions));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.templateProc.size(); i++) {
            paths.add(Globals.templateProc.get(i).name);
        }
        procedures_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.templateProc));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.patients.size(); i++) {
            paths.add(Globals.patients.get(i).name);
        }
        patients_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.patients));

        paths = new ArrayList<>();
        for (int i = 0; i < Globals.medteam.size(); i++) {
            paths.add(Globals.medteam.get(i).name);
        }
        medteams_model = new DefaultTreeModel(Util.generateNodes(paths, Globals.medteam));

        medications.setTitleBtn("Add New Medications");
        insurances.setTitleBtn("Add New Insurance");
        rooms.setTitleBtn("Add New Room");
        services.setTitleBtn("Add New Service");
        pricetags.setTitleBtn("Add New Price Tag");
        roomstags.setTitleBtn("Add New Service Tag");
        condtions.setTitleBtn("Add New Condtion");
        procedures.setTitleBtn("Add New Visit");
        patients.setTitleBtn("Add New Patient Account");
        medteams.setTitleBtn("Add New MED Team Account");

        jTabbedPane1.add("ServicesRooms Groups", roomstags);
        jTabbedPane1.add("Medications", medications);
        jTabbedPane1.add("Insurances", insurances);
        jTabbedPane1.add("Rooms", rooms);
        jTabbedPane1.add("Services", services);
        jTabbedPane1.add("PriceTag", pricetags);
        jTabbedPane1.add("Condtions", condtions);
        jTabbedPane1.add("Visits", procedures);
        jTabbedPane1.add("Patients", patients);
        jTabbedPane1.add("Med Teams", medteams);
        jTabbedPane1.add("Time Skipper", new Skip());

        medications.getTree().setModel(medications_model);
        insurances.getTree().setModel(insurances_model);
        services.getTree().setModel(services_model);
        rooms.getTree().setModel(rooms_model);
        roomstags.getTree().setModel(roomstags_model);
        pricetags.getTree().setModel(pricetags_model);
        condtions.getTree().setModel(condtions_model);
        procedures.getTree().setModel(procedures_model);
        patients.getTree().setModel(patients_model);
        medteams.getTree().setModel(medteams_model);

        initMedications();
        initInsuarnce();
        initPriceTag();
        initServices();
        initRoomTags();
        initRooms();
        initCondtions();
        initProcedures();
        initPatients();
        initMedTeam();

        SaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Globals.SaveWithUi();
            }
        }
        );

        InstallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstallButton.setText("Please wait while installing");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Globals.install();
                        InstallButton.setText("Install");
                    }
                });

            }
        });

    }

    private void initCondtions() {

        condtions.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCondtionTemplates();
            }
        });

        condtions.getTree().addMouseListener(new Viewer.DoubleClickListener(condtions) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) condtions.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    GetCondtion.Condtion ser = (GetCondtion.Condtion) obj.userData;
                    openCondtionEditor(ser);
                }
            }
        });

    }

    private void initProcedures() {
        procedures.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProcedureTemplates(true);
            }
        });

        procedures.getTree().addMouseListener(new Viewer.DoubleClickListener(procedures) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) procedures.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    GetProcedure.Procedure ser = (GetProcedure.Procedure) obj.userData;
                    openProcedureEditor(ser, true);
                }
            }
        });
    }

    private void initPatients() {
        patients.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPatientsEditor(PatientsHandler.createInstant());
            }
        });

        patients.getTree().addMouseListener(new Viewer.DoubleClickListener(patients) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) patients.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    PatientsHandler.Patient ser = (PatientsHandler.Patient) obj.userData;
                    openPatientsEditor(ser);
                }
            }
        });
    }

    private void initMedTeam() {
        medteams.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMedTeamEditor(MedTeamHandler.createInstant());
            }
        });

        medteams.getTree().addMouseListener(new Viewer.DoubleClickListener(medteams) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) medteams.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    MedTeamHandler.MedTeamData ser = (MedTeamHandler.MedTeamData) obj.userData;
                    openMedTeamEditor(ser);
                }
            }
        });
    }

    private void initRoomTags() {
        roomstags.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RoomTagHandler.RoomTag room = RoomTagHandler.createRoomTag();
                openRoomTagEditor(room);
            }
        });

        roomstags.getTree().addMouseListener(new Viewer.DoubleClickListener(roomstags) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) roomstags.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    RoomTagHandler.RoomTag ser = (RoomTagHandler.RoomTag) obj.userData;
                    openRoomTagEditor(ser);
                }
            }
        });
    }

    private void initRooms() {
        rooms.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetAvailibleRooms.Room room = RoomHandler.createRoom();
                openRoomEditor(room);
            }
        });

        rooms.getTree().addMouseListener(new Viewer.DoubleClickListener(rooms) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) rooms.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    GetAvailibleRooms.Room ser = (GetAvailibleRooms.Room) obj.userData;
                    openRoomEditor(ser);
                }
            }
        });
    }

    private void initServices() {
        services.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetAvailibleServices.Service service = ServiceHandler.createService();
                openServiceEditor(service);
            }
        });

        services.getTree().addMouseListener(new Viewer.DoubleClickListener(services) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) services.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    GetAvailibleServices.Service ser = (GetAvailibleServices.Service) obj.userData;
                    openServiceEditor(ser);
                }
            }
        });
    }

    private void initInsuarnce() {
        insurances.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetAvailibleInsurances.Insurance insurance = InsuranceHandler.createInsurance();
                openInsuranceEditor(insurance);
            }
        });

        insurances.getTree().addMouseListener(new Viewer.DoubleClickListener(insurances) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) insurances.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    GetAvailibleInsurances.Insurance insurance = (GetAvailibleInsurances.Insurance) obj.userData;
                    openInsuranceEditor(insurance);
                }
            }
        });

    }

    private void initMedications() {

        medications.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMedicationEditor(MedicationHandler.createMedication());
            }
        });

        medications.getTree().addMouseListener(new Viewer.DoubleClickListener(medications) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) medications.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    GetCondtion.Medication medication = (GetCondtion.Medication) obj.userData;
                    openMedicationEditor(medication);
                }
            }
        });
    }

    private void initPriceTag() {
        pricetags.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PriceTagHandler.PriceTag tag = PriceTagHandler.createPriceTagInstance();
                openPriceTagEditor(tag);
            }
        });

        pricetags.getTree().addMouseListener(new Viewer.DoubleClickListener(pricetags) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) pricetags.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    PriceTagHandler.PriceTag tag = (PriceTagHandler.PriceTag) obj.userData;
                    openPriceTagEditor(tag);
                }
            }
        });
    }

    private void openCondtionEditor(final GetCondtion.Condtion cond) {
        final CondtionSave save = new CondtionSave();

        final JFrame frame = Util.showPanel(save);
        save.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!checkCondtion(cond)) {
                    return;
                }

                CondtionHandler.save(cond);
                updateCondtionsTree(cond.condtion_id);
                frame.dispose();
            }
        });
        save.getCondtionEditor().setData(cond, false);
        frame.revalidate();
        frame.repaint();
    }

    private boolean polishProc(GetProcedure.Procedure proc) {

        if (proc.name == null || proc.name.length() == 0) {
            return false;
        }
        if (proc.note_med == null || proc.note_med.length() == 0) {
            return false;
        }
        if (proc.note_patient == null || proc.note_patient.length() == 0) {
            return false;
        }
        if (proc.objective == null || proc.objective.length() == 0) {
            return false;
        }

        if (proc.activation_period < 1) {
            return false;
        }

        for (int i = 0; i < proc.services.size(); i++) {
            if (proc.services.get(i).count < 1) {
                return false;
            }
            if (proc.services.get(i).services.size() < 1 || proc.services.get(i).rooms.size() < 1) {
                return false;
            }
            if (proc.services.get(i).note_med == null || proc.services.get(i).note_med.length() == 0) {
                return false;
            }
        }

        for (int i = 0; i < proc.smart_feedbacks.size(); i++) {
            if (proc.smart_feedbacks.get(i).services.size() < 1) {
                return false;
            }
            if (proc.smart_feedbacks.get(i).note_med == null || proc.smart_feedbacks.get(i).note_med.length() == 0) {
                return false;
            }
        }

        if (proc.type == Types.Type_Template || proc.type == Types.Type_Template_User) {
            proc.feedbacks.clear();
        } else if (proc.type == Types.Type_Procedure_Condtion) {
            proc.cycle_period = 1;
            proc.routine_activation_period = 1;
        } else if (proc.cycle_period < 1) {
            return false;
        }
        return true;
    }

    private boolean checkCondtion(GetCondtion.Condtion condtion) {
        if (condtion.name.length() == 0
                || condtion.desc.length() == 0 || condtion.activation_period <= 0) {
            Util.showMsg("Please Enter All Fields Correctly");
            return false;
        }

        for (int x = 0; x < condtion.procedures.size(); x++) {

            GetProcedure.Procedure procedure = condtion.procedures.get(x);

            boolean check = polishProc(procedure);
            if (!check) {
                Util.showMsg("Please Enter All Visits accuraly : " + procedure.name);
                return false;
            }

        }
        for (int x = 0; x < condtion.attached_procedures.size(); x++) {
            GetProcedure.Procedure procedure = condtion.attached_procedures.get(x);

            boolean check = polishProc(procedure);
            if (!check) {
                Util.showMsg("Please Enter All Visits accuraly : " + procedure.name);
                return false;
            }
        }
        return true;
    }

    private void openProcedureEditor(final GetProcedure.Procedure proc, boolean show_forP) {
        final ProcedureSave save = new ProcedureSave();
        save.getPRocedureEditor().setForPatientVis(show_forP);
        final JFrame frame = Util.showPanel(save);
        save.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!polishProc(proc)) {
                    Util.showMsg("Please Enetr All data accuratly");
                    return;
                }

                ProcedureHandler.save(proc);
                updateProcedureTree(proc.proc_id);
                frame.dispose();
            }
        });
        save.getPRocedureEditor().setData(null, proc, false, null);
        frame.revalidate();
        frame.repaint();
    }

    private void openCondtionTemplates() {
        Handler d = new Handler();
        d.chooseCondtion(new Handler.ChoosedCond() {
            @Override
            public void onChoose(GetCondtion.Condtion cond) {
                openCondtionEditor(cond);
            }
        });

    }

    private void openProcedureTemplates(boolean show_forP) {
        Handler d = new Handler();
        d.chooseProcedure(new Handler.ChoosedProc() {
            @Override
            public void onChoose(GetProcedure.Procedure proc) {
                openProcedureEditor(proc, show_forP);
            }

        }
        );
    }

    private void openPatientsEditor(final PatientsHandler.Patient p) {

        final PatientData pa = new PatientData();
        pa.getNamePatient().setText(p.name);
        pa.getNamePatientar().setText(p.name_ar);
        pa.getHeightPatient().setText(p.height + "");
        pa.getWeight().setText(p.weight + "");
        pa.getID().setText(p.id + "");
        pa.getPass().setText(p.pass);
        pa.getConfirm().setText(p.pass);
        pa.getAddress().setText(p.Address);
        pa.getPhone().setText(p.Phone);
        pa.getDate().setText(p.date.replace("-", "/"));
        pa.getGender().setSelectedIndex(p.gender.contains("Male") ? 0 : 1);

        System.out.println("" + p.name_ar + "         " + p.name);

        if (p.id != 0) {
            pa.getID().setEnabled(false);
        }
        final List<GetAvailibleInsurances.Insurance> in = new ArrayList<>();

        DefaultTreeModel model;
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < Globals.Insurances.size(); i++) {
            paths.add(Globals.Insurances.get(i).insurance_path);
            if (p.insurance == Globals.Insurances.get(i).insurance_id) {
                in.add(Globals.Insurances.get(i));
            }
        }
        model = new DefaultTreeModel(Util.generateNodes(paths, Globals.Insurances));
        pa.getTree().setModel(model);

        final DefaultMutableTreeNode hl;
        if (in.size() == 1) {
            DefaultMutableTreeNode n = Util.findNode((DefaultMutableTreeNode) model.getRoot(), in.get(0));
            pa.getTree().toggleNode(n);
            hl = n;
        } else {
            hl = null;
        }
        final JFrame frame = Util.showPanel(pa);

        pa.getTree().addCheckChangeEventListener(new JCheckBoxTree2.CheckChangeEventListener() {
            DefaultMutableTreeNode past = hl;

            public void checkStateChanged(JCheckBoxTree2.CheckChangeEvent event) {
                JCheckBoxTree2.Data d = (JCheckBoxTree2.Data) event.getSource();
                List<GetAvailibleInsurances.Insurance> ins = new ArrayList<>();
                Util.getAllObjects((DefaultMutableTreeNode) d.path.getLastPathComponent(), ins);
                if (ins.size() != 1) {
                    pa.getTree().toggleNode((DefaultMutableTreeNode) d.path.getLastPathComponent());
                    return;
                }

                if (d.is_checked) {
                    in.clear();
                    in.addAll(ins);
                    if (past != null) {
                        pa.getTree().toggleNode(past);
                    }
                    past = (DefaultMutableTreeNode) d.path.getLastPathComponent();
                } else {
                    in.clear();
                    past = null;
                }
            }
        });
        pa.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String date = pa.getDate().getText();
                try {
                    date = date.replace("/", "-");
                    Util.Date d = new Util.Date(date + "-0-0");
                } catch (Exception h) {
                    Util.showMsg("Please Eneter a Valid Birth Date");
                    h.printStackTrace();
                    return;
                }

                char[] pass = pa.getPass().getPassword();
                char[] confirm = pa.getConfirm().getPassword();

                try {
                    if (pass.length != confirm.length) {
                        throw new Exception();
                    }
                    for (int i = 0; i < pass.length; i++) {
                        if (pass[i] != confirm[i]) {
                            throw new Exception();
                        }
                    }
                } catch (Exception eds) {
                    Util.showMsg("Passwords Dont match");
                    return;
                }

                String name = pa.getNamePatient().getText();
                String name_ar = pa.getNamePatientar().getText();
                double height = Double.parseDouble(pa.getHeightPatient().getText());
                double weight = Double.parseDouble(pa.getWeight().getText());
                long id = Long.parseLong(pa.getID().getText());
                String phone = pa.getPhone().getText();
                String address = pa.getAddress().getText();
                String gender = pa.getGender().getItemAt(pa.getGender().getSelectedIndex());
                long insurance = Globals.Signature;
                if (in.size() == 1) {
                    insurance = in.get(0).insurance_id;
                }

                p.name = name;
                p.name_ar = name_ar;
                p.Address = address;
                p.date = date;
                p.gender = gender;
                p.height = height;
                p.weight = weight;
                p.insurance = insurance;
                p.Phone = phone;
                p.id = id;
                p.pass = new String(pass);
                PatientsHandler.savePatient(p);
                frame.dispose();
                Util.showMsg("Saving Done");
                updatePatientsTree(id);
            }
        });

    }

    private void openMedTeamEditor(final MedTeamHandler.MedTeamData p) {

        final MedicalTeam pa = new MedicalTeam();
        pa.getNameMed().setText(p.name);
        pa.getID().setText(p.id + "");
        pa.getPass().setText(p.pass);
        pa.getConfirm().setText(p.pass);
        pa.getAddress().setText(p.Address);
        pa.getPhone().setText(p.Phone);
        pa.getDate().setText(p.date.replace("-", "/"));
        pa.getSpeciality().setText(p.Speciality);
        pa.getGender().setSelectedIndex(p.gender.contains("Male") ? 0 : 1);
        if (p.id != 0) {
            pa.getID().setEnabled(false);
        }

        final JFrame frame = Util.showPanel(pa);
        pa.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String date = pa.getDate().getText();
                try {
                    date = date.replace("/", "-");
                    Util.Date d = new Util.Date(date + "-0-0");
                } catch (Exception h) {
                    Util.showMsg("Please Eneter a Valid Birth Date");
                    return;
                }

                char[] pass = pa.getPass().getPassword();
                char[] confirm = pa.getConfirm().getPassword();

                try {

                    if (pass.length != confirm.length) {
                        throw new Exception();

                    }
                    for (int i = 0; i < pass.length; i++) {
                        if (pass[i] != confirm[i]) {
                            throw new Exception();
                        }
                    }
                } catch (Exception eds) {
                    Util.showMsg("Passwords Dont match");
                    return;
                }

                String name = pa.getNameMed().getText();
                long id = Long.parseLong(pa.getID().getText());
                String phone = pa.getPhone().getText();
                String address = pa.getAddress().getText();
                String gender = pa.getGender().getItemAt(pa.getGender().getSelectedIndex());

                p.name = name;
                p.Address = address;
                p.date = date;
                p.gender = gender;
                p.Phone = phone;
                p.id = id;
                p.Speciality = pa.getSpeciality().getText();
                p.pass = new String(pass);
                MedTeamHandler.saveMed(p);
                frame.dispose();
                Util.showMsg("Saving Done");
                updateMedTeamTree(id);
            }
        });

    }

    private void openRoomEditor(final GetAvailibleRooms.Room room) {

        final Room se = new Room();
        final Ref frame = new Ref();

        se.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String title = se.getText().getText();

                if (title == null || title.isEmpty()) {
                    Util.showMsg("Please enter a valid name");
                    return;
                }

                room.room_path = title;
                updateRoomsTree(RoomHandler.saveRoom(room));
                ((JFrame) frame.obj).dispose();
            }
        });

        se.getText().setText(room.room_path);

        frame.obj = Util.showPanel(se);
    }

    private void openServiceEditor(final GetAvailibleServices.Service service) {

        final Service se = new Service();
        final Ref frame = new Ref();

        se.getText().setText(service.service_path);
        se.getAttachPrice().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openServicePriceTag(service);
            }
        });

        se.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String title = se.getText().getText();

                if (title == null || title.isEmpty()) {
                    Util.showMsg("Please enter a valid name");
                    return;
                }

                if (service.pricetag == null) {
                    Util.showMsg("Please Attach A Price For The Medication");
                    return;
                }

                service.service_path = title;
                updateServiceTree(ServiceHandler.save(service));
                ((JFrame) frame.obj).dispose();
            }
        });

        frame.obj = Util.showPanel(se);
    }

    private void openInsuranceEditor(final GetAvailibleInsurances.Insurance insurance) {

        final Insurance in = new Insurance();
        final Ref frame = new Ref();

        in.getText().setText(insurance.insurance_path);
        in.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String path = in.getText().getText();
                if (path == null || path.length() == 0) {
                    Util.showMsg("Please Enter a Valid Path");
                    return;
                }
                path = path.replace("*", "/").replace(".", "/").replace("-", "/").replace("_", "/").replace(",", "/");
                insurance.insurance_path = path;
                long id = InsuranceHandler.saveInsurance(insurance);
                updateInsuranceTree(id);
                ((JFrame) frame.obj).dispose();
            }
        });

        frame.obj = Util.showPanel(in);
    }

    private void removeEmptyNodes(DefaultMutableTreeNode node) {

        if (node.getChildCount() == 0 && node.getParent() != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }
    }

    private void openMedicationEditor(final GetCondtion.Medication medication) {
        final Medication m = new Medication();
        Ref frame = new Ref();
        m.getAttachPrice().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMedicationPriceTag(medication);
            }
        });

        m.getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String title = m.getNameText().getText();
                String route = m.getRouteTypeText().getText();
                String prescription_unit = m.getPrescriptionUnitText().getText();

                if (title == null || title.isEmpty()) {
                    Util.showMsg("Please enter a valid name");
                    return;
                }
                if (route == null || route.isEmpty()) {
                    Util.showMsg("Please enter a valid Route Type");
                    return;
                }
                if (prescription_unit == null || prescription_unit.isEmpty()) {
                    Util.showMsg("Please enter a valid Prescription Unit");
                    return;
                }
                if (medication.priceTag == null) {
                    Util.showMsg("Please Attach A Price For The Medication");
                    return;
                }

                medication.medication_name = title;
                medication.route_type = route;
                medication.prescription_unit = prescription_unit;
                updateMedicationTree(MedicationHandler.saveMedication(medication));
                ((JFrame) frame.obj).dispose();
            }
        });

        m.getNameText().setText(medication.medication_name);
        m.getRouteTypeText().setText(medication.route_type);
        m.getPrescriptionUnitText().setText(medication.prescription_unit);

        frame.obj = Util.showPanel(m);
    }

    private void openMedicationPriceTag(GetCondtion.Medication medication) {

        jTabbedPane1.remove(pricetags);
        final JFrame frame = Util.showPanel(pricetags);
        final MouseListener[] lis = pricetags.getTree().getMouseListeners();
        pricetags.getTree().removeMouseListener(lis[lis.length - 1]);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ewe) {
                pricetags.getTree().removeMouseListener(pricetags.getTree().getMouseListeners()[lis.length - 1]);
                pricetags.getTree().addMouseListener(lis[lis.length - 1]);
                frame.removeAll();
                jTabbedPane1.add("Price Tags", pricetags);
                jTabbedPane1.revalidate();
                jTabbedPane1.repaint();
                frame.dispose();
            }
        });

        pricetags.getTree().addMouseListener(new Viewer.DoubleClickListener(pricetags) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) pricetags.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    PriceTagHandler.PriceTag tag = (PriceTagHandler.PriceTag) obj.userData;
                    medication.priceTag = tag;
                }

                pricetags.getTree().removeMouseListener(this);
                pricetags.getTree().addMouseListener(lis[lis.length - 1]);
                frame.removeAll();
                jTabbedPane1.add("Price Tags", pricetags);
                jTabbedPane1.revalidate();
                jTabbedPane1.repaint();
                frame.dispose();

            }
        });

        if (medication.priceTag != null) {
            openPriceTagEditor(medication.priceTag);
        }
    }

    private void openServicePriceTag(GetAvailibleServices.Service service) {

        jTabbedPane1.remove(pricetags);
        final JFrame frame = Util.showPanel(pricetags);
        final MouseListener[] lis = pricetags.getTree().getMouseListeners();
        pricetags.getTree().removeMouseListener(lis[lis.length - 1]);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ewe) {
                pricetags.getTree().removeMouseListener(pricetags.getTree().getMouseListeners()[lis.length - 1]);
                pricetags.getTree().addMouseListener(lis[lis.length - 1]);
                frame.removeAll();
                jTabbedPane1.add("Price Tags", pricetags);
                jTabbedPane1.revalidate();
                jTabbedPane1.repaint();
                frame.dispose();
            }
        });

        pricetags.getTree().addMouseListener(new Viewer.DoubleClickListener(pricetags) {
            @Override
            public void onDoubleClick() {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) pricetags.getTree().getLastSelectedPathComponent();
                if (node.getUserObject() instanceof String) {
                    return;
                }
                Obj obj = (Obj) node.getUserObject();
                if (obj != null) {
                    PriceTagHandler.PriceTag tag = (PriceTagHandler.PriceTag) obj.userData;
                    service.pricetag = tag;
                }

                pricetags.getTree().removeMouseListener(this);
                pricetags.getTree().addMouseListener(lis[lis.length - 1]);
                frame.removeAll();
                frame.dispose();
                jTabbedPane1.add("Price Tags", pricetags);
                jTabbedPane1.revalidate();
                jTabbedPane1.repaint();
            }
        });
        if (service.pricetag != null) {
            openPriceTagEditor(service.pricetag);
        }
    }

    private void openRoomTagEditor(final RoomTagHandler.RoomTag tag) {
        final ServiceTag roomTag = new ServiceTag();
        final Ref frame = new Ref();

        DefaultMutableTreeNode rooms_root = Util.getRoomsNodes();
        DefaultMutableTreeNode services_root = Util.getServiceNodes();
        DefaultTreeModel room_mod = new DefaultTreeModel(rooms_root);
        DefaultTreeModel service_mod = new DefaultTreeModel(services_root);
        roomTag.getRoomsTree().setModel(room_mod);
        roomTag.getServiceTree().setModel(service_mod);

        for (int i = 0; i < tag.rooms.size(); i++) {
            roomTag.getRoomsTree().toggleNode(Util.findNode(rooms_root, RoomHandler.getRoom(tag.rooms.get(i))));
        }
        for (int i = 0; i < tag.services.size(); i++) {
            roomTag.getServiceTree().toggleNode(Util.findNode(services_root, ServiceHandler.getService(tag.services.get(i))));
        }

        roomTag.getTitle().setText(tag.name);

        roomTag.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = roomTag.getTitle().getText();
                if (title == null || title.length() == 0) {
                    Util.showMsg("Please Enter a Valid Title");
                    return;
                }
                tag.name = title;
                long id = RoomTagHandler.save(tag);
                updateRoomTagsTree(id);
                ((JFrame) frame.obj).dispose();
            }
        }
        );

        roomTag.getServiceTree().addCheckChangeEventListener(new JCheckBoxTree2.CheckChangeEventListener() {
            @Override
            public void checkStateChanged(JCheckBoxTree2.CheckChangeEvent e) {
                JCheckBoxTree2.Data data = (JCheckBoxTree2.Data) e.getSource();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getPathComponent(data.path.getPathCount() - 1);
                if (node.getUserObject() != null && !(node.getUserObject() instanceof String)) {
                    List<GetAvailibleServices.Service> services = new ArrayList<>();
                    Util.getAllObjects(node, services);
                    if (data.is_checked) {
                        for (int i = 0; i < services.size(); i++) {
                            if (!tag.services.contains(services.get(i).service_id)) {
                                tag.services.add(services.get(i).service_id);
                            }
                        }
                    } else {
                        for (int i = 0; i < services.size(); i++) {
                            tag.services.remove(services.get(i).service_id);
                        }
                    }
                }
            }
        }
        );

        roomTag.getRoomsTree().addCheckChangeEventListener(new JCheckBoxTree2.CheckChangeEventListener() {
            @Override
            public void checkStateChanged(JCheckBoxTree2.CheckChangeEvent e) {
                JCheckBoxTree2.Data data = (JCheckBoxTree2.Data) e.getSource();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getPathComponent(data.path.getPathCount() - 1);
                if (node.getUserObject() != null && !(node.getUserObject() instanceof String)) {
                    List<GetAvailibleRooms.Room> rooms = new ArrayList<>();
                    Util.getAllObjects(node, rooms);
                    if (data.is_checked) {
                        for (int i = 0; i < rooms.size(); i++) {
                            if (!tag.rooms.contains(rooms.get(i).room_id)) {
                                tag.rooms.add(rooms.get(i).room_id);
                                rooms.get(i).roomtag = tag;
                            }
                        }
                    } else {
                        for (int i = 0; i < rooms.size(); i++) {
                            tag.rooms.remove(rooms.get(i).room_id);
                            rooms.get(i).roomtag = null;
                        }
                    }
                }
            }
        });

        frame.obj = Util.showPanel(roomTag);
    }

    private void openPriceTagEditor(PriceTagHandler.PriceTag priceTag) {
        final Ref frame = new Ref();

        final PriceTag tag = new PriceTag();
        tag.getAddInsurance().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInsuranceGroup(tag, priceTag, null);
            }
        });

        tag.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean is = savePriceTag(tag, priceTag);
                if (is) {
                    ((JFrame) frame.obj).dispose();
                }
            }
        });

        tag.getPrice().setText(priceTag.price + "");
        tag.getTitle().setText(priceTag.name + "");

        for (int i = 0; i < priceTag.insurances.size(); i++) {
            addInsuranceGroup(tag, priceTag, priceTag.insurances.get(i));
        }

        frame.obj = Util.showPanel(tag);

    }

    private boolean savePriceTag(PriceTag priceTag, PriceTagHandler.PriceTag tag) {

        String title = priceTag.getTitle().getText();
        String price = priceTag.getPrice().getText();

        if (title == null || title.length() == 0) {
            Util.showMsg("Please Eneter The Title");
            return false;
        }
        if (price == null || price.length() == 0) {
            Util.showMsg("Please Eneter The Price");
            return false;
        }
        float pr = 0.0f;
        try {
            if (!price.endsWith("f")) {
                price = price + "f";
            }
            pr = Float.parseFloat(price);
        } catch (Exception e) {
            Util.showMsg("Please Eneter Valid Price (Just Float Numbers eg. 4 ,5,6,5.1f)");
            return false;
        }

        tag.name = title;
        tag.price = pr;
        long id = PriceTagHandler.savePriceTag(tag);
        updatePriceTagTree(id);
        return true;
    }

    private void addInsuranceGroup(PriceTag priceTag, final PriceTagHandler.PriceTag tag, PriceTagHandler.InsuranceGroup gr) {

        final PriceTagHandler.InsuranceGroup g;
        if (gr == null) {
            g = PriceTagHandler.createInsuranceGroupInstance();
            tag.insurances.add(g);
        } else {
            g = gr;
        }
        final InsuranceGroup group = new InsuranceGroup();
        group.getRemove().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tag.insurances.remove(g);
                priceTag.getContainer().remove(group);
                priceTag.getContainer().revalidate();
                priceTag.getContainer().repaint();
            }
        });

        ArrayList<String> paths = new ArrayList<>();
        for (int i = 0; i < Globals.Insurances.size(); i++) {
            paths.add(Globals.Insurances.get(i).insurance_path);
        }

        DefaultMutableTreeNode root = Util.generateNodes(paths, Globals.Insurances);
        DefaultTreeModel model = new DefaultTreeModel(root);
        group.getTree().setModel(model);

        group.getTree().addCheckChangeEventListener(new JCheckBoxTree2.CheckChangeEventListener() {
            @Override
            public void checkStateChanged(JCheckBoxTree2.CheckChangeEvent event) {
                JCheckBoxTree2.Data data = (JCheckBoxTree2.Data) event.getSource();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) data.path.getPathComponent(data.path.getPathCount() - 1);
                if (node.getUserObject() != null && !(node.getUserObject() instanceof String)) {
                    List<GetAvailibleInsurances.Insurance> in = new ArrayList<>();
                    Util.getAllObjects(node, in);
                    if (data.is_checked) {
                        for (GetAvailibleInsurances.Insurance i : in) {
                            if (!g.insurances.contains(i.insurance_id)) {
                                g.insurances.add(i.insurance_id);
                            }
                        }
                    } else {
                        List<Long> rem = new ArrayList<>();
                        for (GetAvailibleInsurances.Insurance i : in) {
                            rem.add(i.insurance_id);
                        }
                        g.insurances.removeAll(rem);
                    }
                }
            }
        });

        group.getPercentage().getDocument().addDocumentListener(new DocumentListener() {
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

                float percentage = 0.0f;

                try {
                    percentage = Float.parseFloat(group.getPercentage().getText());
                } catch (Exception e) {
                    if (!(group.getPercentage().getText() == null || group.getPercentage().getText().length() == 0)) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                group.getPercentage().setText(group.getPercentage().getText().substring(0, group.getPercentage().getText().length() - 1));
                            }
                        });
                    }
                }
                g.percentage = percentage;
            }
        }
        );

        //restore state
        if (gr != null) {
            group.getPercentage().setText(gr.percentage + "");
            for (int index = 0; index < gr.insurances.size(); index++) {
                for (GetAvailibleInsurances.Insurance i : Globals.Insurances) {
                    if (gr.insurances.get(index).equals(i.insurance_id)) {
                        DefaultMutableTreeNode node = Util.findNode(root, i);
                        System.out.println("Heelo   " + node);
                        group.getTree().toggleNode(node);
                    }
                }
            }
        }
        //

        priceTag.getContainer()
                .add(group);
        priceTag.getContainer()
                .revalidate();
        priceTag.getContainer()
                .repaint();
    }

    private void updateCondtionsTree(long id) {
        GetCondtion.Condtion medte = null;
        for (int i = 0; i < Globals.condtions.size(); i++) {
            if (Globals.condtions.get(i).condtion_id == id) {
                medte = Globals.condtions.get(i);
                break;
            }
        }
        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) condtions_model.getRoot(), medte);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }

        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) condtions_model.getRoot(), medte.name, medte);
        condtions.reload();
        condtions.getTree().setSelectionPath(new TreePath(no.getPath()));
    }

    private void updateProcedureTree(long id) {
        GetProcedure.Procedure medte = null;
        for (int i = 0; i < Globals.templateProc.size(); i++) {
            if (Globals.templateProc.get(i).proc_id == id) {
                medte = Globals.templateProc.get(i);
                break;
            }
        }
        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) procedures_model.getRoot(), medte);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }

        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) procedures_model.getRoot(), medte.name, medte);
        procedures.reload();
        procedures.getTree().setSelectionPath(new TreePath(no.getPath()));

    }

    private void updatePatientsTree(long id) {
        PatientsHandler.Patient medte = null;
        for (int i = 0; i < Globals.patients.size(); i++) {
            if (Globals.patients.get(i).id == id) {
                medte = Globals.patients.get(i);
                break;
            }
        }
        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) patients_model.getRoot(), medte);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }

        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) patients_model.getRoot(), medte.name, medte);
        patients.reload();
        patients.getTree().setSelectionPath(new TreePath(no.getPath()));

    }

    private void updateMedTeamTree(long id) {
        MedTeamHandler.MedTeamData medte = null;
        for (int i = 0; i < Globals.medteam.size(); i++) {
            if (Globals.medteam.get(i).id == id) {
                medte = Globals.medteam.get(i);
                break;
            }
        }

        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) medteams_model.getRoot(), medte);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }

        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) medteams_model.getRoot(), medte.name, medte);
        medteams.reload();
        medteams.getTree().setSelectionPath(new TreePath(no.getPath()));

    }

    private void updateInsuranceTree(long id) {
        GetAvailibleInsurances.Insurance insurance = null;
        for (int i = 0; i < Globals.Insurances.size(); i++) {
            if (Globals.Insurances.get(i).insurance_id == id) {
                insurance = Globals.Insurances.get(i);
                break;
            }
        }

        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) insurances_model.getRoot(), insurance);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }

        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) insurances_model.getRoot(), insurance.insurance_path, insurance);
        insurances.reload();
        insurances.getTree().setSelectionPath(new TreePath(no.getPath()));
    }

    private void updatePriceTagTree(long id) {
        PriceTagHandler.PriceTag priceTag = null;
        for (int i = 0; i < Globals.pricetags.size(); i++) {
            if (Globals.pricetags.get(i).id == id) {
                priceTag = Globals.pricetags.get(i);
                break;
            }
        }

        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) pricetags_model.getRoot(), priceTag);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }

        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) pricetags_model.getRoot(), priceTag.name, priceTag);
        pricetags.reload();
        pricetags.getTree().setSelectionPath(new TreePath(no.getPath()));
    }

    private void updateMedicationTree(long id) {

        GetCondtion.Medication med = null;
        for (int i = 0; i < Globals.Medications.size(); i++) {
            if (Globals.Medications.get(i).medication_id == id) {
                med = Globals.Medications.get(i);
                break;
            }
        }

        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) medications_model.getRoot(), med);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }

        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) medications_model.getRoot(), med.medication_name, med);
        medications.reload();
        medications.getTree().setSelectionPath(new TreePath(no.getPath()));

    }

    private void updateServiceTree(long id) {

        GetAvailibleServices.Service ser = null;
        for (int i = 0; i < Globals.Services.size(); i++) {
            if (Globals.Services.get(i).service_id == id) {
                ser = Globals.Services.get(i);
                break;
            }
        }

        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) services_model.getRoot(), ser);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }

        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) services_model.getRoot(), ser.service_path, ser);
        services.reload();
        services.getTree().setSelectionPath(new TreePath(no.getPath()));

    }

    private void updateRoomsTree(long id) {

        GetAvailibleRooms.Room room = null;
        for (int i = 0; i < Globals.Rooms.size(); i++) {
            if (Globals.Rooms.get(i).room_id == id) {
                room = Globals.Rooms.get(i);
                break;
            }
        }

        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) rooms_model.getRoot(), room);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }
        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) rooms_model.getRoot(), room.room_path, room);
        rooms.reload();
        rooms.getTree().setSelectionPath(new TreePath(no.getPath()));
    }

    private void updateRoomTagsTree(long id) {

        RoomTagHandler.RoomTag room = null;
        for (int i = 0; i < Globals.roomtags.size(); i++) {
            if (Globals.roomtags.get(i).id == id) {
                room = Globals.roomtags.get(i);
                break;
            }
        }

        DefaultMutableTreeNode node = Util.findNode((DefaultMutableTreeNode) roomstags_model.getRoot(), room);
        if (node != null) {
            DefaultMutableTreeNode par = (DefaultMutableTreeNode) node.getParent();
            node.removeFromParent();
            removeEmptyNodes(par);
        }
        DefaultMutableTreeNode no = Util.add_node((DefaultMutableTreeNode) roomstags_model.getRoot(), room.name, room);
        roomstags.reload();
        roomstags.getTree().setSelectionPath(new TreePath(no.getPath()));

    }

    private static class Ref {

        Object obj;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        InstallButton = new javax.swing.JButton();
        SaveButton = new javax.swing.JButton();

        InstallButton.setText("Install");
        InstallButton.setActionCommand("");

        SaveButton.setText("Save");
        SaveButton.setActionCommand("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(InstallButton, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(SaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InstallButton)
                    .addComponent(SaveButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton InstallButton;
    private javax.swing.JButton SaveButton;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
