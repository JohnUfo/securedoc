package online.muydinov.securedoc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/user/login").permitAll()
                                .anyRequest().authenticated())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        var myOwnAuthenticationProvider = new ApiAuthenticationprovider(userDetailsService);
        return new ProviderManager(myOwnAuthenticationProvider);
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        var m1lymoe = User.withDefaultPasswordEncoder()
//                .username("m1lymoe")
//                .password("{noop}2373")
//                .roles("USER")
//                .build();
//
//        var m2lymoe = User.withDefaultPasswordEncoder()
//                .username("m2lymoe")
//                .password("{noop}2373")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(List.of(m1lymoe, m2lymoe));
//    }

    @Bean
    public UserDetailsService inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("m1lymoe")
                        .password("2373")
                        .roles("USER")
                        .build(),
                User.withUsername("m2lymoe")
                        .password("2373")
                        .roles("USER")
                        .build()
        );
    }

}
