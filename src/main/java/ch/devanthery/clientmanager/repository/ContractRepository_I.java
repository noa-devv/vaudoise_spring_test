package ch.devanthery.clientmanager.repository;

import ch.devanthery.clientmanager.model.Contract;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContractRepository_I extends CrudRepository<Contract,Long> {
    List<Contract> findByClientId(Long id);

    @Query("SELECT c FROM Contract c WHERE c.client IS NULL")
    Iterable<Contract> findAllWithoutClient();
}



