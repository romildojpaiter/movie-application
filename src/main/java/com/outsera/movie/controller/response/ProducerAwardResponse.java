package com.outsera.movie.controller.response;

import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
public record ProducerAwardResponse(
  String producer,
  Integer interval,
  Integer previousWin,
  Integer followingWin
) {
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
