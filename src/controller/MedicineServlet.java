package controller;

import dao.MedicineDao;
import model.Medicine;
import util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Handles all CRUD operations for medicine reminders:
 * add, edit, delete, and view.
 * The action to perform is chosen using the "action" request parameter.
 */
public class MedicineServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = getLoggedInUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "edit":
                showEditForm(request, response, userId);
                break;
            case "delete":
                deleteMedicine(request, response, userId);
                break;
            default:
                listMedicines(request, response, userId);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = getLoggedInUserId(request);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("update".equals(action)) {
            updateMedicine(request, response, userId);
        } else {
            addMedicine(request, response, userId);
        }
    }

    /**
     * Displays the list of medicines belonging to the logged-in user.
     */
    private void listMedicines(HttpServletRequest request, HttpServletResponse response, String userId)
            throws ServletException, IOException {

        List<Medicine> medicines = medicineDao.getMedicinesByUser(userId);
        request.setAttribute("medicines", medicines);
        request.getRequestDispatcher("medicines.jsp").forward(request, response);
    }

    /**
     * Validates and saves a new medicine reminder for the logged-in user.
     */
    private void addMedicine(HttpServletRequest request, HttpServletResponse response, String userId)
            throws ServletException, IOException {

        Medicine medicine = buildMedicineFromRequest(request);
        medicine.setUserId(userId);

        String error = validate(medicine);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("add-medicine.jsp").forward(request, response);
            return;
        }

        medicineDao.addMedicine(medicine);
        response.sendRedirect(request.getContextPath() + "/medicines");
    }

    /**
     * Loads a single medicine for editing and forwards to the edit form.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String userId)
            throws ServletException, IOException {

        int medicineId = Integer.parseInt(request.getParameter("id"));
        Medicine medicine = medicineDao.getMedicineById(medicineId, userId);

        if (medicine == null) {
            response.sendRedirect(request.getContextPath() + "/medicines");
            return;
        }

        request.setAttribute("medicine", medicine);
        request.getRequestDispatcher("edit-medicine.jsp").forward(request, response);
    }

    /**
     * Validates and applies changes to an existing medicine reminder.
     */
    private void updateMedicine(HttpServletRequest request, HttpServletResponse response, String userId)
            throws ServletException, IOException {

        Medicine medicine = buildMedicineFromRequest(request);
        medicine.setId(Integer.parseInt(request.getParameter("id")));
        medicine.setUserId(userId);

        String error = validate(medicine);
        if (error != null) {
            request.setAttribute("error", error);
            request.setAttribute("medicine", medicine);
            request.getRequestDispatcher("edit-medicine.jsp").forward(request, response);
            return;
        }

        medicineDao.updateMedicine(medicine);
        response.sendRedirect(request.getContextPath() + "/medicines");
    }

    /**
     * Deletes a medicine reminder owned by the logged-in user.
     */
    private void deleteMedicine(HttpServletRequest request, HttpServletResponse response, String userId)
            throws IOException {

        int medicineId = Integer.parseInt(request.getParameter("id"));
        medicineDao.deleteMedicine(medicineId, userId);
        response.sendRedirect(request.getContextPath() + "/medicines");
    }

    private Medicine buildMedicineFromRequest(HttpServletRequest request) {
        Medicine medicine = new Medicine();
        medicine.setMedicineName(request.getParameter("medicineName"));
        medicine.setDosage(request.getParameter("dosage"));
        medicine.setTime(request.getParameter("time"));
        medicine.setStartDate(request.getParameter("startDate"));
        medicine.setEndDate(request.getParameter("endDate"));
        medicine.setNotes(request.getParameter("notes"));
        return medicine;
    }

    /**
     * Performs basic server-side validation of medicine form fields.
     *
     * @return an error message if validation fails, or null if the input is valid
     */
    private String validate(Medicine medicine) {
        if (isBlank(medicine.getMedicineName()) || isBlank(medicine.getDosage())
                || isBlank(medicine.getTime()) || isBlank(medicine.getStartDate())
                || isBlank(medicine.getEndDate())) {
            return "All fields except notes are required.";
        }
        if (medicine.getStartDate().compareTo(medicine.getEndDate()) > 0) {
            return "Start date cannot be after end date.";
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
