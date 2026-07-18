package model;

/**
 * Represents the personal health record of a user, containing
 * basic medical information used for quick reference in emergencies.
 */
public class HealthRecord {

    private int id;
    private int userId;
    private String bloodGroup;
    private String height;
    private String weight;
    private String allergies;
    private String medicalHistory;
    private String emergencyContact;

    public HealthRecord() {
    }

    public HealthRecord(int id, int userId, String bloodGroup, String height, String weight,
                         String allergies, String medicalHistory, String emergencyContact) {
        this.id = id;
        this.userId = userId;
        this.bloodGroup = bloodGroup;
        this.height = height;
        this.weight = weight;
        this.allergies = allergies;
        this.medicalHistory = medicalHistory;
        this.emergencyContact = emergencyContact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}
