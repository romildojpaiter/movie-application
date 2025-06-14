package com.outsera.movie.infra.database.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Optional;

@Data
@Table("MOVIE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column("movie_year")
  private Integer movieYear;
  private String title;
  private String studios;
  private String producers;
  private boolean winner;

  public Movie of(String title, String studios, String producers, Integer movieYear, Boolean winner) {
    return Movie.builder()
      .id(id)
      .title(Optional.ofNullable(title).orElse(this.title))
      .studios(Optional.ofNullable(studios).orElse(this.studios))
      .producers(Optional.ofNullable(producers).orElse(this.producers))
      .movieYear(Optional.ofNullable(movieYear).orElse(this.movieYear))
      .winner(Optional.ofNullable(winner).orElse(this.winner))
      .build();
  }

}
