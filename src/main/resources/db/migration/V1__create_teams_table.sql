CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(32) NOT NULL,
    league VARCHAR(255),
    country VARCHAR(255),
    rating INTEGER NOT NULL
);
