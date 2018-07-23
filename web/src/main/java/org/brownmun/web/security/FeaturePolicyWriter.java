package org.brownmun.web.security;

import org.springframework.security.web.header.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link HeaderWriter} implementation for the
 * <a href="https://scotthelme.co.uk/a-new-security-header-feature-policy/"><code>Feature-Policy</code></a>
 * header.
 */
public class FeaturePolicyWriter implements HeaderWriter
{
    private static final String FEATURE_POLICY_HEADER = "Feature-Policy";

    private final String featurePolicy;

    public FeaturePolicyWriter(String featurePolicy)
    {
        this.featurePolicy = featurePolicy;
    }

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response)
    {
        response.setHeader(FEATURE_POLICY_HEADER, featurePolicy);
    }
}
