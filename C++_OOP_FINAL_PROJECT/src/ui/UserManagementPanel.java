package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private final UserDAO dao = new UserDAO();
    private final JTable table = new JTable();
    private final JTextField searchField = new JTextField(16);

    public UserManagementPanel() {
        setLayout(new BorderLayout(6,6));
        JPanel top = new JPanel();
        JButton btnAll = new JButton("All");
        JButton btnDoctors = new JButton("Doctors");
        JButton btnPatients = new JButton("Patients");
        JButton btnSearch = new JButton("Search");
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDel = new JButton("Delete");

        top.add(new JLabel("Keyword:"));
        top.add(searchField);
        top.add(btnSearch);
        top.add(btnAll);
        top.add(btnDoctors);
        top.add(btnPatients);
        top.add(btnAdd);
        top.add(btnEdit);
        top.add(btnDel);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnAll.addActionListener(e -> loadAll());
        btnDoctors.addActionListener(e -> loadRole("DOCTOR"));
        btnPatients.addActionListener(e -> loadRole("PATIENT"));
        btnSearch.addActionListener(e -> search(searchField.getText().trim()));
        btnAdd.addActionListener(e -> new RegisterFrame());
        btnEdit.addActionListener(e -> editSelected());
        btnDel.addActionListener(e -> deleteSelected());

        loadAll();
    }

    public void loadAll() { refresh(dao.getAllUsers()); }
    public void loadRole(String role) { refresh(dao.getUsersByRole(role)); }
    public void search(String k) { refresh(dao.searchUsers(k)); }

    private void refresh(List<User> list) {
        String[] cols = {"Username","Full Name","Email","Role"};
        DefaultTableModel m = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        for (User u : list) m.addRow(new Object[]{u.getUsername(), u.getFullName(), u.getEmail(), u.getRole()});
        table.setModel(m);
    }

    private void editSelected() {
        int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this,"Select row"); return; }
        String username = (String) table.getValueAt(r,0);
        UserDAO udao = new UserDAO();
        User u = udao.getByUsername(username);
        if (u == null) { JOptionPane.showMessageDialog(this,"User not found"); return; }

        String full = JOptionPane.showInputDialog(this, "Full name:", u.getFullName());
        String email = JOptionPane.showInputDialog(this, "Email:", u.getEmail());
        String pwd = JOptionPane.showInputDialog(this, "Password (leave blank to keep):", "");
        String role = JOptionPane.showInputDialog(this, "Role:", u.getRole());

        if (full != null) u.setFullName(full);
        if (email != null) u.setEmail(email);
        if (pwd != null && !pwd.trim().isEmpty()) u.setPasswordHash(pwd);
        if (role != null) u.setRole(role.toUpperCase());

        if (udao.updateUser(u)) {
            JOptionPane.showMessageDialog(this,"Updated");
            loadAll();
        } else JOptionPane.showMessageDialog(this,"Update failed");
    }

    private void deleteSelected() {
        int r = table.getSelectedRow(); if (r == -1) { JOptionPane.showMessageDialog(this,"Select row"); return; }
        String username = (String) table.getValueAt(r,0);
        if (JOptionPane.showConfirmDialog(this,"Delete user "+username+"?")==JOptionPane.YES_OPTION) {
            if (dao.deleteUser(username)) { JOptionPane.showMessageDialog(this,"Deleted"); loadAll(); }
            else JOptionPane.showMessageDialog(this,"Delete failed");
        }
    }
}
