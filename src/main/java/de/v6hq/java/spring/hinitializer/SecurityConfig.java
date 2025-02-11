package de.v6hq.java.spring.hinitializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .requiresChannel(channel -> channel
//                        .anyRequest().requiresSecure()
//                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame
                                .sameOrigin()
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        // Erlaubt den Zugriff auf die Homepage und Login-Seite ohne Authentifizierung
                        .requestMatchers("/", "/index.html", "/login**").permitAll() 
                        .anyRequest().authenticated() // Alle anderen Anfragen erfordern Authentifizierung
                )
                .oauth2Login(oauth2 -> oauth2 // Updated configuration style
                        //.loginPage("/login") // Specify the login page if needed
                        .defaultSuccessUrl("/dashboard")
                )
                .logout(logout -> logout
                        //.logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }
}