package com.outsera.movie.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Builder
@Schema(description = "Quando a requisição tiver informações inválidas as mesmas serão listadas nessa propriedade")
public record ErrorField(
  @JsonProperty("field")
  @Schema(description = "Nome da proriedade que cotém informações inválidas", example = "id")
  String field,
  @JsonProperty("error_description")
  @Schema(description = "descrição do erro contido no campo", example = "informe um identificador válido")
  String errorDescription)
{
  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
      .append("field", field)
      .append("errorDescription", errorDescription)
      .toString();
  }
}
