package controller;

import dao.UserDao;
import model.User;
import util.Constants;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles user login, logout, and session management.
 * Mapped to both "/login" and "/logout" (see web.xml).
 */
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    /**
     * Validates login credentials and starts a new session for the user.
     * If the request URL is "/logout", the current session is invalidated instead.
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

        String hashedPassword = PasswordUtil.hash(password);
        User user = userDao.validateLogin(email.trim(), hashedPassword);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute(Constants.SESSION_USER_ID, user.getId());
            session.setAttribute(Constants.SESSION_USER_NAME, user.getName());
            session.setAttribute(Constants.SESSION_USER_EMAIL, user.getEmail());

            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            request.setAttribute("error", "Invalid email or password.");
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
