package ch.devanthery.clientmanager.service;

import ch.devanthery.clientmanager.model.Client;
import ch.devanthery.clientmanager.model.ClientUpdateDto;

import java.util.Optional;

public interface ClientService_I {
    public Client create(Client client);
    public Iterable<Client> getAll();
    public Optional<Client> getById(Long id);
    public Client update(Long id, ClientUpdateDto updatedFields);
    public void delete(Long id);
}
