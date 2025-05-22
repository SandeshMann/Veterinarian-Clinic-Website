package au.edu.rmit.sept.webapp.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Marking this class as a configuration class for Spring.
@EnableWebSecurity // Enabling Spring Security for the web application.
public class SecurityConfigurations {

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure which requests are allowed without authentication (login into the
        // system)
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                // Allow user to access to home page, signup page, and static resources without
                // authentication
                .requestMatchers("/", "/signup", "/profile", "/css/**", "/js/**",
                        "/images/**", "/resources/**")
                .permitAll()
                // Any other request requires authentication
                .anyRequest().authenticated()).csrf().disable()
                .anonymous()
                .and() // Add this to chain the next configuration
                .formLogin(form -> form
                        .loginPage("/login") // Customized login page.
                        .defaultSuccessUrl("/profile", true) // Redirect to home page after successful login
                        .failureUrl("/login?error=true") // Redirect to login page with error with throwing an error.
                        .permitAll())
                // Configuring logout behavior of the system
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "Idea-2e8e7cee")
                        .logoutSuccessUrl("/") // Redirect the user to home page after successful logout
                        .permitAll());
        return http.build();
    }

    // Not yet implemented to hash the user password and store to database.
    // Therefore, we use NoOp encoder.
    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Temporarily use NoOp encoder (for plain-text passwords)
    }

}
