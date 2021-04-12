CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    created_date DATE,
    description TEXT,
    color VARCHAR(50)
);