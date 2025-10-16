package ui;

import model.User;

import javax.swing.*;

public class AdminDashboard extends JFrame {
    private final User me;
    public AdminDashboard(User me) {
        this.me = me;
        setTitle("Admin Dashboard - " + me.getFullName());
        setSize(1000,640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        UserManagementPanel usersPanel = new UserManagementPanel();
        tabs.addTab("Users", usersPanel);

        // Appointments management (view all)
        tabs.addTab("Appointments", new AppointmentAdminPanel()); // we'll create simple admin panel below

        // Prescriptions admin view
        tabs.addTab("Prescriptions", new PrescriptionAdminPanel());

        add(tabs);
        setVisible(true);
    }
}
