package elefant.clone.config;

import elefant.clone.model.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.ignoringRequestMatchers("/saveMsg")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/images/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/categories/jocuri").permitAll()
                        .requestMatchers("/categories/carte_straina_copii").permitAll()
                        .requestMatchers("/categories/carte_copii").permitAll()
                        .requestMatchers("/categories/carte_straina").permitAll()
                        .requestMatchers("/categories/jocuri_de_societate").permitAll()
                        .requestMatchers("/categories/lego").permitAll()
                        .requestMatchers("/categories/parfumuri").permitAll()
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/holidays/**").permitAll()
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/contact").permitAll()
                        .requestMatchers("/saveMsg").permitAll()
                        .requestMatchers("/courses").permitAll()
                        .requestMatchers("/about").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/images_spring/**").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/details/**").permitAll()
                        .requestMatchers("/addToCart/**").permitAll()
                        .requestMatchers("/cart").permitAll()
                        .requestMatchers("/cart/update/**").permitAll()
                        .requestMatchers("/cart/remove/**").permitAll()
                        .requestMatchers("/buy").permitAll()
                        .requestMatchers("/history").permitAll()
                        .requestMatchers("/cartCount/**").permitAll()
                        .requestMatchers("/dashboard", "dashboard/books").authenticated()
                        .requestMatchers("/categories").permitAll())

                .formLogin(loginConfigurer -> loginConfigurer.loginPage("/login")
                        .defaultSuccessUrl("/home").failureUrl("/login?error=true").permitAll())
                .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true).permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
