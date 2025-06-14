package com.outsera.movie.controller;

import com.outsera.movie.controller.request.MovieRequest;
import com.outsera.movie.controller.response.IntervalAwardResponse;
import com.outsera.movie.controller.response.MovieResponse;
import com.outsera.movie.controller.response.ProducerAwardResponse;
import com.outsera.movie.domain.mapper.MovieMapper;
import com.outsera.movie.domain.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.outsera.movie.controller.response.IntervalAwardResponse.MAX;
import static com.outsera.movie.controller.response.IntervalAwardResponse.MIN;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

  private final MovieService movieService;
  private final MovieMapper movieMapper;

  @GetMapping
  @Operation(description = "Lista todos os filmes")
  public Mono<ResponseEntity<List<MovieResponse>>> getMovies() {
    return movieService.getMovies()
      .map(movieMapper::toResponse)
      .collectList()
      .map(ResponseEntity::ok)
      .onErrorResume(e -> {
        log.error("Error fetching all movie", e);
        return Mono.just(ResponseEntity.badRequest().build());
      });
  }

  @GetMapping("/{id}")
  @Operation(description = "Recupera um filme pelo ID")
  public Mono<ResponseEntity<MovieResponse>> getMoviesById(@Validated @PathVariable("id") Long id) {
    return movieService.getMoviesById(id)
      .map(movieMapper::toResponse)
      .map(ResponseEntity::ok)
      .onErrorResume(e -> {
        log.error("Error fetching movie {}", id, e);
        return Mono.just(ResponseEntity.badRequest().build());
      });
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(description = "Deleta um filme pelo ID")
  public Mono<Void> deleteMoviesById(@Validated @PathVariable("id") Long id) {
    return movieService.deleteMoviesById(id);
  }

  @PostMapping(
    consumes = "application/json",
    produces = "application/json"
  )
  @Operation(description = "Cria um novo filme")
  public Mono<ResponseEntity<MovieResponse>> createMovie(@Validated @RequestBody MovieRequest movieRequest) {
    return movieService.createMovie(movieMapper.toEntity(movieRequest))
      .map(movieMapper::toResponse)
      .map(movieResponse -> ResponseEntity.status(HttpStatus.CREATED).body(movieResponse))
      .onErrorResume(e -> {
        log.error("Error creating movie {}", movieRequest, e);
        return Mono.just(ResponseEntity.badRequest().build());
      });
  }

  @PutMapping("/{id}")
  @Operation(description = "Atualiza um filme")
  public Mono<ResponseEntity<MovieResponse>> updateMovie(@Validated @PathVariable("id") Long id,
                                                         @RequestBody MovieRequest movieRequest) {
    return movieService.updateMovie(id, movieMapper.toEntity(movieRequest))
      .map(movieMapper::toResponse)
      .map(ResponseEntity::ok)
      .onErrorResume(e -> {
        log.error("Error updating movie {}", movieRequest, e);
        return Mono.just(ResponseEntity.badRequest().build());
      });
  }

  @GetMapping("/interval")
  public Mono<ResponseEntity<IntervalAwardResponse>> getInterval() {
    return movieService.getIntervalAwards()
      .map(getIntervalAwardResponseFunction())
      .map(ResponseEntity::ok)
      .onErrorResume(e -> {
        log.error("Error fetching interval", e);
        return Mono.just(ResponseEntity.badRequest().build());
      });
  }

  private static Function<Map<String, List<ProducerAwardResponse>>, IntervalAwardResponse> getIntervalAwardResponseFunction() {
    return interval -> new IntervalAwardResponse(interval.get(MIN), interval.get(MAX));
  }

}
