package com.outsera.movie.domain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieServiceTest {

  @Autowired
  MovieService movieService;

  @Test
  void getIntervalAwards() {

    StepVerifier.create(movieService.getIntervalAwards())
      .assertNext(result -> {
        var min = result.get("min");
        var max = result.get("max");

        assertThat(min).hasSize(1);
        assertThat(max).hasSize(1);

        var itemMin = min.getFirst();
        assertThat(itemMin.producer()).isEqualTo("Joel Silver");
        assertThat(itemMin.interval()).isEqualTo(1);
        assertThat(itemMin.previousWin()).isEqualTo(1990);
        assertThat(itemMin.followingWin()).isEqualTo(1991);

        var itemMax = max.getFirst();
        assertThat(itemMax.producer()).isEqualTo("Matthew Vaughn");
        assertThat(itemMax.interval()).isEqualTo(22);
        assertThat(itemMax.previousWin()).isEqualTo(2015);
        assertThat(itemMax.followingWin()).isEqualTo(2037);

      })
      .verifyComplete();
  }

}