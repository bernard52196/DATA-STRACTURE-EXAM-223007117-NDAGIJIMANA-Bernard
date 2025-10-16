package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final JTextField fullName = new JTextField();
    private final JTextField username = new JTextField();
    private final JTextField email = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JComboBox<String> roleBox = new JComboBox<>(new String[]{"PATIENT","DOCTOR","ADMIN"});

    public RegisterFrame() {
        setTitle("Register New User");
        setSize(400,320);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6,2,8,8));
        add(new JLabel("Full name:")); add(fullName);
        add(new JLabel("Username:")); add(username);
        add(new JLabel("Email:")); add(email);
        add(new JLabel("Password:")); add(password);
        add(new JLabel("Role:")); add(roleBox);

        JButton ok = new JButton("Register");
        JButton cancel = new JButton("Cancel");
        add(ok); add(cancel);

        ok.addActionListener(e -> register());
        cancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void register() {
        String nm = fullName.getText().trim();
        String user = username.getText().trim();
        String em = email.getText().trim();
        String pass = new String(password.getPassword()).trim();
        String role = (String) roleBox.getSelectedItem();

        if (nm.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full name, username and password are required");
            return;
        }

        UserDAO udao = new UserDAO();
        User u = new User();
        u.setUsername(user);
        u.setPasswordHash(pass);
        u.setEmail(em);
        u.setFullName(nm);
        u.setRole(role);

        boolean ok = udao.addUser(u);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Registration failed (maybe username exists).");
            return;
        }

        // create patient or doctor rows in their tables when appropriate
        if ("PATIENT".equals(role)) {
            try (var conn = db.DatabaseConnection.getConnection();
                 var ps = conn.prepareStatement("INSERT IGNORE INTO patient (username, full_name, email) VALUES (?, ?, ?)")) {
                ps.setString(1, user); ps.setString(2, nm); ps.setString(3, em); ps.executeUpdate();
            } catch (Exception ex) { ex.printStackTrace(); }
        } else if ("DOCTOR".equals(role)) {
            try (var conn = db.DatabaseConnection.getConnection();
                 var ps = conn.prepareStatement("INSERT IGNORE INTO doctor (username, full_name, email) VALUES (?, ?, ?)")) {
                ps.setString(1, user); ps.setString(2, nm); ps.setString(3, em); ps.executeUpdate();
            } catch (Exception ex) { ex.printStackTrace(); }
        }

        JOptionPane.showMessageDialog(this, "Registered successfully");
        dispose();
    }
}
