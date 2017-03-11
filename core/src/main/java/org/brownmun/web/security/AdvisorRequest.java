package org.brownmun.web.security;

import lombok.Data;

/**
 * A request to create a new school advisor, with all the required information.
 */
@Data
public class AdvisorRequest
{
    String name;
    String email;
}
