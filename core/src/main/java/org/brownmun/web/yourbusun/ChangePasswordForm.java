package org.brownmun.web.yourbusun;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Models the form for when an advisor changes their password.
 */
@Data
public class ChangePasswordForm
{
    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;
}
