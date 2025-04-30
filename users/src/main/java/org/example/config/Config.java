package org.example.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, ConfigProperties.class})
public class Config {

//    @Bean
//    public AuthenticationProvider authenticationProvider(UserRepository userRepository)  {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(getUserDetailsService(userRepository));
//        authenticationProvider.setPasswordEncoder(getPasswordEncoder());
//        return authenticationProvider;
//    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsServiceImpl getUserDetailsService(UserRepository userRepository){
//        return new UserDetailsServiceImpl(userRepository);
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
}
