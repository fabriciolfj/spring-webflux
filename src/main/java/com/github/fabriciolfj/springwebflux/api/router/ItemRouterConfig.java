package com.github.fabriciolfj.springwebflux.api.router;

import com.github.fabriciolfj.springwebflux.api.handler.ItemHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeItem(final ItemHandlerFunction handlerFunction) {
        return RouterFunctions.route(
                GET("/v2/items").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::findAll)
                .andRoute(
                        GET("/v2/items/{id}").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::getOneItem)
                .andRoute(
                        POST("/v2/items").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::createItem)
                .andRoute(DELETE("/v2/items/{id}").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::delete)
                .andRoute(PUT("/v2/items/{id}").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::updateItem);
    }

    @Bean
    public RouterFunction<ServerResponse> routeError(final ItemHandlerFunction handlerFunction) {
        return RouterFunctions.route(
                GET("/v2/error").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::error);
    }
}
