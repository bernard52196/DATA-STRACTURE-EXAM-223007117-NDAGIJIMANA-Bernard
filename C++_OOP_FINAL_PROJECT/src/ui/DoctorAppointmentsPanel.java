package ui;

import dao.AppointmentDAO;
import model.Appointment;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorAppointmentsPanel extends JPanel {
    private final AppointmentDAO dao = new AppointmentDAO();
    private final JTable table = new JTable();
    private final User me;

    public DoctorAppointmentsPanel(User me) {
        this.me = me;
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton approve = new JButton("Approve");
        JButton complete = new JButton("Complete");
        JButton addPresc = new JButton("Add Prescription");
        top.add(refresh); top.add(approve); top.add(complete); top.add(addPresc);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.addActionListener(e -> load());
        approve.addActionListener(e -> changeStatus("APPROVED"));
        complete.addActionListener(e -> changeStatus("COMPLETED"));
        addPresc.addActionListener(e -> addPrescriptionForSelected());

        load();
    }

    private void load() {
        List<Appointment> list = dao.getAppointmentsForDoctor(me.getUsername());
        String[] cols = {"ID","Patient","Date","Status","Notes"};
        DefaultTableModel m = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Appointment a: list) m.addRow(new Object[]{a.getAppointmentId(), a.getPatientUsername(), a.getAppointmentDate(), a.getStatus(), a.getNotes()});
        table.setModel(m);
    }

    private void changeStatus(String status) {
        int r = table.getSelectedRow(); if (r==-1) { JOptionPane.showMessageDialog(this,"Select appointment"); return; }
        int id = (int) table.getValueAt(r,0);
        if (dao.updateAppointmentStatus(id, status)) { JOptionPane.showMessageDialog(this,"Updated"); load(); } else JOptionPane.showMessageDialog(this,"Failed");
    }

    private void addPrescriptionForSelected() {
        int r = table.getSelectedRow(); if (r==-1) { JOptionPane.showMessageDialog(this,"Select appointment"); return; }
        int apptId = (int) table.getValueAt(r,0);
        String patient = (String) table.getValueAt(r,1);
        String med = JOptionPane.showInputDialog(this,"Medication:");
        String dosage = JOptionPane.showInputDialog(this,"Dosage:");
        String instr = JOptionPane.showInputDialog(this,"Instructions:");
        if (med==null || dosage==null) return;
        model.Prescription p = new model.Prescription();
        p.setAppointmentId(apptId);
        p.setPatientUsername(patient);
        p.setDoctorUsername(me.getUsername());
        p.setMedication(med); p.setDosage(dosage); p.setInstructions(instr);
        if (new dao.PrescriptionDAO().addPrescription(p)) { JOptionPane.showMessageDialog(this,"Prescription added"); }
        else JOptionPane.showMessageDialog(this,"Failed");
    }
}
