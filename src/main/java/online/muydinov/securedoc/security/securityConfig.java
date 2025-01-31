package online.muydinov.securedoc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static online.muydinov.securedoc.constant.Constants.STRENGTH;

@Configuration
public class securityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }

}
