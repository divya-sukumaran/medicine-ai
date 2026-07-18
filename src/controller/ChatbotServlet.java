package controller;

import service.ChatbotService;
import service.GeminiChatbotService;
import util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Receives chat messages from the AI Health Assistant chat widget
 * (via AJAX) and returns a health tip as plain text.
 * <p>
 * Tries the Gemini API first (real generative AI) and automatically
 * falls back to the local rule-based {@link ChatbotService} if no API
 * key is configured, the network is unavailable, or the free-tier
 * quota is exceeded. This keeps the chatbot working even without
 * internet access, which is useful for an offline viva demo.
 */
public class ChatbotServlet extends HttpServlet {

    private final ChatbotService ruleBasedChatbotService = new ChatbotService();
    private final GeminiChatbotService geminiChatbotService = new GeminiChatbotService();

    /**
     * Reads the user's message, asks Gemini for a response, and falls
     * back to the rule-based knowledge base on any failure.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constants.SESSION_USER_ID) == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login first.");
            return;
        }

        String message = request.getParameter("message");
        String reply = getReply(message);

        response.setContentType("text/plain");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        try (PrintWriter out = response.getWriter()) {
            out.print(reply);
        }
    }

    /**
     * Attempts to get a reply from the Gemini API. If that fails for
     * any reason, uses the local rule-based knowledge base instead.
     */
    private String getReply(String message) {
        if (geminiChatbotService.isConfigured()) {
            try {
                return geminiChatbotService.getResponse(message);
            } catch (Exception e) {
                getServletContext().log("Gemini API call failed, falling back to rule-based chatbot", e);
            }
        }
        return ruleBasedChatbotService.getResponse(message);
    }
}
