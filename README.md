# AI Health Assistant

### Smart Medicine Reminder and Personal Health Record Management System

A Java web application (Servlets, JSP, JDBC) that helps users track
their daily medicines, maintain a personal health record, and chat
with an AI Health Assistant powered by the Google Gemini API.

---

## 1. Project Description

AI Health Assistant is a web application that allows a registered user
to:

- Manage a list of medicines with dosage, timing, and duration.
- Maintain a personal health record (blood group, height, weight,
  allergies, medical history, emergency contact).
- Chat with an AI Health Assistant (Google Gemini) for quick,
  general health guidance.

The project follows the classic **MVC (Model - View - Controller)**
pattern using Servlets as controllers, JSP as views, and a DAO layer
for all database access.

---

## 2. Features

- Secure signup and login with hashed passwords (SHA-256) and
  session-based authentication with logout support.
- Dashboard summarizing total medicines, today's medicines, and
  health record status.
- Full CRUD (Create, Read, Update, Delete) for medicine reminders.
- Personal health record with summary view and save confirmation.
- AI Health Assistant chatbot powered by the Google Gemini API, with
  suggestion chips and a typing indicator.
- Frontend and backend form validation.
- Modern, responsive UI built with Bootstrap 5, Inter font, and
  Bootstrap Icons.

---

## 3. Technology Stack

| Layer            | Technology              |
|-------------------|--------------------------|
| Frontend          | HTML5, CSS3, JavaScript, Bootstrap 5 |
| Backend           | Java 21, JSP, Java Servlets, JDBC |
| Database          | PostgreSQL (Supabase in production) |
| AI                | Google Gemini API (free tier) |
| Server            | Apache Tomcat 10 (Docker) |
| Hosting           | Render (Docker web service) |
| Build Tool        | Maven |
| Version Control   | Git |

---

## 4. System Requirements (local development)

- Docker Desktop (recommended: runs everything), OR
- JDK 21+, Maven 3.9+, Tomcat 10.x, PostgreSQL 16 for a manual setup

---

## 5. Project Structure

```
medicine-ai/
│
├── README.md
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── render.yaml
├── .env.example
├── .gitignore
│
├── database/
│   └── schema.sql
│
├── src/
│   ├── controller/
│   │   ├── LoginServlet.java
│   │   ├── RegisterServlet.java
│   │   ├── DashboardServlet.java
│   │   ├── MedicineServlet.java
│   │   ├── HealthRecordServlet.java
│   │   └── ChatbotServlet.java
│   │
│   ├── dao/
│   │   ├── UserDao.java
│   │   ├── MedicineDao.java
│   │   └── HealthRecordDao.java
│   │
│   ├── model/
│   │   ├── User.java
│   │   ├── Medicine.java
│   │   └── HealthRecord.java
│   │
│   ├── service/
│   │   ├── ChatbotService.java        (offline fallback)
│   │   └── GeminiChatbotService.java  (Gemini API client)
│   │
│   └── util/
│       ├── DBConnection.java
│       ├── Constants.java
│       └── PasswordUtil.java
│
└── WebContent/
    ├── css/style.css
    ├── js/validation.js, chatbot.js
    ├── images/
    ├── includes/head.jsp, navbar.jsp, footer.jsp
    ├── WEB-INF/web.xml
    └── *.jsp (login, register, dashboard, medicines,
               add-medicine, edit-medicine, health-record, chatbot)
```

---

## 6. Environment Variables

All configuration is via environment variables — nothing sensitive is
hard-coded or committed.

| Variable          | Purpose                                 | Local default |
|-------------------|------------------------------------------|---------------|
| `DB_HOST`         | Postgres host                            | `localhost`   |
| `DB_PORT`         | Postgres port                            | `5432`        |
| `DB_NAME`         | Database name                            | `ai_health_assistant` |
| `DB_USER`         | Database user                            | `postgres`    |
| `DB_PASSWORD`     | Database password                        | `postgres`    |
| `DB_SSLMODE`      | `disable` locally, `require` for Supabase| `disable`     |
| `GEMINI_API_KEY`  | Google Gemini API key ([get one free](https://aistudio.google.com/apikey)) | — |

For local Docker, copy `.env.example` to `.env` and fill in
`GEMINI_API_KEY`. The `.env` file is git-ignored.

---

## 7. Running Locally (Docker)

```bash
git clone git@github.com:divya-sukumaran/medicine-ai.git
cd medicine-ai
cp .env.example .env      # then put your GEMINI_API_KEY in .env
docker compose up --build -d
```

Open **http://localhost:8080** — sign up with a new account and start
using the app. The Postgres schema is created automatically on the
first start (from `database/schema.sql`).

To stop: `docker compose down` (add `-v` to also wipe the database).

---

## 8. Deploying to Render + Supabase

### Step 1 — Supabase (database)

1. Create a free project at [supabase.com](https://supabase.com).
2. Open **SQL Editor**, paste the contents of `database/schema.sql`,
   and run it. This creates the `users`, `medicines`, and
   `health_records` tables.
3. Go to **Project Settings → Database → Connection string** and note
   the **Session pooler** credentials (host like
   `aws-0-<region>.pooler.supabase.com`, port `5432`, user like
   `postgres.<project-ref>`).

   > Important: use the **pooler** host, not the direct
   > `db.<ref>.supabase.co` host — the direct host is IPv6-only and
   > Render's free tier cannot reach it.

### Step 2 — Render (application)

1. Push this repo to GitHub (branch `mvp`).
2. In the [Render dashboard](https://dashboard.render.com), choose
   **New → Blueprint** and connect this repository — Render reads
   `render.yaml` and creates the Docker web service.
   (Or: **New → Web Service**, pick the repo, runtime **Docker**.)
3. Fill in the environment variables when prompted:
   - `DB_HOST` = Supabase pooler host
   - `DB_PORT` = `5432`
   - `DB_NAME` = `postgres`
   - `DB_USER` = `postgres.<project-ref>`
   - `DB_PASSWORD` = your Supabase database password
   - `DB_SSLMODE` = `require`
   - `GEMINI_API_KEY` = your Gemini key
4. Deploy. Render builds the Dockerfile and gives you a free public
   domain like **`https://ai-health-assistant.onrender.com`** (you can
   attach a custom domain later in the service settings).

> Note: on Render's free plan the service sleeps after inactivity;
> the first request after a while takes ~30-60 seconds to wake up.

### Authentication

Signup and login are built into the app itself: passwords are stored
as SHA-256 hashes in the `users` table and sessions are managed by
Tomcat. Supabase is used purely as the Postgres database, so no
Supabase Auth configuration is required.

---

## 9. Screenshots

> Add screenshots of the Login, Dashboard, Medicines, Health Record,
> and Chatbot pages here once the application is running.

| Page      | Screenshot |
|-----------|------------|
| Login     | _placeholder_ |
| Dashboard | _placeholder_ |
| Medicines | _placeholder_ |
| Health Record | _placeholder_ |
| AI Assistant  | _placeholder_ |

---

## 10. Future Enhancements

- Email/SMS notifications for medicine reminder times.
- Password reset via email.
- Stronger password hashing (bcrypt/argon2) with per-user salts.
- Export health record as a PDF.
- Conversation history/context for the AI chatbot.
- Admin panel for managing users.

---

## 11. Author

**Divya Sukumaran**

---

## 12. License

This project is developed for academic and educational purposes.
Free to use and modify for learning purposes.
