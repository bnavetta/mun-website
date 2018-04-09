package org.brownmun.cli.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class PasswordCommands
{
    private final PasswordEncoder encoder;

    @Autowired
    public PasswordCommands(PasswordEncoder encoder)
    {
        this.encoder = encoder;
    }

    @ShellMethod("Encode a password")
    public String encode(String password)
    {
        return encoder.encode(password);
    }
}
