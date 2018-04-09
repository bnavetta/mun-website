package org.brownmun.web.advisors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Models the password reset form
 */
public class PasswordResetForm
{
    @NotNull
    private String token;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

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
