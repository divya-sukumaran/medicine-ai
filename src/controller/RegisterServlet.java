package controller;

import dao.ProfileDao;
import model.UserProfile;
import service.SupabaseAuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Drives the three-step OTP signup flow. Account creation and password
 * storage are delegated entirely to Supabase Auth; this servlet only
 * validates input and orchestrates the steps, holding in-progress
 * registration state in the session between them:
 * <ol>
 *   <li>Name/email/phone &rarr; Supabase emails a one-time code.</li>
 *   <li>The user enters that code to verify their email.</li>
 *   <li>The user sets a password (the only field asked at this step),
 *       and the app-side profile row is created.</li>
 * </ol>
 */
public class RegisterServlet extends HttpServlet {

    private static final String SESSION_REG_NAME = "reg_name";
    private static final String SESSION_REG_EMAIL = "reg_email";
    private static final String SESSION_REG_PHONE = "reg_phone";
    private static final String SESSION_REG_USER_ID = "reg_user_id";
    private static final String SESSION_REG_ACCESS_TOKEN = "reg_access_token";

    private final SupabaseAuthService supabaseAuthService = new SupabaseAuthService();
    private final ProfileDao profileDao = new ProfileDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String step = request.getParameter("step");
        if (step == null) {
            step = "details";
        }

        switch (step) {
            case "otp":
                handleVerifyOtp(request, response);
                break;
            case "password":
                handleSetPassword(request, response);
                break;
            default:
                handleSendOtp(request, response);
        }
    }

    /** Step 1: validate name/email/phone and send the signup OTP. */
    private void handleSendOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        String error = validateDetails(name, email, phone);
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!supabaseAuthService.isConfigured()) {
            request.setAttribute("error",
                    "Sign-up is not available right now (authentication service is not configured).");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try {
            supabaseAuthService.sendSignupOtp(email.trim(), name.trim(), phone.trim());

            HttpSession session = request.getSession();
            session.setAttribute(SESSION_REG_NAME, name.trim());
            session.setAttribute(SESSION_REG_EMAIL, email.trim());
            session.setAttribute(SESSION_REG_PHONE, phone.trim());

            request.setAttribute("email", email.trim());
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    /** Step 2: verify the code the user received by email. */
    private void handleVerifyOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String email = session != null ? (String) session.getAttribute(SESSION_REG_EMAIL) : null;

        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/register.jsp");
            return;
        }

        String otp = request.getParameter("otp");
        if (otp == null || otp.trim().isEmpty()) {
            request.setAttribute("error", "Please enter the code we emailed you.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
            return;
        }

        try {
            SupabaseAuthService.VerifyOtpResult result =
                    supabaseAuthService.verifySignupOtp(email, otp.trim());

            session.setAttribute(SESSION_REG_USER_ID, result.userId);
            session.setAttribute(SESSION_REG_ACCESS_TOKEN, result.accessToken);

            request.getRequestDispatcher("set-password.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("email", email);
            request.getRequestDispatcher("verify-otp.jsp").forward(request, response);
        }
    }

    /** Step 3: set the account password and create the app-side profile. */
    private void handleSetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String accessToken = session != null ? (String) session.getAttribute(SESSION_REG_ACCESS_TOKEN) : null;
        String userId = session != null ? (String) session.getAttribute(SESSION_REG_USER_ID) : null;
        String name = session != null ? (String) session.getAttribute(SESSION_REG_NAME) : null;
        String email = session != null ? (String) session.getAttribute(SESSION_REG_EMAIL) : null;
        String phone = session != null ? (String) session.getAttribute(SESSION_REG_PHONE) : null;

        if (accessToken == null || userId == null) {
            response.sendRedirect(request.getContextPath() + "/register.jsp");
            return;
        }

        String password = request.getParameter("password");
        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long.");
            request.getRequestDispatcher("set-password.jsp").forward(request, response);
            return;
        }

        try {
            supabaseAuthService.setPassword(accessToken, password);
            profileDao.createProfile(new UserProfile(userId, name, email, phone));

            session.removeAttribute(SESSION_REG_NAME);
            session.removeAttribute(SESSION_REG_EMAIL);
            session.removeAttribute(SESSION_REG_PHONE);
            session.removeAttribute(SESSION_REG_USER_ID);
            session.removeAttribute(SESSION_REG_ACCESS_TOKEN);

            request.setAttribute("success", "Registration successful! Please login to continue.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("set-password.jsp").forward(request, response);
        }
    }

    /**
     * Redirects direct GET requests to the registration page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    private String validateDetails(String name, String email, String phone) {
        if (isBlank(name) || isBlank(email) || isBlank(phone)) {
            return "All fields are required.";
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            return "Please enter a valid email address.";
        }
        if (!phone.matches("^[0-9]{10}$")) {
            return "Please enter a valid 10-digit phone number.";
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
