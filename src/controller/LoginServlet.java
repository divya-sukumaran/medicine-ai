package controller;

import service.SupabaseAuthService;
import util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles user login, logout, and session management.
 * Mapped to both "/login" and "/logout" (see web.xml).
 * Credentials are verified by Supabase Auth, not by this application.
 */
public class LoginServlet extends HttpServlet {

    private final SupabaseAuthService supabaseAuthService = new SupabaseAuthService();

    /**
     * Validates login credentials against Supabase Auth and starts a
     * new session for the user.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (!supabaseAuthService.isConfigured()) {
            request.setAttribute("error",
                    "Login is not available right now (authentication service is not configured).");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        try {
            SupabaseAuthService.SignInResult result = supabaseAuthService.signIn(email.trim(), password);

            HttpSession session = request.getSession(true);
            session.setAttribute(Constants.SESSION_USER_ID, result.userId);
            session.setAttribute(Constants.SESSION_USER_NAME, result.name);
            session.setAttribute(Constants.SESSION_USER_EMAIL, result.email);

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    /**
     * Handles GET requests: shows the login page, or logs the user out
     * when the request path is "/logout".
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
