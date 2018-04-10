package org.brownmun.core.staff;

import java.net.URI;
import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.google.common.base.MoreObjects;

/**
 * Models a member of the secretariat. This is used for display on the website
 * and for authentication.
 */
public class SecretariatMember
{
    @NotBlank
    private String title;

    @NotBlank
    private String name;

    @NotBlank
    private String bio;

    @NotNull
    private URI image;

    @Email
    private String email;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public URI getImage()
    {
        return image;
    }

    public void setImage(URI image)
    {
        this.image = image;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SecretariatMember that = (SecretariatMember) o;
        return Objects.equals(title, that.title) && Objects.equals(name, that.name) && Objects.equals(bio, that.bio)
                && Objects.equals(image, that.image) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(title, name, bio, image, email);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("title", title).add("name", name).add("bio", bio)
                .add("image", image).add("email", email).toString();
    }
}
