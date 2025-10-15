package ch.devanthery.clientmanager.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;

@Entity
@DiscriminatorValue("COMPANY")
public class Company extends Client {

    @Pattern(
        regexp = "^[a-zA-Z]{3}-\\d{3}$",
        message = "The company identifier is invalid. Exemple: ABC-123"
    )
    private String companyIdentifier;

    public void setCompanyIdentifier(String companyIdentifier) {
        this.companyIdentifier = companyIdentifier;
    }

    public String getCompanyIdentifier() {
        return companyIdentifier;
    }
}
