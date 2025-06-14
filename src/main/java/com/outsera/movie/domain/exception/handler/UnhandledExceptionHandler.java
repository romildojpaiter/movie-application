package com.outsera.movie.domain.exception.handler;

import com.outsera.movie.controller.response.ErrorResponse;
import com.outsera.movie.domain.exception.BaseErrorMessage;
import com.outsera.movie.domain.exception.BaseException;
import com.outsera.movie.domain.exception.NotFoundException;
import com.outsera.movie.domain.exception.UnauthenticatedException;
import com.outsera.movie.domain.exception.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsera.movie.utils.JsonUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * Any exception that happens before the process reaches a Controller class.
 * Ex: UnauthenticatedException
 */
@Component
@Order(-2)
@Slf4j
@RequiredArgsConstructor
public class UnhandledExceptionHandler implements WebExceptionHandler {

    @NonNull
    private final ObjectMapper mapper;
    private final DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull final ServerWebExchange exchange, @NonNull final Throwable ex) {
        return Mono.error(ex)
                .onErrorResume(MethodNotAllowedException.class, e -> handleMethodNotAllowedException(exchange, e))
                .onErrorResume(UnauthenticatedException.class, e -> handleUnauthenticatedException(exchange, e))
                .onErrorResume(UnauthorizedException.class, e -> handleUnauthorizedException(exchange, e))
                .onErrorResume(throwable -> throwable instanceof ResponseStatusException || throwable instanceof NotFoundException, e -> handleResourceNotFoundException(exchange, ex))
                .onErrorResume(e -> handleGenericException(exchange, e))
                .onErrorResume(JsonProcessingException.class, e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    log.error("==== Failed to map exception for the request {} ====",
                            JsonUtils.builder()
                                    .addPath(exchange.getRequest().getPath().value())
                                    .build() , ex);
                    return exchange.getResponse().setComplete();
                })
                .then();
    }

    /**
     * handle 405 error
     */
    private Mono<Void> handleMethodNotAllowedException(final ServerWebExchange exchange, final MethodNotAllowedException ex) {
        return Mono.fromCallable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    log.error("==== Method is not allowed {} ====",
                            JsonUtils.builder()
                                    .addMethodValue(exchange.getRequest().getMethod())
                                    .addPath(exchange.getRequest().getPath().value())
                                    .addParam("messages",  ex.getLocalizedMessage())
                                    .build());
                    return BaseErrorMessage.GENERIC_METHOD_NOT_ALLOWED.withParams(ex.getHttpMethod()).getMessage();
                }).map(baseErrorMessage -> this.buildError(baseErrorMessage, ex.getClass().getSimpleName()))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    /**
     * handle 401 error
     */
    private Mono<Void> handleUnauthenticatedException(final ServerWebExchange exchange, final UnauthenticatedException ex) {
        return Mono.fromCallable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    log.error("==== Request was not authenticated {} ====",
                            JsonUtils.builder()
                                    .addMethodValue(exchange.getRequest().getMethod())
                                    .addPath(exchange.getRequest().getPath().value())
                                    .addParam("userAgent",   exchange.getRequest().getHeaders().getFirst("user-agent"))
                                    .build());
                    return ex.getLocalizedMessage();
                }).map(baseErrorMessage -> this.buildError(baseErrorMessage, ex.getClass().getSimpleName()))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    /**
     * handle 403 error
     */
    private Mono<Void> handleUnauthorizedException(final ServerWebExchange exchange, final UnauthorizedException ex) {
        return Mono.fromCallable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    log.error("==== Request was not unauthorized {} ====",
                            JsonUtils.builder()
                                    .addMethodValue(exchange.getRequest().getMethod())
                                    .addPath(exchange.getRequest().getPath().value())
                                    .addParam("userAgent",   exchange.getRequest().getHeaders().getFirst("user-agent"))
                                    .build());
                    return ex.getLocalizedMessage();
                }).map(baseErrorMessage -> this.buildError(baseErrorMessage, ex.getClass().getSimpleName()))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    /**
     * handle 404 error
     */
    private Mono<Void> handleResourceNotFoundException(final ServerWebExchange exchange, final Throwable ex) {
        return Mono.fromCallable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    log.error("==== Method is not found {} ====",
                            JsonUtils.builder()
                                    .addMethodValue(exchange.getRequest().getMethod())
                                    .addPath(exchange.getRequest().getPath().value())
                                    .addParam("messages",  ex.getLocalizedMessage())
                                    .build());
                    return BaseErrorMessage.GENERIC_NOT_FOUND_EXCEPTION.getMessage();
                }).map(baseErrorMessage -> this.buildError(baseErrorMessage, ex.getClass().getSimpleName()))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    /**
     * handle any exception as a server error
     */
    private Mono<Void> handleGenericException(final ServerWebExchange exchange, Throwable ex) {
        return Mono.fromCallable(() -> {
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    log.error("==== Generic exception {} ====",
                            JsonUtils.builder()
                                    .addMethodValue(exchange.getRequest().getMethod())
                                    .addPath(exchange.getRequest().getPath().value())
                                    .build(),
                            ex);
                    return BaseErrorMessage.GENERIC_EXCEPTION.getMessage();
                }).map(baseErrorMessage -> this.buildError(baseErrorMessage, BaseException.class.getSimpleName()))
                .flatMap(problemResponse -> writeResponse(exchange, problemResponse));
    }

    /**
     * Write the given error response in the server response
     */
    private Mono<Void> writeResponse(final ServerWebExchange exchange, final ErrorResponse errorResponse) {
        return exchange.getResponse()
                .writeWith(Mono.fromCallable(() -> dataBufferFactory.wrap(mapper.writeValueAsBytes(errorResponse))));
    }

    private ErrorResponse buildError(final String errorDescription, final String errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorDescription(errorDescription)
                .build();
    }
}

