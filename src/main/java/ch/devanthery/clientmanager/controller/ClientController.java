package ch.devanthery.clientmanager.controller;

import ch.devanthery.clientmanager.model.Contract;
import ch.devanthery.clientmanager.model.Person;
import ch.devanthery.clientmanager.service.ClientService;
import ch.devanthery.clientmanager.model.Client;
import ch.devanthery.clientmanager.model.ClientUpdateDto;
import ch.devanthery.clientmanager.model.Company;
import ch.devanthery.clientmanager.service.ContractService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private static final Logger LOGGER = Logger.getLogger(ClientController.class.getName());

    private final ClientService ClientService;
    //private final ContractService contractService;

    public ClientController(@Qualifier("ClientService") ClientService ClientService/*, ContractService contractService*/) {
        this.ClientService = ClientService;
        //this.contractService = contractService;
    }

    //GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        LOGGER.info("Fetching Client with ID: " + id);
        return ClientService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //GET ALL
    /*@GetMapping
    public ResponseEntity<Iterable<Client>> getAllClients() {
        LOGGER.info("Fetching all Clients");
        return ResponseEntity.ok(ClientService.getAll());
    }*/

    //POST
    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        LOGGER.info("Creating a new person: " + person);
        try {
            return ResponseEntity.status(201).body((Person) ClientService.create(person));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/company")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        LOGGER.info("Creating a new company: " + company);
        try {
            return ResponseEntity.status(201).body((Company) ClientService.create(company));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody ClientUpdateDto updatedFields) {
        LOGGER.info("Updating Client with ID: " + id);
        try {
            return ResponseEntity.ok(ClientService.update(id, updatedFields));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        LOGGER.info("Deleting Client with ID: " + id);
        try {
            ClientService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
