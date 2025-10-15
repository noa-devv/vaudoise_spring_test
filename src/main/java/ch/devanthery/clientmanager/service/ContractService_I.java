package ch.devanthery.clientmanager.service;

import ch.devanthery.clientmanager.model.Contract;

import java.time.LocalDate;

public interface ContractService_I {
    public Contract create(Contract contract, Long clientId);
    public Iterable<Contract> getAllByClientId(Long id, LocalDate updateDateFilter);
    public Contract update(Contract contract, Long id);
    public void delete(Long id);
}
