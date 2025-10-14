package ch.devanthery.clientmanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO Contracts fields

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    @JsonBackReference
    private Client client;

    public Contract() {}

    //TODO Getters and setters

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}
