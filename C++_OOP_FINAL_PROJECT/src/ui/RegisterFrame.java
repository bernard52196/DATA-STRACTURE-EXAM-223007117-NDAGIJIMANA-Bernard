package ui;

import dao.*;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class RegisterFrame extends JFrame {
    private final JTextField tfUsername = new JTextField(20);
    private final JPasswordField pf = new JPasswordField(20);
    private final JTextField tfEmail = new JTextField(20);
    private final JTextField tfFull = new JTextField(20);

    public RegisterFrame() {
        setTitle("Register - Healthcare Portal");
        setSize(520, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Styling
        JLabel lblTitle = new JLabel("REGISTER TO HEALTHCARE PORTAL SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 102, 204)); // Blue Color
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        main.add(lblTitle, gbc);
        gbc.gridwidth=1;

        gbc.gridy++; gbc.gridx=0; main.add(new JLabel("Username:"), gbc);
        gbc.gridx=1; main.add(tfUsername, gbc);

        gbc.gridy++; gbc.gridx=0; main.add(new JLabel("Password:"), gbc);
        gbc.gridx=1; main.add(pf, gbc);

        gbc.gridy++; gbc.gridx=0; main.add(new JLabel("Email:"), gbc);
        gbc.gridx=1; main.add(tfEmail, gbc);

        gbc.gridy++; gbc.gridx=0; main.add(new JLabel("Full Name:"), gbc);
        gbc.gridx=1; main.add(tfFull, gbc);

        // Buttons Styled
        gbc.gridy++;
        JButton btnDoctor = new JButton("Register as Doctor");
        btnDoctor.setBackground(new Color(0, 153, 76)); // Green
        btnDoctor.setForeground(Color.WHITE);
        btnDoctor.setFocusPainted(false);

        JButton btnPatient = new JButton("Register as Patient");
        btnPatient.setBackground(new Color(204, 102, 0)); // Orange-Brown
        btnPatient.setForeground(Color.WHITE);
        btnPatient.setFocusPainted(false);

        JPanel rolePanel = new JPanel();
        rolePanel.add(btnDoctor);
        rolePanel.add(btnPatient);
        gbc.gridx=0; gbc.gridwidth=2;
        main.add(rolePanel, gbc);
        gbc.gridwidth=1;

        gbc.gridy++;
        JButton btnBack = new JButton("Back to Login");
        btnBack.setBackground(new Color(220, 54, 54)); // Red
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        main.add(btnBack, gbc);

        add(main);

        // Button Click Actions
        btnDoctor.addActionListener(e -> registerDoctor());
        btnPatient.addActionListener(e -> registerPatient());
        btnBack.addActionListener(e -> {
            dispose(); new LoginFrame().setVisible(true);
        });
    }

    private void registerDoctor() {
        String username = tfUsername.getText().trim();
        String password = new String(pf.getPassword());
        String email = tfEmail.getText().trim();
        String full = tfFull.getText().trim();

        JTextField location = new JTextField();
        JTextField identifier = new JTextField();
        Object[] form = {"Identifier:", identifier, "Location:", location};
        int ok = JOptionPane.showConfirmDialog(this, form, "Doctor Details", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        if (username.isEmpty() || password.isEmpty() || full.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        UserDAO udao = new UserDAO();
        int userId = udao.registerUser(username, password, email, full, "Doctor");
        if (userId <= 0) { JOptionPane.showMessageDialog(this, "Registration failed"); return; }

        DoctorDAO ddao = new DoctorDAO();
        Doctor d = new Doctor();
        d.setUserID(userId);
        d.setName(full);
        d.setIdentifier(identifier.getText().trim());
        d.setStatus("Active");
        d.setLocation(location.getText().trim());
        d.setContact(email);
        d.setAssignedSince(new Date(System.currentTimeMillis()));
        ddao.add(d);

        JOptionPane.showMessageDialog(this, "Doctor registered successfully!");
        dispose(); new LoginFrame().setVisible(true);
    }

    private void registerPatient() {
        String username = tfUsername.getText().trim();
        String password = new String(pf.getPassword());
        String email = tfEmail.getText().trim();
        String full = tfFull.getText().trim();

        JTextField gender = new JTextField();
        JTextField dob = new JTextField("YYYY-MM-DD");
        Object[] form = {"Gender:", gender, "Date of Birth:", dob};
        int ok = JOptionPane.showConfirmDialog(this, form, "Patient Details", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        if (username.isEmpty() || password.isEmpty() || full.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        UserDAO udao = new UserDAO();
        int userId = udao.registerUser(username, password, email, full, "Patient");
        if (userId <= 0) { JOptionPane.showMessageDialog(this, "Registration failed"); return; }

        PatientDAO pdao = new PatientDAO();
        Date dobDate = null;
        try { dobDate = Date.valueOf(dob.getText().trim()); } catch (Exception ex) { /* allow null */ }
        pdao.addProfile(userId, full, gender.getText().trim(), dobDate);

        JOptionPane.showMessageDialog(this, "Patient registered successfully!");
        dispose(); new LoginFrame().setVisible(true);
    }
}
