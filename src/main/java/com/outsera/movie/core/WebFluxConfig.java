package com.outsera.movie.core;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Component
@Profile("!test")
@AllArgsConstructor
public class WebFluxConfig implements WebFluxConfigurer {

    private final Jackson2JsonEncoder encoder;
    private final Jackson2JsonDecoder decoder;
    private final LocalValidatorFactoryBean localValidatorFactoryBean;

    @Override
    public Validator getValidator() {
        return localValidatorFactoryBean;
    }

    @Override
    public void configureHttpMessageCodecs(final ServerCodecConfigurer configure) {
        configure.defaultCodecs().jackson2JsonEncoder(encoder);
        configure.defaultCodecs().jackson2JsonDecoder(decoder);
    }
}
