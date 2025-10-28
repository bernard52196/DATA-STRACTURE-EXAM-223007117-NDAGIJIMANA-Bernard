package dao;

import model.Patient;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public int addProfile(int userId, String fullName, String gender, Date dob) {
        String sql = "INSERT INTO Patient (UserID, FullName, Gender, DOB) VALUES (?,?,?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, fullName);
            ps.setString(3, gender);
            if (dob == null) ps.setNull(4, Types.DATE); else ps.setDate(4, dob);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    public Patient getByUserId(int userId) {
        String sql = "SELECT * FROM Patient WHERE UserID=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Patient p = new Patient();
                p.setPatientID(rs.getInt("PatientID"));
                p.setUserID(rs.getInt("UserID"));
                p.setFullName(rs.getString("FullName"));
                p.setGender(rs.getString("Gender"));
                p.setDob(rs.getDate("DOB"));
                p.setCreatedAt(rs.getTimestamp("CreatedAt"));
                return p;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Patient> getAll() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM Patient ORDER BY CreatedAt DESC";
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Patient p = new Patient();
                p.setPatientID(rs.getInt("PatientID"));
                p.setUserID(rs.getInt("UserID"));
                p.setFullName(rs.getString("FullName"));
                p.setGender(rs.getString("Gender"));
                p.setDob(rs.getDate("DOB"));
                p.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void update(Patient p) {
        String sql = "UPDATE Patient SET FullName=?, Gender=?, DOB=? WHERE PatientID=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getFullName());
            ps.setString(2, p.getGender());
            ps.setDate(3, p.getDob());
            ps.setInt(4, p.getPatientID());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Patient WHERE PatientID=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
