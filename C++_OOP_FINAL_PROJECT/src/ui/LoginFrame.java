package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginFrame() {
        setTitle("Healthcare Portal - System");
        setSize(420, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4,2,8,8));
        panel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        panel.add(loginBtn);
        panel.add(registerBtn);

        add(panel);
        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> new RegisterFrame());

        setVisible(true);
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) { JOptionPane.showMessageDialog(this,"Enter username & password"); return; }

        UserDAO udao = new UserDAO();
        User user = udao.getByUsername(username);
        if (user != null && user.getPasswordHash().equals(password)) {
            udao.updateLastLogin(username);
            JOptionPane.showMessageDialog(this, "Welcome " + user.getFullName());
            dispose();
            switch (user.getRole()) {
                case "ADMIN": new AdminDashboard(user); break;
                case "DOCTOR": new DoctorDashboard(user); break;
                case "PATIENT": new PatientDashboard(user); break;
                default: JOptionPane.showMessageDialog(this,"Unknown role");
            }
        } else {
            JOptionPane.showMessageDialog(this,"Invalid credentials!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
