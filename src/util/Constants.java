package util;

/**
 * Holds application-wide constant values such as session attribute
 * names and shared configuration keys, so that they are not
 * duplicated (as magic strings) across the codebase.
 */
public class Constants {

    private Constants() {
        // Prevent instantiation of utility class
    }

    /** Session attribute key used to store the logged-in user's id. */
    public static final String SESSION_USER_ID = "userId";

    /** Session attribute key used to store the logged-in user's name. */
    public static final String SESSION_USER_NAME = "userName";

    /** Session attribute key used to store the logged-in user's email. */
    public static final String SESSION_USER_EMAIL = "userEmail";

    /** Path (relative to context root) users are redirected to after login. */
    public static final String DASHBOARD_PAGE = "dashboard";

    /** Path (relative to context root) users are redirected to when not logged in. */
    public static final String LOGIN_PAGE = "login.jsp";
}
