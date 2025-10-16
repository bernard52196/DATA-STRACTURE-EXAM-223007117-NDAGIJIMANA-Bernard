package ui;

import dao.PrescriptionDAO;
import model.Prescription;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPrescriptionsPanel extends JPanel {
    private final PrescriptionDAO dao = new PrescriptionDAO();
    private final JTable table = new JTable();
    private final User me;

    public DoctorPrescriptionsPanel(User me) {
        this.me = me;
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton delete = new JButton("Delete");
        top.add(refresh); top.add(delete);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.addActionListener(e -> load());
        delete.addActionListener(e -> deleteSelected());
        load();
    }

    private void load() {
        List<Prescription> list = dao.getByDoctor(me.getUsername());
        String[] cols = {"ID","Appt","Patient","Medication","Dosage","Issued At"};
        DefaultTableModel m = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Prescription p: list) m.addRow(new Object[]{p.getPrescriptionId(), p.getAppointmentId(), p.getPatientUsername(), p.getMedication(), p.getDosage(), p.getIssuedAt()});
        table.setModel(m);
    }

    private void deleteSelected() {
        int r = table.getSelectedRow(); if (r==-1) { JOptionPane.showMessageDialog(this,"Select row"); return; }
        int id = (int) table.getValueAt(r,0);
        if (JOptionPane.showConfirmDialog(this,"Delete prescription "+id+"?")==JOptionPane.YES_OPTION) {
            if (dao.deletePrescription(id)) { JOptionPane.showMessageDialog(this,"Deleted"); load(); }
            else JOptionPane.showMessageDialog(this,"Delete failed");
        }
    }
}
