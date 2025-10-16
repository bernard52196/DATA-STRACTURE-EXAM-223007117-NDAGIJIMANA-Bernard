package dao;

import db.DatabaseConnection;
import model.Doctor;
import java.sql.*;

public class DoctorDAO {
    private Connection conn;

    public DoctorDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    // Existing method
    public Doctor getDoctorByUsername(String username) {
        String sql = "SELECT * FROM doctor WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getLong("doctor_id"));
                d.setName(rs.getString("name"));
                d.setIdentifier(rs.getString("identifier"));
                d.setStatus(rs.getString("status"));
                d.setLocation(rs.getString("location"));
                d.setContact(rs.getString("contact"));
                d.setAssignedSince(rs.getDate("assigned_since"));
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ New method: get doctor by ID
    public Doctor getDoctorById(Long doctorId) {
        String sql = "SELECT * FROM doctor WHERE doctor_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Doctor d = new Doctor();
                d.setDoctorId(rs.getLong("doctor_id"));
                d.setName(rs.getString("name"));
                d.setIdentifier(rs.getString("identifier"));
                d.setStatus(rs.getString("status"));
                d.setLocation(rs.getString("location"));
                d.setContact(rs.getString("contact"));
                d.setAssignedSince(rs.getDate("assigned_since"));
                return d;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
