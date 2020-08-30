package com.github.fabriciolfj.springwebflux.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
/*import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;*/

//@EnableWebFluxSecurity
public class SpringWebFluxSecurityConfig {

    /*@Bean
    public SecurityWebFilterChain filter(final ServerHttpSecurity http) {
        //http.authorizeExchange().anyExchange().authenticated().and().httpBasic().and().formLogin();
        http.authorizeExchange().pathMatchers("/v1/items/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/v2/items/**").hasRole("USER")
                .pathMatchers(HttpMethod.POST, "/v2/items").hasRole("ADMIN")
                .anyExchange().authenticated().and().httpBasic().and().formLogin();

        http.csrf().disable();
        return http.build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetails() {
        var user = User.withDefaultPasswordEncoder()
                .username("fabricio").password("mega").roles("USER").build();

        var admin = User.withDefaultPasswordEncoder()
                .username("admin").password("admin").roles("ADMIN", "USER").build();

        return new MapReactiveUserDetailsService(user, admin);
    }*/
}
