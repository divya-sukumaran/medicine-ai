/**
 * Handles the AI Health Assistant chat widget on chatbot.jsp.
 * Sends the user's message to ChatbotServlet via fetch (AJAX), shows a
 * typing indicator while waiting, and renders the AI reply as a chat
 * bubble. Suggestion chips below the window send a message on click.
 */
(function () {
    "use strict";

    var chatForm = document.getElementById("chatForm");
    var chatInput = document.getElementById("chatInput");
    var chatWindow = document.getElementById("chatWindow");

    if (!chatForm) {
        return;
    }

    function buildRow(sender) {
        var row = document.createElement("div");
        row.className = "chat-row " + sender;

        var avatar = document.createElement("span");
        avatar.className = "row-avatar";
        avatar.innerHTML = sender === "user"
            ? '<i class="bi bi-person-fill"></i>'
            : '<i class="bi bi-robot"></i>';

        row.appendChild(avatar);
        return row;
    }

    function appendMessage(text, sender) {
        var row = buildRow(sender);

        var bubble = document.createElement("div");
        bubble.className = "chat-message";
        bubble.textContent = text;

        row.appendChild(bubble);
        chatWindow.appendChild(row);
        chatWindow.scrollTop = chatWindow.scrollHeight;
    }

    function showTypingIndicator() {
        var row = buildRow("bot");
        row.id = "typingIndicator";

        var bubble = document.createElement("div");
        bubble.className = "chat-message";
        bubble.innerHTML = '<span class="typing-dots"><span></span><span></span><span></span></span>';

        row.appendChild(bubble);
        chatWindow.appendChild(row);
        chatWindow.scrollTop = chatWindow.scrollHeight;
    }

    function hideTypingIndicator() {
        var indicator = document.getElementById("typingIndicator");
        if (indicator) {
            indicator.remove();
        }
    }

    function sendMessage(message) {
        appendMessage(message, "user");
        showTypingIndicator();

        var contextPath = typeof CONTEXT_PATH !== "undefined" ? CONTEXT_PATH : "";

        fetch(contextPath + "/chatbot-ask", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "message=" + encodeURIComponent(message)
        })
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("Request failed");
                }
                return response.text();
            })
            .then(function (reply) {
                hideTypingIndicator();
                appendMessage(reply, "bot");
            })
            .catch(function () {
                hideTypingIndicator();
                appendMessage("Something went wrong. Please try again.", "bot");
            });
    }

    chatForm.addEventListener("submit", function (event) {
        event.preventDefault();

        var message = chatInput.value.trim();
        if (message === "") {
            return;
        }

        chatInput.value = "";
        sendMessage(message);
    });

    // Suggestion chips: clicking one sends its label as a message
    document.querySelectorAll(".suggestion-chip").forEach(function (chip) {
        chip.addEventListener("click", function () {
            sendMessage(chip.textContent.trim());
        });
    });
})();
