package model;

/**
 * Represents a medicine reminder created by a user.
 * Dates are stored as ISO strings (yyyy-MM-dd) so this class can be
 * used directly by both the DAO layer and the JSP views without
 * extra conversion.
 */
public class Medicine {

    private int id;
    private int userId;
    private String medicineName;
    private String dosage;
    private String time;
    private String startDate;
    private String endDate;
    private String notes;

    public Medicine() {
    }

    public Medicine(int id, int userId, String medicineName, String dosage, String time,
                     String startDate, String endDate, String notes) {
        this.id = id;
        this.userId = userId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.time = time;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
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

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
