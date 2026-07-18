package dao;

import model.UserProfile;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object responsible for the {@code profiles} table:
 * the app-side profile row (name, email, phone) linked to a Supabase
 * Auth user by matching UUID. Authentication itself is handled by
 * {@link service.SupabaseAuthService}, not this class.
 */
public class ProfileDao {

    /**
     * Creates the profile row for a newly registered Supabase Auth user.
     * Safe to call more than once for the same id (idempotent upsert).
     *
     * @param profile the profile to save (id must be the Supabase Auth user's UUID)
     * @return true if the insert/update succeeded
     */
    public boolean createProfile(UserProfile profile) {
        String sql = "INSERT INTO profiles (id, name, email, phone) VALUES (?, ?, ?, ?) "
                + "ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, "
                + "email = EXCLUDED.email, phone = EXCLUDED.phone";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, profile.getId());
            stmt.setString(2, profile.getName());
            stmt.setString(3, profile.getEmail());
            stmt.setString(4, profile.getPhone());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a user's profile by their Supabase Auth user id.
     *
     * @param userId the Supabase Auth user's UUID
     * @return the matching {@link UserProfile}, or null if not found
     */
    public UserProfile getById(String userId) {
        String sql = "SELECT * FROM profiles WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserProfile(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
