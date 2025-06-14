package com.outsera.movie.core;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.outsera.movie.infra.database.entities.Movie;
import com.outsera.movie.infra.database.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class MoviesCsvInitializer implements ApplicationRunner {

  private static final String MOVIES_LIST_CSV = "movielist.csv";
  private static final String YES = "YES";

  private final MovieRepository movieRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    movieRepository.count()
      .flatMap(count -> {
        if (count == 0) {
          log.info("Loading data from CSV...");
          return loadMoviesFromCsv();
        } else {
          log.info("Table already contains data, skipping CSV load. {}", count);
          return Mono.empty();
        }
      })
      .subscribe();
  }

  private Mono<Void> loadMoviesFromCsv() {
    return Mono.fromCallable(() -> {
        ClassPathResource resource = new ClassPathResource(MOVIES_LIST_CSV);
        Reader reader = new InputStreamReader(resource.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(reader)
          .withSkipLines(1)
          .withCSVParser(new com.opencsv.CSVParserBuilder()
            .withSeparator(';')
            .withQuoteChar('"')
            .build())
          .build();
        return csvReader.readAll();
      })
      .flatMapMany(Flux::fromIterable)
      .flatMap(this::processCsvLine)
      .collectList()
      .flatMap(movies -> movieRepository.saveAll(movies)
        .doOnComplete(() -> log.info("[MoviesCsvInitializer] Movies loaded successfully. Total: {}", movies.size()))
        .then())
      .then();
  }

  private Mono<Movie> processCsvLine(String[] line) {
    try {
      int year = Integer.parseInt(line[0]);
      String title = line[1];
      String studios = line[2];
      String producers = line[3];
      String winner = line[4];

      Movie movie = Movie.builder()
        .movieYear(year)
        .title(title)
        .studios(studios)
        .producers(producers)
        .winner(Objects.equals(winner.toUpperCase(), YES))
        .build();

      return Mono.just(movie);
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      log.error("[MoviesCsvInitializer] Error on process CSV: " + String.join(",", line) + " - " + e.getMessage());
      return Mono.empty();
    }
  }

}
