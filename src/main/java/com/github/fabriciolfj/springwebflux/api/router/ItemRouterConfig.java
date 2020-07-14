package com.github.fabriciolfj.springwebflux.api.router;

import com.github.fabriciolfj.springwebflux.api.handler.ItemHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ItemRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeItem(final ItemHandlerFunction handlerFunction) {
        return RouterFunctions.route(
                GET("/v2/items").and(accept(MediaType.APPLICATION_JSON)), handlerFunction::findAll);
    }
}
