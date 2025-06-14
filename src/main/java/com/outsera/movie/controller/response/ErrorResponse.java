package com.outsera.movie.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;

@Builder
@Schema(description = "Retorna informações de erros em requisições")
public record ErrorResponse(
    @JsonProperty("error")
    @Schema(description = "Tipo de erro que aconteceu", example = "GenericException")
    String errorCode,
    @JsonProperty("error_description")
    @Schema(description = "Detalhes do erro que aconteceu", example = "Ocorreu um erro desconhecido durante o processamento!")
    String errorDescription,
    @JsonProperty("error_fields")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Collection<ErrorField> fields)
{
  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("errorCode", errorCode)
        .append("errorDescription", errorDescription)
        .append("fields", fields)
        .toString();
  }
}