package controller;

import dao.UserDao;
import model.User;
import util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles new user registration requests.
 */
public class RegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    /**
     * Validates the registration form input, creates a new user
     * account with a hashed password, and redirects to the login page.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        String error = validate(name, email, phone, password);

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (userDao.isEmailTaken(email)) {
            request.setAttribute("error", "This email is already registered. Please login instead.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPhone(phone.trim());
        user.setPassword(PasswordUtil.hash(password));

        boolean success = userDao.registerUser(user);

        if (success) {
            request.setAttribute("success", "Registration successful! Please login to continue.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
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

    /**
     * Performs basic server-side validation of the registration form fields.
     *
     * @return an error message if validation fails, or null if the input is valid
     */
    private String validate(String name, String email, String phone, String password) {
        if (isBlank(name) || isBlank(email) || isBlank(phone) || isBlank(password)) {
            return "All fields are required.";
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            return "Please enter a valid email address.";
        }
        if (!phone.matches("^[0-9]{10}$")) {
            return "Please enter a valid 10-digit phone number.";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
