-- ============================================================
-- AI Health Assistant - Database Schema (PostgreSQL / Supabase)
-- Smart Medicine Reminder and Personal Health Record Management
--
-- Supabase: paste this whole file into the SQL Editor and run it.
-- Local Docker: it runs automatically on first container start.
-- ============================================================

DROP TABLE IF EXISTS health_records CASCADE;
DROP TABLE IF EXISTS medicines CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ------------------------------------------------------------
-- Table: users
-- Stores registered user account details
-- ------------------------------------------------------------
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- Table: medicines
-- Stores medicine reminders created by users
-- ------------------------------------------------------------
CREATE TABLE medicines (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    medicine_name VARCHAR(100) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    time VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medicines_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- Table: health_records
-- Stores the personal health record of each user
-- ------------------------------------------------------------
CREATE TABLE health_records (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    blood_group VARCHAR(10),
    height VARCHAR(20),
    weight VARCHAR(20),
    allergies VARCHAR(255),
    medical_history TEXT,
    emergency_contact VARCHAR(15),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_health_records_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_health_records_user UNIQUE (user_id)
);
