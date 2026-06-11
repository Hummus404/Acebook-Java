CREATE TABLE friendships (
    id  BIGSERIAL PRIMARY KEY,
    requestor_id INT NOT NULL REFERENCES users(id),
    addressee_id INT NOT NULL REFERENCES users(id),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(requestor_id, addressee_id)
);