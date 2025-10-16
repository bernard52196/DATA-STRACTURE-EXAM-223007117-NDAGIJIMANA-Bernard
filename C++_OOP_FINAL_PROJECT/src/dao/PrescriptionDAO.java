package dao;

import db.DatabaseConnection;
import model.Prescription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    public boolean addPrescription(Prescription p) {
        String sql = "INSERT INTO prescription (appointment_id, patient_username, doctor_username, medication, dosage, instructions) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getAppointmentId());
            ps.setString(2, p.getPatientUsername());
            ps.setString(3, p.getDoctorUsername());
            ps.setString(4, p.getMedication());
            ps.setString(5, p.getDosage());
            ps.setString(6, p.getInstructions());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public List<Prescription> getByPatient(String patientUsername) {
        List<Prescription> out = new ArrayList<>();
        String sql = "SELECT * FROM prescription WHERE patient_username = ? ORDER BY issued_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patientUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(extract(rs));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return out;
    }

    public List<Prescription> getByDoctor(String doctorUsername) {
        List<Prescription> out = new ArrayList<>();
        String sql = "SELECT * FROM prescription WHERE doctor_username = ? ORDER BY issued_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctorUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(extract(rs));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return out;
    }

    public boolean deletePrescription(int id) {
        String sql = "DELETE FROM prescription WHERE prescription_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    private Prescription extract(ResultSet rs) throws SQLException {
        Prescription p = new Prescription();
        p.setPrescriptionId(rs.getInt("prescription_id"));
        p.setAppointmentId(rs.getInt("appointment_id"));
        p.setPatientUsername(rs.getString("patient_username"));
        p.setDoctorUsername(rs.getString("doctor_username"));
        p.setMedication(rs.getString("medication"));
        p.setDosage(rs.getString("dosage"));
        p.setInstructions(rs.getString("instructions"));
        p.setIssuedAt(rs.getTimestamp("issued_at"));
        return p;
    }
}
