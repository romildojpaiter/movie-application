CREATE TABLE IF NOT EXISTS MOVIE (
    id BIGINT NOT NULL AUTO_INCREMENT,
    movie_year INT NOT NULL,
    title VARCHAR(255),
    studios VARCHAR(255),
    producers VARCHAR(255),
    winner boolean,
    PRIMARY KEY (id)
);