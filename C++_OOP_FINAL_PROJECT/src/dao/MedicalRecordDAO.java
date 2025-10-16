package dao;

import db.DatabaseConnection;
import model.MedicalRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {
    private Connection conn;

    public MedicalRecordDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    public List<MedicalRecord> getMedicalRecordsByPatientId(Long patientId) {
        List<MedicalRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM medical_record WHERE patient_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MedicalRecord m = new MedicalRecord();
                m.setMedicalRecordId(rs.getLong("medical_record_id"));
                m.setAttribute1(rs.getString("attribute1"));
                m.setAttribute2(rs.getString("attribute2"));
                m.setAttribute3(rs.getString("attribute3"));
                m.setCreatedAt(rs.getTimestamp("created_at"));
                m.setPatientId(rs.getLong("patient_id"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
