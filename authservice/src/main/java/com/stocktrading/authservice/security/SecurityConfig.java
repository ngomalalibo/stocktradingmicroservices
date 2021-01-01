package com.stocktrading.authservice.security;

import com.stocktrading.authservice.repository.GenericDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Qualifier("userDataRepository")
    @Autowired
    GenericDataRepository userDataRepository;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    public SecurityConfig()
    {
        super();
        this.userDetailsService = new UsersDP(userDataRepository);
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    { //
        return super.authenticationManagerBean();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(11);
    }
    
    @Bean
    @Override
    public UserDetailsService userDetailsService()
    {
        return new UsersDP(userDataRepository);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.authenticationProvider(new UserAuthenticationProvider());
    }
    
    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper()
    {
        SimpleAuthorityMapper authMapper = new SimpleAuthorityMapper();
        authMapper.setConvertToUpperCase(true);
        authMapper.setDefaultAuthority("USER");
        authMapper.setPrefix("");
        return authMapper;
    }
    
    
}