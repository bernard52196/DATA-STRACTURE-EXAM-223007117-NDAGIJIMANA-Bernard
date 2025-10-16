package ui;

import dao.AppointmentDAO;
import dao.PrescriptionDAO;
import model.Appointment;
import model.Prescription;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientHistoryPanel extends JPanel {
    private final User me;
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final JTable apptTable = new JTable();
    private final JTable prescTable = new JTable();

    public PatientHistoryPanel(User me) {
        this.me = me;
        setLayout(new BorderLayout());
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Appointments"), BorderLayout.NORTH);
        top.add(new JScrollPane(apptTable), BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Prescriptions"), BorderLayout.NORTH);
        bottom.add(new JScrollPane(prescTable), BorderLayout.CENTER);
        split.setTopComponent(top);
        split.setBottomComponent(bottom);
        split.setDividerLocation(250);
        add(split, BorderLayout.CENTER);
        load();
    }

    private void load() {
        List<Appointment> appts = appointmentDAO.getAppointmentsForPatient(me.getUsername());
        DefaultTableModel am = new DefaultTableModel(new String[]{"ID","Doctor","Date","Status","Notes"},0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Appointment a: appts) am.addRow(new Object[]{a.getAppointmentId(), a.getDoctorUsername(), a.getAppointmentDate(), a.getStatus(), a.getNotes()});
        apptTable.setModel(am);

        List<Prescription> pres = prescriptionDAO.getByPatient(me.getUsername());
        DefaultTableModel pm = new DefaultTableModel(new String[]{"ID","ApptID","Doctor","Medication","Dosage","Issued"},0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Prescription p: pres) pm.addRow(new Object[]{p.getPrescriptionId(), p.getAppointmentId(), p.getDoctorUsername(), p.getMedication(), p.getDosage(), p.getIssuedAt()});
        prescTable.setModel(pm);
    }
}
