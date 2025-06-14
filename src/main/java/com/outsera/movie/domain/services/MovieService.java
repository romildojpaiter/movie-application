package com.outsera.movie.domain.services;

import com.google.common.io.Files;
import com.outsera.movie.controller.response.ProducerAwardResponse;
import com.outsera.movie.domain.exception.NotFoundException;
import com.outsera.movie.infra.database.entities.Movie;
import com.outsera.movie.infra.database.repository.MovieRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.outsera.movie.controller.response.IntervalAwardResponse.MAX;
import static com.outsera.movie.controller.response.IntervalAwardResponse.MIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;

  public Flux<Movie> getMovies() {
    return movieRepository.findAll();
  }

  public Mono<Movie> getMoviesById(Long id) {
    return movieRepository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException("Movie not found with id: " + id)));
  }

  public Mono<Void> deleteMoviesById(Long id) {
    return movieRepository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException("Not is possible delete this item: " + id)))
      .flatMap(movie -> movieRepository.deleteById(movie.getId()));
  }

  public Mono<Movie> createMovie(Movie movieEntity) {
    return movieRepository.save(movieEntity);
  }

  public Mono<Movie> updateMovie(@NotNull Long id,
                                 @NotNull Movie movieEntity) {
    return movieRepository.findById(id)
      .switchIfEmpty(Mono.error(new NotFoundException("Not is possible update this movie: " + id)))
      .map(movie -> movie.of(
        movieEntity.getTitle(),
        movieEntity.getStudios(),
        movieEntity.getProducers(),
        movieEntity.getMovieYear(),
        movieEntity.isWinner()))
      .flatMap(movieRepository::save);
  }

  public Mono<Map<String, List<ProducerAwardResponse>>> getIntervalAwards() {
    return movieRepository.findAll(Example.of(Movie.builder().winner(true).build()))
      .collectList()
      .map(movie -> {
        log.info("Found {} movies with winner status", movie.size());
        Map<String, List<Integer>> producerWinners = extractProducersWinners(movie);
        List<ProducerAwardResponse> producerAwardsTwoOrMore = getProducerMultipleWinners(producerWinners);

        int minInterval = producerAwardsTwoOrMore.stream().mapToInt(ProducerAwardResponse::interval).min().orElse(0);
        int maxInterval = producerAwardsTwoOrMore.stream().mapToInt(ProducerAwardResponse::interval).max().orElse(0);

        return Map.of(
          MIN, producerAwardsTwoOrMore.stream()
            .filter(i -> i.interval() == minInterval)
            .toList(),
          MAX, producerAwardsTwoOrMore.stream()
            .filter(i -> i.interval() == maxInterval)
            .toList()
        );

      });

  }

  private static List<ProducerAwardResponse> getProducerMultipleWinners(Map<String, List<Integer>> producerWinners) {
    List<ProducerAwardResponse> producerAwardsTwoOrMore = new ArrayList<>();
    producerWinners.forEach((producer, years) -> {

      if (years.size() < 2) {
        return;
      }

      int minYear = years.stream().min(Integer::compareTo).orElse(0);
      int maxYear = years.stream().max(Integer::compareTo).orElse(0);

      int interval = maxYear - minYear;

      producerAwardsTwoOrMore.add(ProducerAwardResponse.builder()
        .producer(producer)
        .interval(interval)
        .previousWin(minYear)
        .followingWin(maxYear)
        .build()
      );
    });
    log.info("Producers with two or more winners: {}", producerAwardsTwoOrMore);
    return producerAwardsTwoOrMore;
  }

  private static Map<String, List<Integer>> extractProducersWinners(List<Movie> movie) {
    Map<String, List<Integer>> producerWinners = new HashMap<>();
    movie.forEach(m -> Arrays.stream(m.getProducers().split(", | and "))
      .forEach(producer -> producerWinners.computeIfAbsent(producer, k -> new ArrayList<>())
        .add(m.getMovieYear()))
    );
    log.info("Producers with winners: {}", producerWinners);
    return producerWinners;
  }


}
