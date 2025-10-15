package ch.devanthery.clientmanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table(name = "Contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    @FutureOrPresent(message="The end date should be in the future")
    private LocalDate endDate;

    @Positive(message="The amount cost should be positive")
    private Double costAmount;
    private LocalDate lastUpdateDate;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=true)
    @JsonBackReference
    private Client client;

    public Contract() {}

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setCostAmount(Double costAmount) {
        this.costAmount = costAmount;
        setLastUpdateDate();
    }

    public Double getCostAmount() {
        return costAmount;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setLastUpdateDate() {
        this.lastUpdateDate = LocalDate.now();
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public boolean isActive() {
        return endDate == null || LocalDate.now().isBefore(endDate);
    }
}
