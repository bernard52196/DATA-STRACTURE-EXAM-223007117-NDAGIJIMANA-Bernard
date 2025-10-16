package dao;

import db.DatabaseConnection;
import model.Patient;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class PatientDAO {
    private Connection conn;

    public PatientDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    // ===== Get patient by username =====
    public Patient getPatientByUsername(String username) {
        String sql = "SELECT * FROM patient WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Patient p = new Patient();
                p.setPatientId(rs.getLong("patient_id"));
                p.setUsername(rs.getString("username"));
                p.setPasswordHash(rs.getString("password_hash"));
                p.setEmail(rs.getString("email"));
                p.setFullName(rs.getString("full_name"));
                p.setRole(rs.getString("role"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setLastLogin(rs.getTimestamp("last_login"));
                return p; // ✅ returns Patient
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // ✅ must return null if not found
    }

    // ===== Update last login =====
    public boolean updateLastLogin(Long patientId) {
        String sql = "UPDATE patient SET last_login = NOW() WHERE patient_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, patientId);
            int rows = stmt.executeUpdate();
            return rows > 0; // ✅ return true if updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // ✅ return false on exception
        }
    }

    // ===== Get Appointments by patientId =====
    public DefaultTableModel getAppointmentsByPatientId(long patientId) {
        String[] columns = {"ID", "Order Number", "Date", "Status", "Total Amount", "Payment Method", "Notes", "Doctor ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            String sql = "SELECT * FROM appointment WHERE patient_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getLong("appointment_id"));
                row.add(rs.getString("order_number"));
                row.add(rs.getTimestamp("appointment_date"));
                row.add(rs.getString("status"));
                row.add(rs.getDouble("total_amount"));
                row.add(rs.getString("payment_method"));
                row.add(rs.getString("notes"));
                row.add(rs.getLong("doctor_id"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model; // ✅ always returns a model
    }

    // ===== Get Prescriptions by patientId =====
    public DefaultTableModel getPrescriptionsByPatientId(long patientId) {
        String[] columns = {"ID", "Code", "Medication", "Dosage", "Created At", "Appointment ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            String sql = "SELECT * FROM prescription WHERE patient_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getLong("prescription_id"));
                row.add(rs.getString("code"));
                row.add(rs.getString("medication"));
                row.add(rs.getString("dosage"));
                row.add(rs.getTimestamp("created_at"));
                row.add(rs.getLong("appointment_id"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model; // ✅ always returns a model
    }

    // ===== Get Billing by patientId =====
    public DefaultTableModel getBillingByPatientId(long patientId) {
        String[] columns = {"ID", "Invoice Number", "Amount", "Status", "Created At", "Medical Record ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try {
            String sql = "SELECT * FROM billing WHERE patient_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getLong("billing_id"));
                row.add(rs.getString("invoice_number"));
                row.add(rs.getDouble("amount"));
                row.add(rs.getString("status"));
                row.add(rs.getTimestamp("created_at"));
                row.add(rs.getLong("medical_record_id"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model; // ✅ always returns a model
    }
}
