package com.outsera.movie.utils;


import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@UtilityClass
public class JsonUtils {

  public static JsonUtilsBuilder builder() {
    return new JsonUtilsBuilder();
  }

  public static class JsonUtilsBuilder {

    private final ToStringBuilder toStringBuilder;

    public JsonUtilsBuilder() {
      toStringBuilder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
    }

    public JsonUtilsBuilder addParam(String key, Object value) {
      this.toStringBuilder.append(key, value);
      return this;
    }

    public JsonUtilsBuilder addOrderId(Object value) {
      return addParam("orderId", value);
    }

    public JsonUtilsBuilder addHttpCode(Object value) {
      return addParam("httpCode", value);
    }

    public JsonUtilsBuilder addError(Object value) {
      return addParam("error", value);
    }

    public JsonUtilsBuilder addMethodValue(Object value) {
      return addParam("methodValue", value);
    }

    public JsonUtilsBuilder addPath(Object value) {
      return addParam("path", value);
    }

    public JsonUtilsBuilder addCustomerId(Object value) {
      return addParam("customerID", value);
    }

    public JsonUtilsBuilder addType(Object value) {
      return addParam("type", value);
    }

    public JsonUtilsBuilder addProjectId(Object value) {
      return addParam("project_id", value);
    }

    public JsonUtilsBuilder addPrivateKeyId(Object value) {
      return addParam("private_key_id", value);
    }

    public JsonUtilsBuilder addPrivateKey(Object value) {
      return addParam("private_key", value);
    }

    public JsonUtilsBuilder addClientEmail(Object value) {
      return addParam("client_email", value);
    }

    public JsonUtilsBuilder addClientId(Object value) {
      return addParam("client_id", value);
    }

    public JsonUtilsBuilder addAuthUri(Object value) {
      return addParam("auth_uri", value);
    }

    public JsonUtilsBuilder addTokenUri(Object value) {
      return addParam("token_uri", value);
    }

    public JsonUtilsBuilder addAuthProviderUrl(Object value) {
      return addParam("auth_provider_x509_cert_url", value);
    }

    public JsonUtilsBuilder addClientCertUrl(Object value) {
      return addParam("client_x509_cert_url", value);
    }

    public String build() {
      return toStringBuilder.build();
    }
  }
}