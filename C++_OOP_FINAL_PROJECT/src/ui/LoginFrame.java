package ui;

import dao.*;
import model.*;
import util.DBConnection;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Admin","Doctor","Patient"});
    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {
        setTitle("Healthcare Portal - Login");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("WELCOME TO HEALTHCARE PORTAL SYSTEM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        p.add(lblTitle, gbc);
        gbc.gridwidth=1;
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(new Color(0, 102, 204));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        p.add(lblTitle, gbc);
        gbc.gridwidth=1;


        gbc.gridy++; gbc.gridx=0; p.add(new JLabel("Username:"), gbc);
        gbc.gridx=1; p.add(txtUsername, gbc);

        gbc.gridy++; gbc.gridx=0; p.add(new JLabel("Password:"), gbc);
        gbc.gridx=1; p.add(txtPassword, gbc);

        gbc.gridy++; gbc.gridx=0; p.add(new JLabel("Role:"), gbc);
        gbc.gridx=1; p.add(cmbRole, gbc);

        gbc.gridy++; gbc.gridx=0; gbc.gridwidth=2;
        JPanel bp = new JPanel();
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");
        bp.add(btnLogin); bp.add(btnRegister);
        btnLogin.setBackground(new Color(69, 181, 124));
        btnLogin.setForeground(Color.WHITE); // White text
        btnLogin.setFocusPainted(false);
        btnRegister.setBackground(new Color(19, 151, 168));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);

        bp.add(btnLogin);
        bp.add(btnRegister);
        p.add(bp, gbc);


        p.add(bp, gbc);

        add(p);

        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> {
            dispose(); new RegisterFrame().setVisible(true);
        });
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String role = cmbRole.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter credentials");
            return;
        }

        User u = userDAO.authenticateUser(username, password);
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
            return;
        }
        if (!u.getRole().equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this, "Role mismatch. You are: " + u.getRole());
            return;
        }

        JOptionPane.showMessageDialog(this, "Welcome " + u.getFullName());
        dispose();

        switch (u.getRole().toLowerCase()) {
            case "admin" -> new AdminDashboard(u).setVisible(true);
            case "doctor" -> {
                DoctorDAO ddao = new DoctorDAO();
                Doctor d = ddao.getByUserId(u.getUserID());
                if (d == null) {
                    JOptionPane.showMessageDialog(this, "No doctor profile found. Contact admin.");
                    new AdminDashboard(u).setVisible(true);
                } else {
                    new DoctorDashboard(u, d).setVisible(true);
                }
            }
            case "patient" -> {
                PatientDAO pdao = new PatientDAO();
                Patient p = pdao.getByUserId(u.getUserID());
                if (p == null) {
                    JOptionPane.showMessageDialog(this, "No patient profile found. Contact admin.");
                    new AdminDashboard(u).setVisible(true);
                } else {
                    new PatientDashboard(u, p).setVisible(true);
                }
            }
            default -> JOptionPane.showMessageDialog(this, "Unknown role");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
