package org.brownmun.web.about;

import lombok.Data;

/**
 * A member of the secretariat. Because I didn't feel like repeating all the HTML so I decided to
 * model them here.
 */
@Data
public class SecretariatMember
{
    String title;

    String name;

    String bio;

    String imageAddress;

    String email;
}
