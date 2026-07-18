package controller;

import dao.HealthRecordDao;
import model.HealthRecord;
import util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles viewing and saving (create/update) of the logged-in user's
 * personal health record.
 */
public class HealthRecordServlet extends HttpServlet {

    private final HealthRecordDao healthRecordDao = new HealthRecordDao();

    /**
     * Loads the logged-in user's health record and forwards to the view page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = getLoggedInUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        HealthRecord record = healthRecordDao.getByUserId(userId);
        request.setAttribute("record", record);
        request.getRequestDispatcher("health-record.jsp").forward(request, response);
    }

    /**
     * Validates and saves the health record form, creating it if it
     * does not exist yet or updating it otherwise.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = getLoggedInUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        HealthRecord record = new HealthRecord();
        record.setUserId(userId);
        record.setBloodGroup(request.getParameter("bloodGroup"));
        record.setHeight(request.getParameter("height"));
        record.setWeight(request.getParameter("weight"));
        record.setAllergies(request.getParameter("allergies"));
        record.setMedicalHistory(request.getParameter("medicalHistory"));
        record.setEmergencyContact(request.getParameter("emergencyContact"));

        String error = validate(record);
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("record", record);
            request.getRequestDispatcher("health-record.jsp").forward(request, response);
            return;
        }

        healthRecordDao.saveOrUpdate(record);
        // "saved=1" makes the page show a success message after the redirect
        response.sendRedirect(request.getContextPath() + "/health-record?saved=1");
    }

    /**
     * Performs basic server-side validation of health record form fields.
     *
     * @return an error message if validation fails, or null if the input is valid
     */
    private String validate(HealthRecord record) {
        if (isBlank(record.getBloodGroup()) || isBlank(record.getHeight())
                || isBlank(record.getWeight()) || isBlank(record.getEmergencyContact())) {
            return "Blood group, height, weight, and emergency contact are required.";
        }
        if (!record.getEmergencyContact().matches("^[0-9]{10}$")) {
            return "Please enter a valid 10-digit emergency contact number.";
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Reads the logged-in user's id from the current session.
     *
     * @return the user id, or null if no user is logged in
     */
    private String getLoggedInUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.SESSION_USER_ID) == null) {
            return null;
        }
        return (String) session.getAttribute(Constants.SESSION_USER_ID);
    }
}
