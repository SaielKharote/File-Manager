package org.vcriate.filemanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {
    @Bean
    public SecurityFilterChain securityConfiguration(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(c -> c.disable());

        http.authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.POST, "/fm/auth/**").permitAll())
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/").permitAll())
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.GET,"/ws/**").permitAll())
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.GET, "/fm/files").authenticated())
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.GET, "/fm/files/download/**").authenticated())
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.POST, "/fm/files/upload").authenticated())
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.POST, "/fm/files/share/**").authenticated())
                .authorizeHttpRequests(c -> c.requestMatchers(HttpMethod.GET, "/fm/files/list").authenticated())
                .authorizeHttpRequests(c -> c.anyRequest().permitAll());

        http.addFilterBefore(new JwtTokenValidationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.cors(c -> c.configurationSource(request -> {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.setAllowedOrigins(Collections.singletonList("*")); // allowed all origins for testing frontend
            cfg.setAllowedMethods(Collections.singletonList("*")); // commented for frontend purpose
//                        cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Add other HTTP methods your application supports
            cfg.setAllowCredentials(true);
            cfg.setAllowedHeaders(Collections.singletonList("*"));
            cfg.setExposedHeaders(List.of("Authorization"));
            cfg.setMaxAge(3600L);
            return cfg;
        }));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}