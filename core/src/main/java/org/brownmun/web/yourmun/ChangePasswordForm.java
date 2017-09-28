package org.brownmun.web.yourmun;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Models the form for when an advisor changes their password.
 */
public class ChangePasswordForm
{
    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPasswordConfirm()
    {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm)
    {
        this.passwordConfirm = passwordConfirm;
    }
}
