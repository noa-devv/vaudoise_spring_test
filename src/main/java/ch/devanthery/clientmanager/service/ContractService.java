package ch.devanthery.clientmanager.service;

import ch.devanthery.clientmanager.model.Client;
import ch.devanthery.clientmanager.model.Contract;
import ch.devanthery.clientmanager.repository.ClientRepository_I;
import ch.devanthery.clientmanager.repository.ContractRepository_I;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@Qualifier("ContractService")
public class ContractService implements ContractService_I {

    private final ContractRepository_I repository;
    private final ClientRepository_I clientRepository;

    public ContractService(ContractRepository_I repository, ClientRepository_I clientRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Contract create(Contract contract, Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow();
        contract.setClient(client);
        contract.setLastUpdateDate();

        if(contract.getStartDate() == null){
            contract.setStartDate(LocalDate.now());
        }

        return repository.save(contract);
    }

    @Override
    public Iterable<Contract> getAllByClientId(Long clientId, LocalDate updateDateFilter) {
        Client client = clientRepository.findById(clientId).orElseThrow();

        LocalDate today = LocalDate.now();

        return client.getContracts().stream()
            .filter(c -> c.getEndDate() == null || today.isBefore(c.getEndDate())) //c for "contrat"
            .filter(c -> updateDateFilter == null || !c.getLastUpdateDate().isBefore(updateDateFilter))
            .toList();
    }

    @Override
    public Contract update(Contract contract, Long id) {
        Contract existingContract = repository.findById(id).orElseThrow(() -> new RuntimeException("Contract not found with ID: " + id));

        if (contract.getStartDate() != null) existingContract.setStartDate(contract.getStartDate());
        if (contract.getEndDate() != null) existingContract.setEndDate(contract.getEndDate());
        if (contract.getCostAmount() != null) existingContract.setCostAmount(contract.getCostAmount()); //Last updated date update here

        return repository.save(existingContract);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
