package org.brownmun.web.yourbusun;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

/**
 * Models the form submitted by advisors creating an account.
 */
@Data
public class AdvisorRegistrationForm
{
    /**
     * The unique token that was emailed to the advisor.
     */
    @NotNull
    String token;

    /**
     * The advisor's password.
     */
    @NotBlank
    String password;

    /**
     * The password again for confirmation.
     */
    @NotBlank
    String passwordConfirm;

    /**
     * The advisor's phone number.
     */
    @NotBlank
    String phoneNumber;

    @AssertTrue(message = "Passwords must match")
    public boolean passwordsMatch()
    {
        return password.equals(passwordConfirm);
    }
}
