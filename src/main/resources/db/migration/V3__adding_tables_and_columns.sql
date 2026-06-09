DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS comments;

CREATE TABLE posts (
id BIGSERIAL PRIMARY KEY,
content TEXT,
poster INTEGER,
image TEXT,
likes INTEGER,
date_of_post TIMESTAMP
);

INSERT INTO posts (content , poster, image, likes, date_of_post) VALUES
('Woz I here? Nobody knows.', 1, 'alt text', 0,  NOW()),
('Burritos for lunch. Wahoo.', 2, 'alt text', 0,  NOW());

CREATE TABLE comments (
id BIGSERIAL PRIMARY KEY,
content TEXT,
poster INTEGER,
post INTEGER,
likes INTEGER,
date_of_comment TIMESTAMP
);

INSERT INTO comments (content, poster, post, likes, date_of_comment) VALUES ('Burritos are bad.', 1, 2,0, NOW());

CREATE TABLE users (
id BIGSERIAL PRIMARY KEY,
username VARCHAR(100),
profile_picture TEXT,
first_name VARCHAR(100),
surname VARCHAR(100),
email_address VARCHAR(100),
friends TEXT,
blocked TEXT,
enabled boolean NOT NULL
);

INSERT INTO users (username, profile_picture, first_name, surname, email_address, friends, blocked, enabled) VALUES
                                                                                                                 ('adrian_woz_here', 'alt_text', 'Adrian', 'Swaine', 'adrianjswaine@gmail.com','2,3','', TRUE),
                                                                                                                 ('adrian_woz_not_here', 'alt_text', 'NotAdrian', 'NotSwaine', 'notadrianjswaine@gmail.com','1','3', TRUE);