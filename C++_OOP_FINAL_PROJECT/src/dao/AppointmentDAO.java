package dao;

import db.DatabaseConnection;
import model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public boolean addAppointment(Appointment a) {
        String sql = "INSERT INTO appointment (patient_username, doctor_username, appointment_date, status, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getPatientUsername());
            ps.setString(2, a.getDoctorUsername());
            ps.setTimestamp(3, a.getAppointmentDate());
            ps.setString(4, a.getStatus());
            ps.setString(5, a.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public List<Appointment> getAppointmentsForPatient(String patientUsername) {
        List<Appointment> out = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE patient_username = ? ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patientUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentId(rs.getInt("appointment_id"));
                    a.setPatientUsername(rs.getString("patient_username"));
                    a.setDoctorUsername(rs.getString("doctor_username"));
                    a.setAppointmentDate(rs.getTimestamp("appointment_date"));
                    a.setStatus(rs.getString("status"));
                    a.setNotes(rs.getString("notes"));
                    out.add(a);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return out;
    }

    public List<Appointment> getAppointmentsForDoctor(String doctorUsername) {
        List<Appointment> out = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE doctor_username = ? ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctorUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment a = new Appointment();
                    a.setAppointmentId(rs.getInt("appointment_id"));
                    a.setPatientUsername(rs.getString("patient_username"));
                    a.setDoctorUsername(rs.getString("doctor_username"));
                    a.setAppointmentDate(rs.getTimestamp("appointment_date"));
                    a.setStatus(rs.getString("status"));
                    a.setNotes(rs.getString("notes"));
                    out.add(a);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return out;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> out = new ArrayList<>();
        String sql = "SELECT * FROM appointment ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Appointment a = new Appointment();
                a.setAppointmentId(rs.getInt("appointment_id"));
                a.setPatientUsername(rs.getString("patient_username"));
                a.setDoctorUsername(rs.getString("doctor_username"));
                a.setAppointmentDate(rs.getTimestamp("appointment_date"));
                a.setStatus(rs.getString("status"));
                a.setNotes(rs.getString("notes"));
                out.add(a);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return out;
    }

    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointment SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointment WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }
}
