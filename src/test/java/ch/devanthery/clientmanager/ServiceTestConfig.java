package ch.devanthery.clientmanager;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import ch.devanthery.clientmanager.repository.ClientRepository_I;
import ch.devanthery.clientmanager.repository.ContractRepository_I;
import ch.devanthery.clientmanager.service.ClientService;
import ch.devanthery.clientmanager.service.ContractService;

import org.mockito.Mockito;

@ContextConfiguration
@TestConfiguration
public class ServiceTestConfig {

    @Bean
    public ClientRepository_I clientRepository() {
        return Mockito.mock(ClientRepository_I.class);
    }

    @Bean
    public ContractRepository_I contractRepository() {
        return Mockito.mock(ContractRepository_I.class);
    }

    @Bean
    public ClientService clientService(ClientRepository_I clientRepository, ContractRepository_I contractRepository) {
        return new ClientService(clientRepository, contractRepository);
    }

    @Bean
    public ContractService contractService(ContractRepository_I contractRepository, ClientRepository_I clientRepository) {
        return new ContractService(contractRepository, clientRepository);
    }
}
