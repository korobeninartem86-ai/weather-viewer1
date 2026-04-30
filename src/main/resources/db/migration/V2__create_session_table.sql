CREATE TABLE sessions (
    session_id  UUID PRIMARY KEY ,
    user_id INT NOT NULL ,
    expires_at TIMESTAMP NOT NULL ,
    FOREIGN KEY (user_id)REFERENCES users(id)
);