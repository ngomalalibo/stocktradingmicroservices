package com.stocktrading.authservice.security;

import com.stocktrading.authservice.entity.User;
import com.stocktrading.authservice.repository.GenericDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDP implements UserDetailsService
{
    @Qualifier("userDataRepository")
    @Autowired
    private final GenericDataRepository userDataRepository;
    
    public UsersDP(GenericDataRepository userDataRepository)
    {
        this.userDataRepository = userDataRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException
    {
        User user = (User) userDataRepository.getRecordByEntityProperty("username", s);
        
        
        if (user == null)
        {
            throw new UsernameNotFoundException("user not found: " + s);
        }
        
        return new UserPrincipal(user);
    }
    
    /*public static void main(String[] args)
    {
        new UsersDP(new GenericDataRepository(new User())).loadUserByUsername("ngomalalibo@yahoo.com");
    }*/
}
