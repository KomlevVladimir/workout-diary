DELETE FROM confirmation_secrets;
DELETE FROM users;
INSERT INTO users (id, first_name, last_name, age, email, password, is_enabled) VALUES
 (25, 'John', 'Doe', 23, 'test@myemail.com', '$2a$10$WpR3CCt8otTarm1xI8OMu.ip/edANp2JEdTWpqyM9uRhSXo5gpB0.', true);