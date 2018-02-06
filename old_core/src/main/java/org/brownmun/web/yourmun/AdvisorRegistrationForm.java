package org.brownmun.web.yourmun;

import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

/**
 * Models the form submitted by advisors creating an account.
 */
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

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("token", token)
                .add("password", password)
                .add("passwordConfirm", passwordConfirm)
                .add("phoneNumber", phoneNumber)
                .toString();
    }
}
