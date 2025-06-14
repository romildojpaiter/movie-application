package com.outsera.movie.controller.response;

import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

@Builder
public record IntervalAwardResponse(
  List<ProducerAwardResponse> min,
  List<ProducerAwardResponse> max
) {

  public static final String MIN = "min";
  public static final String MAX = "max";

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
