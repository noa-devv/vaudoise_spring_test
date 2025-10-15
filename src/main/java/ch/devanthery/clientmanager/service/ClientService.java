package ch.devanthery.clientmanager.service;

import ch.devanthery.clientmanager.model.Client;
import ch.devanthery.clientmanager.model.ClientUpdateDto;
import ch.devanthery.clientmanager.repository.ClientRepository_I;
import ch.devanthery.clientmanager.repository.ContractRepository_I;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Qualifier("ClientService")
public class ClientService implements ClientService_I {

    private final ClientRepository_I repository;
    private final ContractRepository_I contractRepository;

    public ClientService(ClientRepository_I repository, ContractRepository_I contractRepository) {
        this.repository = repository;
        this.contractRepository = contractRepository;
    }

    @Override
    public Client create(Client client) {
        return repository.save(client);
    }

    @Override
    public Iterable<Client> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Client> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Client update(Long id, ClientUpdateDto updatedFields) {
        Client existingClient = repository.findById(id).orElseThrow(() -> new RuntimeException("Client not found with ID: " + id));

        if (updatedFields.getName() != null) existingClient.setName(updatedFields.getName());
        if (updatedFields.getPhone() != null) existingClient.setPhone(updatedFields.getPhone());
        if (updatedFields.getEmail() != null) existingClient.setEmail(updatedFields.getEmail());

        return repository.save(existingClient);
    }

    //We don't delete the client really, we just update the conctract end date
    @Override
    public void delete(Long id) {
        Client currentClient = repository.findById(id).orElseThrow(() -> new RuntimeException("Client not found with ID: " + id));
        
        for (var contract : currentClient.getContracts()) {
            if (contract.isActive()) {
                contract.setEndDate(LocalDate.now());
            }
            contractRepository.save(contract);
        }
    }

    @Override
    public double getSumOfClientActiveContracts(Long clientId){

        LocalDate today = LocalDate.now();

        return repository.findById(clientId).orElseThrow().getContracts().stream()
            .parallel()
            .filter(c -> c.getEndDate() == null || today.isBefore(c.getEndDate()))
            .mapToDouble(c -> c.getCostAmount())
            .sum();
    }
}
