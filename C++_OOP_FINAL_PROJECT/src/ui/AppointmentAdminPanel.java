package ui;

import dao.AppointmentDAO;
import model.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentAdminPanel extends JPanel {
    private final AppointmentDAO dao = new AppointmentDAO();
    private final JTable table = new JTable();

    public AppointmentAdminPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton delete = new JButton("Delete");
        top.add(refresh); top.add(delete);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refresh.addActionListener(e -> loadAll());
        delete.addActionListener(e -> deleteSelected());

        loadAll();
    }

    private void loadAll() {
        List<Appointment> list = dao.getAllAppointments();
        String[] cols = {"ID","Patient","Doctor","Date","Status","Notes"};
        DefaultTableModel m = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        for (Appointment a : list) m.addRow(new Object[]{a.getAppointmentId(), a.getPatientUsername(), a.getDoctorUsername(), a.getAppointmentDate(), a.getStatus(), a.getNotes()});
        table.setModel(m);
    }

    private void deleteSelected() {
        int r = table.getSelectedRow(); if (r==-1) { JOptionPane.showMessageDialog(this,"Select row"); return; }
        int id = (int) table.getValueAt(r,0);
        if (JOptionPane.showConfirmDialog(this,"Delete appointment "+id+"?")==JOptionPane.YES_OPTION) {
            if (dao.deleteAppointment(id)) { JOptionPane.showMessageDialog(this,"Deleted"); loadAll(); }
            else JOptionPane.showMessageDialog(this,"Delete failed");
        }
    }
}
