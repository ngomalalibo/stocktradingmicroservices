package com.stocktrading.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter
{
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    TokenStore tokenStore;
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
    {
        clients.inMemory()
               .withClient("stocktrading")
               .secret(new BCryptPasswordEncoder(11).encode("thisissecret"))
               .autoApprove(true)
               .authorities("ROLE_USER", "ROLE_ADMIN", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
               .authorizedGrantTypes("refresh_token", "password", "client_credentials")
               .scopes("webclient", "mobileclient", "read", "write", "trust");
    }
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService).tokenStore(tokenStore);
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception
    {
        security.passwordEncoder(new BCryptPasswordEncoder(11))
                .checkTokenAccess("permitAll()")
                .tokenKeyAccess("permitAll()");
    }
    
    
}