package dao;

import model.Medicine;
import util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object responsible for all database operations
 * related to the {@link Medicine} entity.
 */
public class MedicineDao {

    /**
     * Adds a new medicine reminder for a user.
     *
     * @param medicine the medicine details to save
     * @return true if the insert succeeded
     */
    public boolean addMedicine(Medicine medicine) {
        String sql = "INSERT INTO medicines (user_id, medicine_name, dosage, time, start_date, end_date, notes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicine.getUserId());
            stmt.setString(2, medicine.getMedicineName());
            stmt.setString(3, medicine.getDosage());
            stmt.setString(4, medicine.getTime());
            stmt.setDate(5, Date.valueOf(medicine.getStartDate()));
            stmt.setDate(6, Date.valueOf(medicine.getEndDate()));
            stmt.setString(7, medicine.getNotes());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing medicine reminder. The record is scoped to the
     * given user id to prevent one user from editing another user's data.
     *
     * @param medicine the medicine details to update (id must be set)
     * @return true if the update succeeded
     */
    public boolean updateMedicine(Medicine medicine) {
        String sql = "UPDATE medicines SET medicine_name = ?, dosage = ?, time = ?, "
                + "start_date = ?, end_date = ?, notes = ? WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicine.getMedicineName());
            stmt.setString(2, medicine.getDosage());
            stmt.setString(3, medicine.getTime());
            stmt.setDate(4, Date.valueOf(medicine.getStartDate()));
            stmt.setDate(5, Date.valueOf(medicine.getEndDate()));
            stmt.setString(6, medicine.getNotes());
            stmt.setInt(7, medicine.getId());
            stmt.setString(8, medicine.getUserId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a medicine reminder belonging to the given user.
     *
     * @param medicineId the id of the medicine to delete
     * @param userId     the id of the owning user
     * @return true if the delete succeeded
     */
    public boolean deleteMedicine(int medicineId, String userId) {
        String sql = "DELETE FROM medicines WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicineId);
            stmt.setString(2, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a single medicine by id, scoped to the owning user.
     *
     * @param medicineId the id of the medicine to fetch
     * @param userId     the id of the owning user
     * @return the matching {@link Medicine}, or null if not found
     */
    public Medicine getMedicineById(int medicineId, String userId) {
        String sql = "SELECT * FROM medicines WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medicineId);
            stmt.setString(2, userId);

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
     * Retrieves all medicines belonging to a user, most recent first.
     *
     * @param userId the id of the owning user
     * @return a list of the user's medicines
     */
    public List<Medicine> getMedicinesByUser(String userId) {
        List<Medicine> medicines = new ArrayList<>();
        String sql = "SELECT * FROM medicines WHERE user_id = ? ORDER BY start_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicines.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    /**
     * Counts the total number of medicines a user has ever added.
     *
     * @param userId the id of the owning user
     * @return the total medicine count
     */
    public int countMedicinesByUser(String userId) {
        String sql = "SELECT COUNT(*) AS total FROM medicines WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Counts how many of the user's medicines are active today
     * (today's date falls within the medicine's start and end date).
     *
     * @param userId the id of the owning user
     * @return the count of medicines active today
     */
    public int countTodaysMedicines(String userId) {
        String sql = "SELECT COUNT(*) AS total FROM medicines WHERE user_id = ? "
                + "AND CURRENT_DATE BETWEEN start_date AND end_date";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Maps the current row of a {@link ResultSet} to a {@link Medicine} object.
     */
    private Medicine mapRow(ResultSet rs) throws SQLException {
        return new Medicine(
                rs.getInt("id"),
                rs.getString("user_id"),
                rs.getString("medicine_name"),
                rs.getString("dosage"),
                rs.getString("time"),
                rs.getString("start_date"),
                rs.getString("end_date"),
                rs.getString("notes")
        );
    }
}
