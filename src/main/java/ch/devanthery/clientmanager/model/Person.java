package ch.devanthery.clientmanager.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@DiscriminatorValue("PERSON")
public class Person extends Client {

    @DateTimeFormat
    @PastOrPresent(message="The birthdate should be in the past")
    private LocalDate birthdate;

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }
}
