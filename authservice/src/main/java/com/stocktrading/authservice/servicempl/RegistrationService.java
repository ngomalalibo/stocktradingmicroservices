package com.stocktrading.authservice.servicempl;

import com.google.common.base.Strings;
import com.stocktrading.authservice.entity.Client;
import com.stocktrading.authservice.entity.PersistingBaseEntity;
import com.stocktrading.authservice.entity.User;
import com.stocktrading.authservice.exception.CustomNullPointerException;
import com.stocktrading.authservice.repository.GenericDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.util.Map;

@Slf4j
@Service
//@RequiredArgsConstructor
public class RegistrationService implements TransactionService
{
    @Qualifier("userDataRepository")
    @Autowired
    private GenericDataRepository userDataRepository;
    
    @Autowired
    PersistingBaseEntity persistingBaseEntity;
    
    //register new client users
    @Override
    public Object service(Map<String, Object> params) throws NonUniqueResultException, CustomNullPointerException
    {
        Client client = new Client();
        String username = params.get("user").toString();
        String password = params.get("pass").toString();
        if (client == null)
        {
            throw new CustomNullPointerException("Please provide an existing client in order to create an account");
        }
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password))
        {
            throw new CustomNullPointerException("Please provide a valid username or password");
        }
        else if ((exists(username)) != null)
        {
            log.info("This user exists in the system. Kindly reset your password of choose another username");
            return null;
            //throw new NonUniqueResultException("This user exists in the system. Kindly reset your password of choose another username");
        }
        User user = new User(username, new BCryptPasswordEncoder(11).encode(password), "USER", username);
        // token = jwtTokenProvider.createToken(user);
        user.setToken("token");
        client.setEmail(username);
        
        //  confirm registration was successful by retrieving for user and client from database
        client = (Client) persistingBaseEntity.save(client);
        user = (User) persistingBaseEntity.save(user);
        
        if (user != null && client != null && user.getUsername().equalsIgnoreCase(client.getEmail()))
        {
            return "token"; // user and client creation confirmed
        }
        return null;
    }
    
    public String exists(String username)
    {
        User user = (User) userDataRepository.getRecordByEntityProperty("username", username);
        if (user == null)
        {
            return null; // user does not exist
        }
        else
        {
            return user.getUsername(); // user already exists
        }
    }
}
