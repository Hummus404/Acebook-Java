CREATE TABLE likes (
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    post_id BIGINT NOT NULL REFERENCES posts(id),
    CONSTRAINT uq_likes_user_post UNIQUE (user_id, post_id)
);