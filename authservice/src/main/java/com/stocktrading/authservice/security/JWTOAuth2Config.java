package com.stocktrading.authservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class JWTOAuth2Config extends AuthorizationServerConfigurerAdapter
{
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private TokenStore tokenStore;
    
    @Autowired
    private DefaultTokenServices tokenServices;
    
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    
    @Autowired
    private TokenEnhancer jwtTokenEnhancer;
    
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception
    {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));
        
        endpoints.tokenStore(tokenStore)                             //JWT
                 .accessTokenConverter(jwtAccessTokenConverter)       //JWT
                 .tokenEnhancer(tokenEnhancerChain)                   //JWT
                 .authenticationManager(authenticationManager)
                 .userDetailsService(userDetailsService);
    }
    
    
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
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception
    {
        security.passwordEncoder(new BCryptPasswordEncoder(11))
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("permitAll()");
    }
}