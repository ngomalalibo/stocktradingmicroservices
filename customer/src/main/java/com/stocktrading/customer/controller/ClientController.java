package com.stocktrading.customer.controller;

import com.stocktrading.customer.entity.Client;
import com.stocktrading.customer.entity.PersistingBaseEntity;
import com.stocktrading.customer.repository.GenericDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClientController
{
    @Qualifier("clientDataRepository")
    @Autowired
    private GenericDataRepository clientDataRepository;
    
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/client/{id}")
    public ResponseEntity<Object> getClient(@PathVariable String id)
    {
        return ResponseEntity.ok(clientDataRepository.getRecordByEntityProperty("_id", id));
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/client/new")
    public ResponseEntity<Object> newClient(@RequestBody Client client)
    {
        if (client != null)
        {
            PersistingBaseEntity save = client.save(client);
            if (save != null)
            {
                return ResponseEntity.ok(save);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Client not saved. Something went wrong. Try again");
    }
}
