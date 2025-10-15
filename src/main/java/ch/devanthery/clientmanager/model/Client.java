package ch.devanthery.clientmanager.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "Clients")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "client_type")
public abstract class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Should not be blank")
    private String name;

    @Pattern(
        regexp = "(0\\d{2}|\\+\\d{4})\\s?\\d{3}\\s?\\d{2}\\s?\\d{2}", 
        message = "The phone number is invalid. Exemple: 012 345 67 89 or +4112 345 67 89"
    )
    private String phone;

    @Email(message="Invalid email")
    private String email;

    @OneToMany(mappedBy = "client", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @JsonManagedReference
    List<Contract> contracts = new ArrayList<>();

    public Client() {}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }
}

