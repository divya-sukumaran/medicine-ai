package dao;

import model.HealthRecord;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object responsible for all database operations
 * related to the {@link HealthRecord} entity.
 * Each user has at most one health record (enforced by a unique
 * constraint on user_id), so this DAO exposes a simple
 * "save or update" style API.
 */
public class HealthRecordDao {

    /**
     * Retrieves the health record belonging to a user.
     *
     * @param userId the id of the owning user
     * @return the user's {@link HealthRecord}, or null if none exists yet
     */
    public HealthRecord getByUserId(int userId) {
        String sql = "SELECT * FROM health_records WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates a new health record for a user.
     *
     * @param record the health record details to save
     * @return true if the insert succeeded
     */
    public boolean createRecord(HealthRecord record) {
        String sql = "INSERT INTO health_records (user_id, blood_group, height, weight, allergies, "
                + "medical_history, emergency_contact) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            bindRecord(stmt, record);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the existing health record of a user.
     *
     * @param record the health record details to update
     * @return true if the update succeeded
     */
    public boolean updateRecord(HealthRecord record) {
        String sql = "UPDATE health_records SET blood_group = ?, height = ?, weight = ?, allergies = ?, "
                + "medical_history = ?, emergency_contact = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, record.getBloodGroup());
            stmt.setString(2, record.getHeight());
            stmt.setString(3, record.getWeight());
            stmt.setString(4, record.getAllergies());
            stmt.setString(5, record.getMedicalHistory());
            stmt.setString(6, record.getEmergencyContact());
            stmt.setInt(7, record.getUserId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves a user's health record, creating it if it does not exist
     * yet or updating it otherwise.
     *
     * @param record the health record details to save
     * @return true if the save succeeded
     */
    public boolean saveOrUpdate(HealthRecord record) {
        if (getByUserId(record.getUserId()) == null) {
            return createRecord(record);
        }
        return updateRecord(record);
    }

    private void bindRecord(PreparedStatement stmt, HealthRecord record) throws SQLException {
        stmt.setInt(1, record.getUserId());
        stmt.setString(2, record.getBloodGroup());
        stmt.setString(3, record.getHeight());
        stmt.setString(4, record.getWeight());
        stmt.setString(5, record.getAllergies());
        stmt.setString(6, record.getMedicalHistory());
        stmt.setString(7, record.getEmergencyContact());
    }

    /**
     * Maps the current row of a {@link ResultSet} to a {@link HealthRecord} object.
     */
    private HealthRecord mapRow(ResultSet rs) throws SQLException {
        return new HealthRecord(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("blood_group"),
                rs.getString("height"),
                rs.getString("weight"),
                rs.getString("allergies"),
                rs.getString("medical_history"),
                rs.getString("emergency_contact")
        );
    }
}
