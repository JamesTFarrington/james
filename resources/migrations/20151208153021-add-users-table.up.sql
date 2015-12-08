CREATE TABLE users
(id VARCHAR(20) PRIMARY KEY,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 email VARCHAR(30),
 admin BOOLEAN,
 last_login TIME,
 is_active BOOLEAN,
 pass VARCHAR(100));

CREATE TABLE recipes
(id INTEGER PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(30),
 ingredients VARCHAR(200),
 description VARCHAR(200),
 timestamp TIMESTAMP);
