package org.brownmun.web.advisor;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Models the form submitted when resetting a password.
 */
@Data
public class ResetPasswordForm
{
    private String token;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;
}
