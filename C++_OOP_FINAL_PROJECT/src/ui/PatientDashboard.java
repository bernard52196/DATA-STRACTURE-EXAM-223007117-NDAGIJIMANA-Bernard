package ui;

import model.User;

import javax.swing.*;

public class PatientDashboard extends JFrame {
    private final User me;
    public PatientDashboard(User me) {
        this.me = me;
        setTitle("Patient Dashboard - " + me.getFullName());
        setSize(1000,640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Book Appointment", new PatientBookAppointmentPanel(me));
        tabs.addTab("My Appointments & History", new PatientHistoryPanel(me));
        tabs.addTab("My Prescriptions", new PatientPrescriptionsPanel(me));

        add(tabs);
        setVisible(true);
    }
}
