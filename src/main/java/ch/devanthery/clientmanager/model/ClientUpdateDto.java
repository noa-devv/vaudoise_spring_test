package ch.devanthery.clientmanager.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ClientUpdateDto {
    @NotBlank(message = "Should not be blank")
    private String name;

    @Pattern(
        regexp = "(0\\d{2}|\\+\\d{4})\\s?\\d{3}\\s?\\d{2}\\s?\\d{2}", 
        message = "The phone number is invalid. Exemple: 012 345 67 89 or +4112 345 67 89"
    )
    private String phone;

    @Email(message="Invalid email")
    private String email;

    public ClientUpdateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}