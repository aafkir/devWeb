package fr.umontpellier.projetweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /*
        @Autowired
        public void globalConfig(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("admin").password("{noop}123").roles("ADMIN");
                    auth.inMemoryAuthentication().withUser("user2").password("{noop}1234").rolesTOURIST");
    } */


        @Autowired
        DataSource dataSource;

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
           //PasswordEncoder passwordEncoder = passwordEncoder();
            //System.out.println("password Encoder"+ passwordEncoder.encode("1234"));
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .usersByUsernameQuery("select login as principal, password  as credentials ,'true' as enabled from t_users where login=?") //? = la valeur saisie
                    .authoritiesByUsernameQuery("select login as principal , roleName  as role from t_users u join  t_users_roles_associations t on u.idUSer=t.idUser join t_roles r ON r.idROle=t.idRole where u.login=?")
                    .passwordEncoder(new BCryptPasswordEncoder());

        //.anyRequest().authenticated();
        }

  @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/list").permitAll()
                .antMatchers("/rechercher").hasAnyAuthority("ADMIN","TOURIST")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutUrl("/appLogout")
                .logoutSuccessUrl("/login");
        //loginPage("/login");

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*    // create two users, admin and user
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("ADMIN");
    }*/
}
