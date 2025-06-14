package com.outsera.movie.infra.database.converters;

import com.outsera.movie.infra.database.entities.Movie;
import io.r2dbc.spi.Row;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@Slf4j
@ReadingConverter
public class MovieEntityConverter implements Converter<Row, Movie> {

  @Override
  public Movie convert(Row source) {
    return Movie.builder()
      .id(source.get("id", Long.class))
      .movieYear(source.get("movie_year", Integer.class))
      .title(source.get("title", String.class))
      .producers(source.get("producers", String.class))
      .studios(source.get("studios", String.class))
      .winner(Boolean.TRUE.equals(source.get("winner", Boolean.class)))
      .build();
  }
}
