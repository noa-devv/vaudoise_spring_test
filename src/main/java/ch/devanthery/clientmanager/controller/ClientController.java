package ch.devanthery.clientmanager.controller;

import ch.devanthery.clientmanager.model.Contract;
import ch.devanthery.clientmanager.model.Person;
import ch.devanthery.clientmanager.service.ClientService;
import ch.devanthery.clientmanager.model.Client;
import ch.devanthery.clientmanager.model.ClientUpdateDto;
import ch.devanthery.clientmanager.model.Company;
import ch.devanthery.clientmanager.service.ContractService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private static final Logger LOGGER = Logger.getLogger(ClientController.class.getName());

    private final ClientService ClientService;
    private final ContractService contractService;

    public ClientController(@Qualifier("ClientService") ClientService ClientService, @Qualifier("ContractService")ContractService contractService) {
        this.ClientService = ClientService;
        this.contractService = contractService;
    }

    //GET client by id
    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) {
        LOGGER.info("Fetching Client with ID: " + clientId);
        return ClientService.getById(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //POST person
    @PostMapping("/person")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        LOGGER.info("Creating a new person: " + person);
        try {
            return ResponseEntity.status(201).body((Person) ClientService.create(person));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //POST company
    @PostMapping("/company")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        LOGGER.info("Creating a new company: " + company);
        try {
            return ResponseEntity.status(201).body((Company) ClientService.create(company));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //PATCH client
    @PatchMapping("/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @Valid @RequestBody ClientUpdateDto updatedFields) {
        LOGGER.info("Updating Client with ID: " + clientId);
        try {
            return ResponseEntity.ok(ClientService.update(clientId, updatedFields));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE client
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        LOGGER.info("Deleting Client with ID: " + clientId);
        try {
            ClientService.delete(clientId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //GET contract by client id
    @GetMapping("/{clientId}/contracts")
    public ResponseEntity<Iterable<Contract>> getAllContracts(@PathVariable Long clientId, @RequestParam(required = false) LocalDate updateDateFilter) {
        LOGGER.info("Fetching all contracts for Client ID: " + clientId);
        try {
            return ResponseEntity.ok(contractService.getAllByClientId(clientId, updateDateFilter));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //POST contract by client id
    @PostMapping("/{clientId}/contracts")
    public ResponseEntity<Contract> postContracts(@PathVariable Long clientId, @Valid @RequestBody Contract contract) {
        LOGGER.info("Posting a contract for Client ID: " + clientId);
        System.out.print(clientId);
        try {
            return ResponseEntity.status(201).body(contractService.create(contract, clientId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //PATCH contract by client id
    @PatchMapping("/{clientId}/contracts/{contractId}")
    public ResponseEntity<Contract> putContracts(@PathVariable Long clientId, @PathVariable Long contractId, @Valid @RequestBody Contract contract) {
        LOGGER.info("Editing contract ID: " + contractId + " for Client ID: " + clientId);

        try {
            return ResponseEntity.ok(contractService.update(contract, contractId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE contract by client id
    @DeleteMapping("/{clientId}/contracts/{contractId}")
    public ResponseEntity<Void> deleteContracts(@PathVariable Long clientId, @PathVariable Long contractId) {
        LOGGER.info("Deleting contract ID: " + contractId + " for Client ID: " + clientId);
        try {
            contractService.delete(contractId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //GET sum of a client contracts
    @GetMapping("/{clientId}/contracts/sum")
    public ResponseEntity<Double> getSumOfContracts(@PathVariable Long clientId) {
        LOGGER.info("Fetching all contracts for Client ID: " + clientId);
        try {
            return ResponseEntity.ok(ClientService.getSumOfClientActiveContracts(clientId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
