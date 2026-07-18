package service;

/**
 * Local fallback for the AI Health Assistant chatbot.
 * <p>
 * The real responses come from the Gemini API (see
 * {@link GeminiChatbotService}). This class is only used when the API
 * is unreachable, so instead of pretending to answer it tells the user
 * the assistant is temporarily unavailable.
 */
public class ChatbotService {

    private static final String UNAVAILABLE_MESSAGE =
            "The AI assistant is temporarily unavailable. Please try again in a moment, "
                    + "and consult a healthcare professional for urgent concerns.";

    /**
     * Returns a fallback reply for the user's message.
     *
     * @param userMessage the message typed by the user
     * @return a prompt to type a question if the message is empty,
     *         otherwise the standard "temporarily unavailable" reply
     */
    public String getResponse(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Please type a health-related question so I can help you.";
        }
        return UNAVAILABLE_MESSAGE;
    }
}
