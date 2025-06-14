package com.outsera.movie.controller.response;

import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
public record MovieResponse(
  Long id,
  Integer year,
  String title,
  String studios,
  String producers,
  boolean winner
) {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
