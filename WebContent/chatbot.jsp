<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>AI Assistant | AI Health Assistant</title>
    <%@ include file="includes/head.jsp" %>
</head>
<body>

<%@ include file="includes/navbar.jsp" %>

<div class="container py-4" style="max-width: 820px;">
    <div class="mb-4">
        <h3 class="page-title"><i class="bi bi-stars me-2" style="color:#2563EB;"></i>AI Health Assistant</h3>
        <p class="page-subtitle mb-0">Ask anything health-related — powered by AI, not a substitute for a doctor.</p>
    </div>

    <div class="card chat-card">
        <div class="chat-header">
            <span class="chat-avatar"><i class="bi bi-robot"></i></span>
            <div>
                <div class="fw-bold">Health Assistant</div>
                <span class="chat-status">Online</span>
            </div>
        </div>

        <div id="chatWindow" class="chat-window">
            <div class="chat-row bot">
                <span class="row-avatar"><i class="bi bi-robot"></i></span>
                <div class="chat-message">
                    Hello! I am your AI Health Assistant. Ask me about symptoms, healthy habits, or general health questions.
                </div>
            </div>
        </div>

        <div class="chat-suggestions">
            <button type="button" class="suggestion-chip">I have a headache</button>
            <button type="button" class="suggestion-chip">Tips to sleep better</button>
            <button type="button" class="suggestion-chip">What is a balanced diet?</button>
            <button type="button" class="suggestion-chip">How much water should I drink?</button>
        </div>

        <form id="chatForm" class="chat-input-bar">
            <input type="text" id="chatInput" class="form-control" placeholder="Type your question..." autocomplete="off" required>
            <button type="submit" class="btn-send" title="Send"><i class="bi bi-send-fill"></i></button>
        </form>
    </div>
</div>

<%@ include file="includes/footer.jsp" %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const CONTEXT_PATH = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/chatbot.js"></script>
</body>
</html>
