package dao;

import db.DatabaseConnection;
import model.Patient;
import model.Doctor;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class AdminDAO {
    private Connection conn;

    public AdminDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    // ====== Patients ======

    public DefaultTableModel getAllPatients() {
        String[] columns = {"ID", "Username", "Full Name", "Email", "Role"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            String sql = "SELECT patient_id, username, full_name, email, role FROM patient";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getLong("patient_id"));
                row.add(rs.getString("username"));
                row.add(rs.getString("full_name"));
                row.add(rs.getString("email"));
                row.add(rs.getString("role"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public boolean addPatient(String username, String password, String fullName, String email, String role) {
        String sql = "INSERT INTO patient(username, password_hash, full_name, email, role, created_at, last_login) VALUES(?,?,?,?,?,NOW(),NOW())";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, role);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePatient(long id, String fullName, String email, String role) {
        String sql = "UPDATE patient SET full_name=?, email=?, role=? WHERE patient_id=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, role);
            stmt.setLong(4, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePatient(long id) {
        String sql = "DELETE FROM patient WHERE patient_id=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ====== Doctors ======

    public DefaultTableModel getAllDoctors() {
        String[] columns = {"ID", "Name", "Identifier", "Status", "Location", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            String sql = "SELECT doctor_id, name, identifier, status, location, contact FROM doctor";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getLong("doctor_id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("identifier"));
                row.add(rs.getString("status"));
                row.add(rs.getString("location"));
                row.add(rs.getString("contact"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public boolean addDoctor(String name, String identifier, String status, String location, String contact) {
        String sql = "INSERT INTO doctor(name, identifier, status, location, contact, assigned_since) VALUES(?,?,?,?,?,NOW())";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, identifier);
            stmt.setString(3, status);
            stmt.setString(4, location);
            stmt.setString(5, contact);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDoctor(long id, String name, String identifier, String status, String location, String contact) {
        String sql = "UPDATE doctor SET name=?, identifier=?, status=?, location=?, contact=? WHERE doctor_id=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, identifier);
            stmt.setString(3, status);
            stmt.setString(4, location);
            stmt.setString(5, contact);
            stmt.setLong(6, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDoctor(long id) {
        String sql = "DELETE FROM doctor WHERE doctor_id=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
