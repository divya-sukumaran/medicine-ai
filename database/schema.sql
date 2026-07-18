-- ============================================================
-- AI Health Assistant - Database Schema (PostgreSQL / Supabase)
-- Smart Medicine Reminder and Personal Health Record Management
--
-- Authentication is handled entirely by Supabase Auth (the built-in
-- auth.users table) - this app never stores or hashes passwords.
-- "profiles" is a small app-side table keyed by the same UUID as the
-- Supabase Auth user, holding the display details (name, phone) the
-- app needs and giving medicines/health_records something to link to.
--
-- Supabase: paste this whole file into the SQL Editor and run it.
-- Local Docker: it runs automatically on first container start.
-- ============================================================

DROP TABLE IF EXISTS health_records CASCADE;
DROP TABLE IF EXISTS medicines CASCADE;
DROP TABLE IF EXISTS profiles CASCADE;

-- ------------------------------------------------------------
-- Table: profiles
-- App-side profile for each Supabase Auth user (id = auth.users.id)
-- ------------------------------------------------------------
CREATE TABLE profiles (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- Table: medicines
-- Stores medicine reminders created by users
-- ------------------------------------------------------------
CREATE TABLE medicines (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    medicine_name VARCHAR(100) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    time VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    notes VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medicines_user FOREIGN KEY (user_id)
        REFERENCES profiles(id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- Table: health_records
-- Stores the personal health record of each user
-- ------------------------------------------------------------
CREATE TABLE health_records (
    id SERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    blood_group VARCHAR(10),
    height VARCHAR(20),
    weight VARCHAR(20),
    allergies VARCHAR(255),
    medical_history TEXT,
    emergency_contact VARCHAR(15),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_health_records_user FOREIGN KEY (user_id)
        REFERENCES profiles(id) ON DELETE CASCADE,
    CONSTRAINT uq_health_records_user UNIQUE (user_id)
);
