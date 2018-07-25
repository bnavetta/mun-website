package org.brownmun.web.security;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.security.web.header.HeaderWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.List;

/**
 * Adds the {@code Expect-CT} header to requests.
 *
 * @see <a href="https://scotthelme.co.uk/a-new-security-header-expect-ct">A new security header: Expect-CT</a>
 */
public class ExpectCTHeaderWriter implements HeaderWriter
{
    private static final String EXPECT_CT_HEADER = "Expect-CT";

    private final String policy;

    public ExpectCTHeaderWriter(boolean enforce, Duration maxAge, String reportUri)
    {
        List<String> directives = Lists.newArrayList();

        if (enforce)
        {
            directives.add("enforce");
        }

        directives.add("max-age=" + maxAge.getSeconds());

        if (!Strings.isNullOrEmpty(reportUri))
        {
            directives.add("report-uri=\"" + reportUri + "\"");
        }

        this.policy = String.join(", ", directives);
    }

    @Override
    public void writeHeaders(HttpServletRequest request, HttpServletResponse response)
    {
        response.setHeader(EXPECT_CT_HEADER, policy);
    }

    @Override
    public String toString()
    {
        return String.format("ExpectCTHeaderWriter{%s}", policy);
    }
}
