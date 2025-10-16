package ui;

import dao.PrescriptionDAO;
import model.Prescription;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPrescriptionsPanel extends JPanel {
    private final PrescriptionDAO dao = new PrescriptionDAO();
    private final JTable table = new JTable();
    private final User me;

    public PatientPrescriptionsPanel(User me) {
        this.me = me;
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh");
        add(refresh, BorderLayout.NORTH);
        refresh.addActionListener(e -> load());
        load();
    }

    private void load() {
        List<Prescription> list = dao.getByPatient(me.getUsername());
        String[] cols = {"ID","Appt","Doctor","Medication","Dosage","Issued At"};
        DefaultTableModel m = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Prescription p: list) m.addRow(new Object[]{p.getPrescriptionId(), p.getAppointmentId(), p.getDoctorUsername(), p.getMedication(), p.getDosage(), p.getIssuedAt()});
        table.setModel(m);
    }
}
