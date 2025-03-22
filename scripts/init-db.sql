-- Enable pgcrypto for encryption
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Create tables
CREATE TABLE IF NOT EXISTS employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    department VARCHAR(255) NOT NULL,
    salary VARCHAR(255) NOT NULL
);

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Preload roles
INSERT INTO roles (name) VALUES
  ('ROLE_HR_STAFF'),
  ('ROLE_ADMIN'),
  ('ROLE_EMPLOYEE')
ON CONFLICT (name) DO NOTHING;