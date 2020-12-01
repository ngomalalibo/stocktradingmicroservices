package com.stocktrading.authservice.security;

import com.google.common.base.Strings;
import com.stocktrading.authservice.entity.User;
import com.stocktrading.authservice.repository.GenericDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class UserAuthenticationProvider extends DaoAuthenticationProvider
{
    private UsersDP usersDP;
    
    GrantedAuthoritiesMapper authoritiesMapper;
    
    GenericDataRepository userDataRepository;
    
    
    public UserAuthenticationProvider()
    {
        super();
        this.usersDP = new UsersDP(userDataRepository);
        userDataRepository = new GenericDataRepository(new User());
        authoritiesMapper = new SecurityConfig().authoritiesMapper();
    }
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        log.info("authenticating......");
        
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        log.info("authenticate -> " + username);
        log.info("credentials -> " + password);
        
        if (Strings.isNullOrEmpty(password) || Strings.isNullOrEmpty(username))
        {
            throw new BadCredentialsException("Invalid username or password.");
        }
        
        User usere = (User) userDataRepository.getRecordByEntityProperty("username", username);
        
        if (usere == null)
        {
            throw new BadCredentialsException("User does not exist");
        }
        
        if (new BCryptPasswordEncoder(11).matches(password, usere.getPassword()))
        {
            Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
            
            // String[] availableRoles = {"USER", "ADMIN", "SUPER_ADMIN"};
            // List.of(availableRoles).forEach(d -> grantedAuthorities.add(new SimpleGrantedAuthority(PersonRoleType.getDisplayText(d))));
            System.out.println("user role -> " + usere.getRole());
            grantedAuthorities.add(new SimpleGrantedAuthority(usere.getRole()));
            
            return new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
        }
        else
        {
            throw new BadCredentialsException("Invalid password.");
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    @Override
    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapperrr)
    {
        super.setAuthoritiesMapper(authoritiesMapper);
    }
    
    
    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder)
    {
        // super.getPasswordEncoder();
        super.setPasswordEncoder(new BCryptPasswordEncoder(11));
    }
    
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        this.setUserDetailsService(usersDP);
    }
    
    /*public static void main(String[] args)
    {
        String enc = "$2a$11$VtsZT9uB.SRUgqf.Sp4lHu4U1o4Cd7Ez/a8Ign/s4io1s1TbhJzoW";
        if (new BCryptPasswordEncoder(11).matches("1234567890", enc))
        {
            System.out.println("Confirmed");
        }
        else
        {
            System.out.println("Not confirmed");
        }
    }*/
}
