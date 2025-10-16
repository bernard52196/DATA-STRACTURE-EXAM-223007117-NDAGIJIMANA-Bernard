package ui;

import model.User;

import javax.swing.*;

public class DoctorDashboard extends JFrame {
    private final User me;
    public DoctorDashboard(User me) {
        this.me = me;
        setTitle("Doctor Dashboard - " + me.getFullName());
        setSize(1000,640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("My Appointments", new DoctorAppointmentsPanel(me));
        tabs.addTab("My Prescriptions", new DoctorPrescriptionsPanel(me));
        add(tabs);
        setVisible(true);
    }
}
