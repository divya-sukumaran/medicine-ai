package model;

/**
 * Represents the application-side profile of a user, keyed by the
 * UUID assigned by Supabase Auth. Passwords are never stored here —
 * Supabase Auth owns authentication entirely; this table only holds
 * the profile details the app needs to display and to link medicines
 * and health records to (via a UUID foreign key).
 */
public class UserProfile {

    private String id;
    private String name;
    private String email;
    private String phone;

    public UserProfile() {
    }

    public UserProfile(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
