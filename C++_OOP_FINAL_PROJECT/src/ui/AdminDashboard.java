package ui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class AdminDashboard extends JFrame {
    private final User admin;
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final BillingDAO billingDAO = new BillingDAO();
    private final MedicalRecordDAO recordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();


    public AdminDashboard(User admin) {
        this.admin = admin;
        setTitle("Admin Dashboard - " + (admin != null ? admin.getFullName() : ""));
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("ADMIN CONTROL CENTER", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Doctors", doctorsPanel());
        tabs.addTab("Patients", patientsPanel());
        tabs.addTab("Appointments", appointmentsPanel());
        tabs.addTab("Billing", billingPanel());
        tabs.addTab("Medical Records", medicalRecordsPanel());
        tabs.addTab("Prescriptions", prescriptionsPanel());


        add(tabs, BorderLayout.CENTER);

        JLabel footer = new JLabel("Logged in as: " + (admin != null ? admin.getFullName() : ""), SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(footer, BorderLayout.SOUTH);
    }

    private String safe(Object o) { return o == null ? "" : o.toString(); }

    private JPanel doctorsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"DoctorID","UserID","Name","Identifier","Status","Location","Contact","AssignedSince","CreatedAt"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add"), update = new JButton("Update"), delete = new JButton("Delete"), refresh = new JButton("Refresh");
        bottom.add(add); bottom.add(update); bottom.add(delete); bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Doctor> list = doctorDAO.getAll();
            for (Doctor d : list) model.addRow(new Object[]{d.getDoctorID(), d.getUserID(), d.getName(), d.getIdentifier(), d.getStatus(), d.getLocation(), d.getContact(), d.getAssignedSince(), d.getCreatedAt()});
        });

        add.addActionListener(e -> {
            JTextField userId = new JTextField(), name = new JTextField(), identifier = new JTextField(), location = new JTextField(), contact = new JTextField();
            Object[] form = {"UserID:", userId, "Name:", name, "Identifier:", identifier, "Location:", location, "Contact:", contact};
            int ok = JOptionPane.showConfirmDialog(this, form, "Add Doctor", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    Doctor d = new Doctor();
                    d.setUserID(Integer.parseInt(userId.getText().trim()));
                    d.setName(name.getText().trim());
                    d.setIdentifier(identifier.getText().trim());
                    d.setLocation(location.getText().trim());
                    d.setContact(contact.getText().trim());
                    d.setStatus("Active");
                    d.setAssignedSince(new Date(System.currentTimeMillis()));
                    doctorDAO.add(d);
                    refresh.doClick();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage()); }
            }
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(this, "Select doctor"); return; }
            int id = (int) model.getValueAt(r,0);
            JTextField name = new JTextField(safe(model.getValueAt(r,2)));
            JTextField identifier = new JTextField(safe(model.getValueAt(r,3)));
            JTextField status = new JTextField(safe(model.getValueAt(r,4)));
            JTextField location = new JTextField(safe(model.getValueAt(r,5)));
            JTextField contact = new JTextField(safe(model.getValueAt(r,6)));
            Object[] form = {"Name:", name, "Identifier:", identifier, "Status:", status, "Location:", location, "Contact:", contact};
            int ok = JOptionPane.showConfirmDialog(this, form, "Update Doctor", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                Doctor d = new Doctor();
                d.setDoctorID(id);
                d.setName(name.getText().trim());
                d.setIdentifier(identifier.getText().trim());
                d.setStatus(status.getText().trim());
                d.setLocation(location.getText().trim());
                d.setContact(contact.getText().trim());
                doctorDAO.update(d);
                refresh.doClick();
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(this, "Select doctor"); return; }
            int id = (int) model.getValueAt(r,0);
            int conf = JOptionPane.showConfirmDialog(this, "Delete doctor #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) { doctorDAO.delete(id); refresh.doClick(); }
        });

        refresh.doClick();
        return panel;
    }

    private JPanel patientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"PatientID","UserID","FullName","Gender","DOB","CreatedAt"},0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton refresh = new JButton("Refresh"), delete = new JButton("Delete");
        bottom.add(delete); bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Patient> list = patientDAO.getAll();
            for (Patient p : list) model.addRow(new Object[]{p.getPatientID(), p.getUserID(), p.getFullName(), p.getGender(), p.getDob(), p.getCreatedAt()});
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r==-1){ JOptionPane.showMessageDialog(this,"Select patient"); return; }
            int id = (int) model.getValueAt(r,0); if (JOptionPane.showConfirmDialog(this,"Delete?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){ patientDAO.delete(id); refresh.doClick(); }
        });

        refresh.doClick();
        return panel;
    }

    private JPanel appointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"AppointmentID","OrderNumber","PatientID","DoctorID","Date","Status","TotalAmount","PaymentMethod","Notes"},0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh");
        panel.add(refresh, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Appointment> list = appointmentDAO.getAll();
            for (Appointment a : list) model.addRow(new Object[]{a.getAppointmentID(), a.getOrderNumber(), a.getPatientID(), a.getDoctorID(), a.getAppointmentDate(), a.getStatus(), a.getTotalAmount(), a.getPaymentMethod(), a.getNotes()});
        });

        refresh.doClick();
        return panel;
    }

    private JPanel billingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"BillingID","PatientID","PatientName","Amount","PaymentMethod","MedicalRecordID","CreatedAt"},0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add"), refresh = new JButton("Refresh"), delete = new JButton("Delete");
        bottom.add(add); bottom.add(delete); bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Billing> list = billingDAO.getAll();
            for (Billing b : list) model.addRow(new Object[]{b.getBillingID(), b.getPatientID(), b.getPatientName(), b.getAmount(), b.getPaymentMethod(), b.getMedicalRecordID(), b.getCreatedAt()});
        });

        add.addActionListener(e -> {
            JTextField pid = new JTextField(), pname = new JTextField(), amt = new JTextField(), pmethod = new JTextField();
            Object[] form = {"PatientID:", pid, "PatientName:", pname, "Amount:", amt, "PaymentMethod:", pmethod};
            int ok = JOptionPane.showConfirmDialog(this, form, "Add Billing", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    Billing b = new Billing();
                    b.setPatientID(pid.getText().trim().isEmpty()? null : Integer.valueOf(pid.getText().trim()));
                    b.setPatientName(pname.getText().trim());
                    b.setAmount(Double.parseDouble(amt.getText().trim()));
                    b.setPaymentMethod(pmethod.getText().trim());
                    billingDAO.add(b);
                    refresh.doClick();
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid data"); }
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r==-1){ JOptionPane.showMessageDialog(this,"Select billing"); return; }
            int id = (int) model.getValueAt(r,0); if (JOptionPane.showConfirmDialog(this,"Delete?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){ billingDAO.delete(id); refresh.doClick(); }
        });

        refresh.doClick();
        return panel;
    }

    private JPanel medicalRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new String[]{"MedicalRecordID","Attribute1","Attribute2","Attribute3","CreatedAt"},0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bottom = new JPanel();
        JButton add = new JButton("Add"), delete = new JButton("Delete"), refresh = new JButton("Refresh");
        bottom.add(add); bottom.add(delete); bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            List<MedicalRecord> list = recordDAO.getAll();
            for (MedicalRecord m : list) model.addRow(new Object[]{m.getMedicalRecordID(), m.getAttribute1(), m.getAttribute2(), m.getAttribute3(), m.getCreatedAt()});
        });

        add.addActionListener(e -> {
            JTextField a1 = new JTextField(), a2 = new JTextField(), a3 = new JTextField();
            Object[] form = {"Attribute1:", a1, "Attribute2:", a2, "Attribute3:", a3};
            int ok = JOptionPane.showConfirmDialog(this, form, "Add Medical Record", JOptionPane.OK_CANCEL_OPTION);
            if (ok==JOptionPane.OK_OPTION) {
                MedicalRecord m = new MedicalRecord();
                m.setAttribute1(a1.getText().trim());
                m.setAttribute2(a2.getText().trim());
                m.setAttribute3(a3.getText().trim());
                recordDAO.add(m);
                refresh.doClick();
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r==-1){ JOptionPane.showMessageDialog(this,"Select record"); return; }
            int id = (int) model.getValueAt(r,0); if (JOptionPane.showConfirmDialog(this,"Delete?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){ recordDAO.delete(id); refresh.doClick(); }
        });

        refresh.doClick();
        return panel;
    }

    private JPanel prescriptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"PrescriptionID", "AppointmentID", "Attribute1", "Attribute2", "Attribute3", "CreatedAt"}, 0);
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        bottom.add(add); bottom.add(update); bottom.add(delete); bottom.add(refresh);
        panel.add(bottom, BorderLayout.SOUTH);

        refresh.addActionListener(e -> {
            model.setRowCount(0);
            for (Prescription p : prescriptionDAO.getAll()) {
                model.addRow(new Object[]{
                        p.getPrescriptionID(), p.getAppointmentID(),
                        p.getAttribute1(), p.getAttribute2(),
                        p.getAttribute3(), p.getCreatedAt()
                });
            }
        });

        add.addActionListener(e -> {
            JTextField appt = new JTextField();
            JTextField a1 = new JTextField();
            JTextField a2 = new JTextField();
            JTextField a3 = new JTextField();
            Object[] form = {"AppointmentID:", appt, "Attribute1:", a1, "Attribute2:", a2, "Attribute3:", a3};
            if (JOptionPane.showConfirmDialog(this, form, "Add Prescription", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Prescription p = new Prescription();
                try {
                    p.setAppointmentID(Integer.parseInt(appt.getText().trim()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid appointment ID");
                    return;
                }
                p.setAttribute1(a1.getText().trim());
                p.setAttribute2(a2.getText().trim());
                p.setAttribute3(a3.getText().trim());
                prescriptionDAO.add(p);
                JOptionPane.showMessageDialog(this, "Prescription added!");
                refresh.doClick();
            }
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select a record");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            JTextField a1 = new JTextField(model.getValueAt(r, 2).toString());
            JTextField a2 = new JTextField(model.getValueAt(r, 3).toString());
            JTextField a3 = new JTextField(model.getValueAt(r, 4).toString());
            Object[] form = {"Attribute1:", a1, "Attribute2:", a2, "Attribute3:", a3};
            if (JOptionPane.showConfirmDialog(this, form, "Update Prescription", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Prescription p = new Prescription();
                p.setPrescriptionID(id);
                p.setAttribute1(a1.getText().trim());
                p.setAttribute2(a2.getText().trim());
                p.setAttribute3(a3.getText().trim());
                prescriptionDAO.update(p);
                refresh.doClick();
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Select record to delete");
                return;
            }
            int id = (int) model.getValueAt(r, 0);
            prescriptionDAO.delete(id);
            refresh.doClick();
        });

        refresh.doClick();
        return panel;
    }




}
