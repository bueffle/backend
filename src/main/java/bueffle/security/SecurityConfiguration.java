package bueffle.security;

import bueffle.service.BackendUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final
    BackendUserDetailsService userDetailsService;

    public SecurityConfiguration(BackendUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //filters are just for testing like this
                .antMatchers("/collections").hasRole("USER")
                .antMatchers("/cards/*").hasRole("USER")
                .antMatchers("/cards").permitAll()
                .antMatchers("/user").permitAll()
                .antMatchers("/user/*").permitAll()
                .antMatchers("/").permitAll()
                .and().formLogin()
                .and().csrf().disable(); // Only activated for testing with postman
    }

    //Only for testing, real PasswordEncoder will be implemented
    @Bean
    public PasswordEncoder gPE() {
        return NoOpPasswordEncoder.getInstance();
    }
}
