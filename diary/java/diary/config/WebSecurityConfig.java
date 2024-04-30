package diary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import diary.service.MyUserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
	
	private MyUserService userService;
	
	@Autowired
    public WebSecurityConfig (MyUserService userService) {
        this.userService = userService;
    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/js/", "/css/", "/loginForm").permitAll()
						.anyRequest().authenticated())
				.formLogin(login -> login
						
			            .loginProcessingUrl("/login")
			            
			            .defaultSuccessUrl("/diary", true)
			            .failureUrl("/loginForm?error=true"))
				.logout(logout -> logout
						.permitAll());

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withDefaultPasswordEncoder()
				.username("yyamada")
				.password("yyamada")
				.roles("USER")
				.build();

		UserDetails adminDetail = User.withDefaultPasswordEncoder()
				.username("admin")
				.password("adminpass")
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(userDetails, adminDetail);
	}
	
	
	
	
	

}
