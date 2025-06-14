package com.outsera.movie.core;

import com.outsera.movie.infra.database.converters.MovieEntityConverter;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Configuration
@EnableR2dbcRepositories
@EnableTransactionManagement
@RequiredArgsConstructor
public class R2DBCConfig {

  @Bean
  ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);
    initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
    return initializer;
  }

  @Bean
  public R2dbcCustomConversions r2dbcCustomConversions() {
    List<Converter<?, ?>> converterList = new ArrayList<>();
    converterList.add(new UUIDWriteConverter());
    converterList.add(new UUIDReadConverter());
    converterList.add(new MovieEntityConverter());

    return new R2dbcCustomConversions(CustomConversions.StoreConversions.NONE, converterList);
  }

  @WritingConverter
  public static class UUIDWriteConverter implements Converter<UUID, String> {
    @Override
    public String convert(final UUID source) {
      return source.toString();
    }
  }

  @ReadingConverter
  public static class UUIDReadConverter implements Converter<String, UUID> {
    @Override
    public UUID convert(final String source) {
      return UUID.fromString(source);
    }
  }

}
