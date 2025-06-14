package com.outsera.movie.domain.mapper;

import com.outsera.movie.controller.request.MovieRequest;
import com.outsera.movie.controller.response.MovieResponse;
import com.outsera.movie.infra.database.entities.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(
  componentModel = "spring",
  unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MovieMapper {

  MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

  @Mapping(source = "movieYear", target = "year")
  MovieResponse toResponse(Movie movie);

  @Mapping(source = "year", target = "movieYear")
  Movie toEntity(MovieRequest movieResponse);
}
