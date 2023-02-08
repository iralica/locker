package com.example.locker;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@Configuration //конфигурационный бин
public class SecurityConfiguration {
    @Bean
    public static /*NoOpPasswordEncoder*/ PasswordEncoder getEncoder()
    {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
        // return new BCryptPasswordEncoder();
    }

    // для аутентификации и авторизации вызовов HTTP методов
    @Bean
    public SecurityFilterChain getChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(
                        auth ->
                                auth
                                        .requestMatchers(toH2Console()).permitAll()
                                        .requestMatchers("/h2-console/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/index.html", "/", "/open", "/h2-console**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/logout.html", "/secure").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/admin/**").hasAnyRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .formLogin()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .httpBasic(Customizer.withDefaults())
                .logout() //     POST /logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        ;
        return http.build();
    }

//    @Bean
   // public InMemoryUserDetailsManager(){
        // создаем пользователей
        // создаем InMemoryUserDetailsManager
        // добавляем туда пользователей
        // возвращаем InMemoryUserDetailsManager
      //  UserDetails admin = User.withUsername("admin")
        //        .password("admin")
         //     .roles("ADMIN")
          //      .build();

        //UserDetails user = User.withUsername("user")
         //       .password("user")
         //       .roles("USER")
          //      .build();
        //return new InMemoryUserDetailsManager(admin, user);
   // }

    @Autowired
    private MyUserDetailsService userDetailsService;

    // РњРµС…Р°РЅРёР·Рј РґР»СЏ РїСЂРѕРІРµСЂРєРё РїРѕР»СЊР·РѕРІР°С‚РµР»РµР№ С‡РµСЂРµР· Р±Р°Р·Сѓ РґР°РЅРЅС‹С…
    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // provider.setUserDetailsService(userDetailsService());
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(getEncoder());
        return provider;
    }

}
