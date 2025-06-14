package com.outsera.movie.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataUtils {

  public static UUID getUUID(String uuid) {
    return Optional.ofNullable(uuid).map(UUID::fromString).orElse(null);
  }


  public static Boolean getBoolean(Byte value) {
    return Optional.ofNullable(value).map(v -> v == 1).orElse(null);
  }
}
