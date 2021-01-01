package com.stocktrading.customer.controller;

import com.google.common.base.Strings;
import com.stocktrading.customer.entity.Client;
import com.stocktrading.customer.event.SimpleSourceBean;
import com.stocktrading.customer.repository.GenericDataRepository;
import com.stocktrading.customer.serviceImpl.ClientService;
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
    
    @Autowired
    SimpleSourceBean simpleSourceBean;
    
    @Autowired
    ClientService clientService;
    
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/client/{id}")
    public ResponseEntity<Object> getClient(@PathVariable String id)
    {
        return ResponseEntity.ok(clientService. getClient(id));
    }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/client/new")
    public ResponseEntity<Object> newClient(@RequestBody Client client)
    {
        if (client != null)
        {
            Client save = (Client) client.save(client);
            if (save != null)
            {
                
                return ResponseEntity.ok(clientService.saveClient(save));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Client not saved. Something went wrong. Try again");
    }
    
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/client/delete/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable String id)
    {
        if (!Strings.isNullOrEmpty(id))
        {
            Client client = (Client) clientDataRepository.getRecordByEntityProperty("_id", id);
            
            if (client != null)
            {
                System.out.println("Client dummy deleted>>>>>>>>>>");/** @DeleteMapping endpoint deletes data even with dummy method block */
                Client client1 = clientService.getClient(id);
                if (client1 != null)
                {
                    return ResponseEntity.ok(clientService.deleteClient(client1));
                }
                
                /*boolean delete = client.delete(MongoConnectionImpl.DB_CLIENT, client);
                if (delete)
                {
                    return ResponseEntity.ok(client);
                }*/
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Client not deleted. Something went wrong. Try again");
    }
}
