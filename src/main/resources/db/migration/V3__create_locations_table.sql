CREATE TABLE locations (
 id SERIAL PRIMARY KEY ,
 name VARCHAR(100) NOT NULL ,
 userId INT NOT NULL REFERENCES users(id),
 latitude DECIMAL(9,6) ,
 longitude DECIMAL(9,6)
);