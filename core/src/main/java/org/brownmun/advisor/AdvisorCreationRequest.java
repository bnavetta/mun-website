package org.brownmun.advisor;

import lombok.Data;

/**
 * A request to create a new school advisor, with all the required information.
 */
@Data
public class AdvisorCreationRequest
{
    String name;
    String email;
}
