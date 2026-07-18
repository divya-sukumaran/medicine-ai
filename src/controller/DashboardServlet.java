package controller;

import dao.HealthRecordDao;
import dao.MedicineDao;
import model.HealthRecord;
import util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Gathers the summary statistics shown on the dashboard page:
 * total medicines, today's medicines, and health record status.
 */
public class DashboardServlet extends HttpServlet {

    private final MedicineDao medicineDao = new MedicineDao();
    private final HealthRecordDao healthRecordDao = new HealthRecordDao();

    /**
     * Loads the dashboard statistics for the logged-in user and
     * forwards to dashboard.jsp for rendering.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.SESSION_USER_ID) == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        int userId = (int) session.getAttribute(Constants.SESSION_USER_ID);

        int totalMedicines = medicineDao.countMedicinesByUser(userId);
        int todaysMedicines = medicineDao.countTodaysMedicines(userId);
        HealthRecord healthRecord = healthRecordDao.getByUserId(userId);
        boolean healthRecordComplete = healthRecord != null;

        request.setAttribute("totalMedicines", totalMedicines);
        request.setAttribute("todaysMedicines", todaysMedicines);
        request.setAttribute("healthRecordComplete", healthRecordComplete);

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
