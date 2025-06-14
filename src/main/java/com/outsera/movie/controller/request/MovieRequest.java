package com.outsera.movie.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
public record MovieRequest(
  @NotNull Integer year,
  @NotNull String title,
  @NotNull String studios,
  @NotNull String producers,
  @NotNull Boolean winner
) {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

}
