package com.stocktrading.authservice.controller;

import com.stocktrading.authservice.entity.User;
import com.stocktrading.authservice.exception.ApiResponse;
import com.stocktrading.authservice.repository.GenericDataRepository;
import com.stocktrading.authservice.servicempl.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
public class RegistrationController
{
    @Qualifier("userDataRepository")
    @Autowired
    private GenericDataRepository userDataRepository;
    
    @Qualifier("registrationService")
    @Autowired
    TransactionService services;
    
    
    // @PreAuthorize("{hasAuthority('USER'), #oauth2.hasScope('read')}")
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = {"/user"}, produces = "application/json")
    public Map<String, Object> user(OAuth2Authentication user)
    {
        Map<String, Object> userInfo = new HashMap<>();
        
        Object principal = user.getUserAuthentication().getPrincipal();
        System.out.println("principal -> " + principal.toString());
        
        Set<String> value = AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities());
        value.forEach(d -> System.out.print("authority -> " + d + " "));
        
        userInfo.put("user", principal);
        userInfo.put("authorities", value);
        userInfo.put("details", user.getUserAuthentication().getDetails());
        return userInfo;
    }
    
    
/*public ResponseEntity<Object> register(@RequestBody HashMap<String, Object> request)
    {
        
        return Optional.ofNullable(services.register(request.get("user").toString(), request.get("pass").toString(), new Client())).filter(Objects::nonNull)
                       .map(token -> ResponseEntity.ok()
                                                   .header("API_TOKEN", "User has been registered successfully with token \n" + token)
                                                   .body((Object) new ApiResponse(HttpStatus.OK, "User has been registered successfully with token " +
                                                           token, HttpStatus.OK.getReasonPhrase())))
                       .orElse(ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST, "User creation failed", HttpStatus.BAD_REQUEST.getReasonPhrase())));
    }*/
    
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/currentuser")
    public ResponseEntity<Object> currentUser(OAuth2Authentication userDetails)
    {
        if (userDetails != null)
        {
            Map<Object, Object> model = new HashMap<>();
            model.put("username", userDetails.getName());
            model.put("roles", userDetails.getAuthorities());
            User user = (User) userDataRepository.getRecordByEntityProperty("username", userDetails.getName());
            String token = null;
            if (user != null)
            {
                token = user.getToken();
            }
            return ResponseEntity.ok().body((Object) model);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user principal");
    }
    
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/test")
    public ResponseEntity<ApiResponse> test()
    {
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK, "Hello OAuth", HttpStatus.OK.getReasonPhrase()));
    }
    
    @PostMapping(value = "/registration")
    public ResponseEntity<Object> register(@RequestBody HashMap<String, Object> request)
    {
        String token = (String) services.service(request);
        if (token != null)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "User has been registered successfully with token " +
                    token, HttpStatus.OK.getReasonPhrase());
            return ResponseEntity.ok().body(apiResponse);
        }
        else
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "User creation failed", HttpStatus.BAD_REQUEST.getReasonPhrase());
            return ResponseEntity.of(Optional.of(apiResponse));
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User creation failed");
        }
    }
    
    
}
