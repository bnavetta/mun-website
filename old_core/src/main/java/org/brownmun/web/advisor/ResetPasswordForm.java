package org.brownmun.web.advisor;

import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

/**
 * Models the form submitted when resetting a password.
 */
public class ResetPasswordForm
{
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResetPasswordForm that = (ResetPasswordForm) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(password, that.password) &&
                Objects.equals(passwordConfirm, that.passwordConfirm);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(token, password, passwordConfirm);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("token", token)
                .add("password", password)
                .add("passwordConfirm", passwordConfirm)
                .toString();
    }
}
