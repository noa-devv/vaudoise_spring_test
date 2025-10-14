package ch.devanthery.clientmanager.service;

import ch.devanthery.clientmanager.model.Client;
import ch.devanthery.clientmanager.model.ClientUpdateDto;
import ch.devanthery.clientmanager.repository.ClientRepository_I;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Qualifier("ClientService")
public class ClientService implements ClientService_I {

    private final ClientRepository_I repository;

    public ClientService(ClientRepository_I repository) {
        this.repository = repository;
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

        existingClient.setName(updatedFields.getName());
        existingClient.setPhone(updatedFields.getPhone());
        existingClient.setEmail(updatedFields.getEmail());

        return repository.save(existingClient);
    }

    @Override
    public void delete(Long id) {
        //TODO Mettre contrats du client a jour (date de fin)
        repository.deleteById(id);
    }
}
