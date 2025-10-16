package dao;

import db.DatabaseConnection;
import model.Billing;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {
    private Connection conn;

    public BillingDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
    }

    public List<Billing> getBillingByPatientId(Long patientId) {
        List<Billing> list = new ArrayList<>();
        String sql = "SELECT * FROM billing WHERE patient_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Billing b = new Billing();
                b.setBillingId(rs.getLong("billing_id"));
                b.setAttribute1(rs.getString("attribute1"));
                b.setAttribute2(rs.getDouble("attribute2"));
                b.setAttribute3(rs.getString("attribute3"));
                b.setCreatedAt(rs.getTimestamp("created_at"));
                b.setPatientId(rs.getLong("patient_id"));
                b.setMedicalRecordId(rs.getLong("medical_record_id"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
