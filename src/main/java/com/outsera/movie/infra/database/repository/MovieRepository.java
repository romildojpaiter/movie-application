package com.outsera.movie.infra.database.repository;

import com.outsera.movie.infra.database.entities.Movie;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends R2dbcRepository<Movie, Long> {
}
