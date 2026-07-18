package service;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Client for Supabase's built-in Authentication service (GoTrue).
 * <p>
 * User accounts, passwords, and sign-in are managed entirely by
 * Supabase Auth over its REST API &mdash; this application never
 * stores or hashes a password itself. {@code SUPABASE_URL} and
 * {@code SUPABASE_ANON_KEY} (from the Supabase project's API
 * settings) must be set as environment variables.
 */
public class SupabaseAuthService {

    private final HttpClient httpClient;
    private final String supabaseUrl;
    private final String anonKey;

    public SupabaseAuthService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.supabaseUrl = trimTrailingSlash(System.getenv("SUPABASE_URL"));
        this.anonKey = System.getenv("SUPABASE_ANON_KEY");
    }

    /**
     * Checks whether Supabase Auth credentials are configured.
     *
     * @return true if both {@code SUPABASE_URL} and {@code SUPABASE_ANON_KEY} are set
     */
    public boolean isConfigured() {
        return supabaseUrl != null && !supabaseUrl.isBlank()
                && anonKey != null && !anonKey.isBlank();
    }

    /**
     * Registers a new user with Supabase Auth. The user's name and
     * phone are stored as Supabase user metadata (in addition to the
     * app's own {@code profiles} table).
     *
     * @param email    the new user's email address
     * @param password the new user's chosen password
     * @param name     the new user's full name
     * @param phone    the new user's phone number
     * @return the sign-up result (Supabase user id and whether email confirmation is required)
     * @throws Exception if Supabase Auth is not configured, the email is
     *                    already registered, or the request otherwise fails
     */
    public SignUpResult signUp(String email, String password, String name, String phone) throws Exception {
        requireConfigured();

        JSONObject metadata = new JSONObject().put("name", name).put("phone", phone);
        JSONObject body = new JSONObject()
                .put("email", email)
                .put("password", password)
                .put("data", metadata);

        HttpResponse<String> response = post("/auth/v1/signup", body);
        JSONObject json = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            throw new RuntimeException(extractErrorMessage(json));
        }

        // When "Confirm email" is off, Supabase creates a session immediately
        // and nests the user object under "user"; otherwise the user object
        // itself is the top-level response.
        boolean emailConfirmationRequired = !json.has("access_token");
        JSONObject user = emailConfirmationRequired ? json : json.getJSONObject("user");

        // Supabase returns a 200 with an empty "identities" array when the
        // email is already registered (an anti-enumeration measure), rather
        // than a clear error.
        if (user.has("identities") && user.getJSONArray("identities").isEmpty()) {
            throw new RuntimeException("This email is already registered. Please login instead.");
        }

        String userId = user.getString("id");

        return new SignUpResult(userId, emailConfirmationRequired);
    }

    /**
     * Signs a user in with Supabase Auth using email and password.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return the signed-in user's id, email, and name (from user metadata)
     * @throws Exception if Supabase Auth is not configured or the
     *                    credentials are invalid
     */
    public SignInResult signIn(String email, String password) throws Exception {
        requireConfigured();

        JSONObject body = new JSONObject().put("email", email).put("password", password);

        HttpResponse<String> response = post("/auth/v1/token?grant_type=password", body);
        JSONObject json = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            throw new RuntimeException(extractErrorMessage(json));
        }

        JSONObject user = json.getJSONObject("user");
        JSONObject metadata = user.optJSONObject("user_metadata");
        String name = metadata != null ? metadata.optString("name", user.getString("email")) : user.getString("email");

        return new SignInResult(user.getString("id"), user.getString("email"), name);
    }

    /**
     * Sends a one-time signup code to the given email via Supabase Auth.
     * Creates an (unconfirmed) user if one doesn't already exist, with
     * the given name/phone stored as user metadata.
     *
     * @throws Exception if Supabase Auth is not configured or the request fails
     */
    public void sendSignupOtp(String email, String name, String phone) throws Exception {
        requireConfigured();

        JSONObject metadata = new JSONObject().put("name", name).put("phone", phone);
        JSONObject body = new JSONObject()
                .put("email", email)
                .put("create_user", true)
                .put("data", metadata);

        HttpResponse<String> response = post("/auth/v1/otp", body);

        if (response.statusCode() != 200 && response.statusCode() != 204) {
            JSONObject json = new JSONObject(response.body().isBlank() ? "{}" : response.body());
            throw new RuntimeException(extractErrorMessage(json));
        }
    }

    /**
     * Verifies a one-time code sent by {@link #sendSignupOtp}. On success,
     * the user's email is confirmed and a session is created (the account
     * still has no password at this point).
     *
     * @return the verified user's id and a short-lived access token, used
     *         to set the account's password next via {@link #setPassword}
     * @throws Exception if the code is invalid, expired, or the request otherwise fails
     */
    public VerifyOtpResult verifySignupOtp(String email, String otp) throws Exception {
        requireConfigured();

        JSONObject body = new JSONObject()
                .put("email", email)
                .put("token", otp)
                .put("type", "email");

        HttpResponse<String> response = post("/auth/v1/verify", body);
        JSONObject json = new JSONObject(response.body());

        if (response.statusCode() != 200) {
            throw new RuntimeException(extractErrorMessage(json));
        }

        String accessToken = json.getString("access_token");
        JSONObject user = json.getJSONObject("user");

        return new VerifyOtpResult(user.getString("id"), accessToken);
    }

    /**
     * Sets the password for the account identified by the given access
     * token (obtained from {@link #verifySignupOtp}).
     *
     * @throws Exception if the access token is invalid/expired or the request fails
     */
    public void setPassword(String accessToken, String password) throws Exception {
        requireConfigured();

        JSONObject body = new JSONObject().put("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(supabaseUrl + "/auth/v1/user"))
                .timeout(Duration.ofSeconds(15))
                .header("apikey", anonKey)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .method("PUT", HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            JSONObject json = new JSONObject(response.body());
            throw new RuntimeException(extractErrorMessage(json));
        }
    }

    private HttpResponse<String> post(String path, JSONObject body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(supabaseUrl + path))
                .timeout(Duration.ofSeconds(15))
                .header("apikey", anonKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String extractErrorMessage(JSONObject json) {
        if (json.has("msg")) {
            return json.getString("msg");
        }
        if (json.has("error_description")) {
            return json.getString("error_description");
        }
        if (json.has("error")) {
            return json.getString("error");
        }
        return "Authentication request failed.";
    }

    private void requireConfigured() {
        if (!isConfigured()) {
            throw new IllegalStateException(
                    "SUPABASE_URL and SUPABASE_ANON_KEY environment variables must be set.");
        }
    }

    private static String trimTrailingSlash(String value) {
        if (value == null) {
            return null;
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    /** Result of a successful {@link #signUp} call. */
    public static final class SignUpResult {
        public final String userId;
        public final boolean emailConfirmationRequired;

        public SignUpResult(String userId, boolean emailConfirmationRequired) {
            this.userId = userId;
            this.emailConfirmationRequired = emailConfirmationRequired;
        }
    }

    /** Result of a successful {@link #verifySignupOtp} call. */
    public static final class VerifyOtpResult {
        public final String userId;
        public final String accessToken;

        public VerifyOtpResult(String userId, String accessToken) {
            this.userId = userId;
            this.accessToken = accessToken;
        }
    }

    /** Result of a successful {@link #signIn} call. */
    public static final class SignInResult {
        public final String userId;
        public final String email;
        public final String name;

        public SignInResult(String userId, String email, String name) {
            this.userId = userId;
            this.email = email;
            this.name = name;
        }
    }
}
