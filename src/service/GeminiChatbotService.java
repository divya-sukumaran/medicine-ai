package service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Calls the Google Gemini free-tier API (generateContent) to answer
 * health-related questions with real generative AI.
 * <p>
 * The API key is read from the {@code GEMINI_API_KEY} environment
 * variable so it is never hard-coded or committed to source control.
 * If the key is missing or the API call fails for any reason, callers
 * should catch the exception and fall back to {@link ChatbotService}.
 */
public class GeminiChatbotService {

    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";

    private static final String SYSTEM_INSTRUCTION =
            "You are a helpful AI Health Assistant inside a medicine reminder app. "
                    + "Answer the user's health question in 2 to 3 short, simple sentences. "
                    + "Always remind the user to consult a doctor for serious or persistent symptoms. "
                    + "Do not provide a diagnosis.";

    private final HttpClient httpClient;
    private final String apiKey;

    public GeminiChatbotService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.apiKey = System.getenv("GEMINI_API_KEY");
    }

    /**
     * Checks whether a Gemini API key is available.
     *
     * @return true if {@code GEMINI_API_KEY} is set
     */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    /**
     * Sends the user's message to the Gemini API and returns the
     * generated reply.
     *
     * @param userMessage the message typed by the user
     * @return the AI-generated response text
     * @throws Exception if the key is missing, the request fails, or the
     *                    response cannot be parsed (callers should fall
     *                    back to the rule-based {@link ChatbotService})
     */
    public String getResponse(String userMessage) throws Exception {
        if (!isConfigured()) {
            throw new IllegalStateException("GEMINI_API_KEY environment variable is not set.");
        }

        JSONObject requestBody = buildRequestBody(userMessage);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?key=" + apiKey))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Gemini API error: HTTP " + response.statusCode() + " - " + response.body());
        }

        return extractReplyText(response.body());
    }

    private JSONObject buildRequestBody(String userMessage) {
        JSONObject userPart = new JSONObject().put("text", userMessage);
        JSONObject userContent = new JSONObject()
                .put("role", "user")
                .put("parts", new JSONArray().put(userPart));

        JSONObject systemPart = new JSONObject().put("text", SYSTEM_INSTRUCTION);
        JSONObject systemInstruction = new JSONObject()
                .put("parts", new JSONArray().put(systemPart));

        return new JSONObject()
                .put("contents", new JSONArray().put(userContent))
                .put("systemInstruction", systemInstruction);
    }

    private String extractReplyText(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        return json.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .trim();
    }
}
