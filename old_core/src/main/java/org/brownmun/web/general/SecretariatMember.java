package org.brownmun.web.general;

/**
 * A member of the staff. Because I didn't feel like repeating all the HTML so I decided to
 * model them here.
 */
public class SecretariatMember
{
    private String title;

    private String name;

    private String bio;

    private String imageAddress;

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

    public String getImageAddress()
    {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress)
    {
        this.imageAddress = imageAddress;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
