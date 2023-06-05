package ng.com.createsoftware.ecommercestorebe.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private  JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable().cors().disable();
//        http.authorizeHttpRequests().anyRequest().permitAll();
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable();
        http.addFilterBefore(jwtRequestFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests()
//                .requestMatchers("api/v1/product").permitAll()
//                .requestMatchers("api/v1/order").permitAll()
//                .requestMatchers("auth/v1/api/register").permitAll()
                .requestMatchers("/api/v1/auth/**", "/api/v1/product",
                        "api/v1/auth/verify").permitAll()
                .anyRequest().authenticated();
        return http.build();
    }
}
