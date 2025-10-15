package ch.devanthery.clientmanager;

import ch.devanthery.clientmanager.model.Client;
import ch.devanthery.clientmanager.model.Company;
import ch.devanthery.clientmanager.model.Person;
import ch.devanthery.clientmanager.model.ClientUpdateDto;
import ch.devanthery.clientmanager.model.Contract;
import ch.devanthery.clientmanager.repository.ClientRepository_I;
import ch.devanthery.clientmanager.repository.ContractRepository_I;
import ch.devanthery.clientmanager.service.ClientService;
import ch.devanthery.clientmanager.service.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClientmanagerApplicationTests {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ContractService contractService;

    @Mock
    private ClientRepository_I clientRepository;

    @Mock
    private ContractRepository_I contractRepository;

    private Person personClient;
    private Company companyClient;

    private Contract activeContract;
    private Contract endedContract;

    @BeforeEach
    public void setup() {
        personClient = new Person();
        personClient.setName("John Doe");
        personClient.setEmail("john@example.com");

        companyClient = new Company();
        companyClient.setName("Acme Inc");
        companyClient.setEmail("contact@acme.com");

        activeContract = new Contract();
        activeContract.setCostAmount(1000.0);
        activeContract.setStartDate(LocalDate.now().minusDays(10));
        activeContract.setEndDate(null); //Active

        endedContract = new Contract();
        endedContract.setCostAmount(500.0);
        endedContract.setStartDate(LocalDate.now().minusDays(20));
        endedContract.setEndDate(LocalDate.now().minusDays(1)); //Ended

        personClient.setContracts(List.of(activeContract, endedContract));
        activeContract.setClient(personClient);
        endedContract.setClient(personClient);
    }

    @Test
    public void testCreateClient() {
        when(clientRepository.save(personClient)).thenReturn(personClient);

        Client created = clientService.create(personClient);

        assertNotNull(created);
        assertEquals("John Doe", created.getName());
    }

    @Test
    public void testUpdateClient() {
        ClientUpdateDto dto = new ClientUpdateDto();
        dto.setEmail("newemail@example.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(personClient));
        when(clientRepository.save(any(Client.class))).thenReturn(personClient);

        Client updated = clientService.update(1L, dto);

        assertEquals("newemail@example.com", updated.getEmail());
    }

    @Test
    public void testDeleteClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(personClient));
        when(contractRepository.save(any(Contract.class))).thenReturn(activeContract);

        clientService.delete(1L);

        assertNotNull(activeContract.getEndDate());
        assertTrue(activeContract.getEndDate().isBefore(LocalDate.now().plusDays(1)));
    }

    @Test
    public void testGetSumOfClientActiveContracts() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(personClient));

        double sum = clientService.getSumOfClientActiveContracts(1L);

        assertEquals(1000.0, sum);
    }

    @Test
    public void testSumPerformance() {
        List<Contract> bigList = new java.util.ArrayList<>();
        for (int i = 0; i < 10000; i++) { //Performance test for 10000 contracts
            Contract c = new Contract();
            c.setCostAmount(10.0);
            c.setStartDate(LocalDate.now().minusDays(5));
            c.setEndDate(null);
            bigList.add(c);
        }
        personClient.setContracts(bigList);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(personClient));

        long start = System.nanoTime();
        double sum = clientService.getSumOfClientActiveContracts(1L);
        long end = System.nanoTime();

        assertEquals(10000 * 10.0, sum);
        System.out.println("Performance test duration (ms): " + (end - start)/1_000_000);
    }

    @Test
    public void testCreateContractForClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(personClient));
        when(contractRepository.save(activeContract)).thenReturn(activeContract);

        Contract created = contractService.create(activeContract, 1L);

        assertNotNull(created.getClient());
        assertNotNull(personClient.getContracts());
    }
}