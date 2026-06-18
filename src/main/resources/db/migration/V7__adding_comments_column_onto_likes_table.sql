CREATE TABLE commentLikes (
                       id      BIGSERIAL PRIMARY KEY,
                       user_id BIGINT NOT NULL REFERENCES users(id),
                       comment_id BIGINT NOT NULL REFERENCES comments(id),
                       CONSTRAINT uq_likes_user_comment UNIQUE (user_id, comment_id)
);