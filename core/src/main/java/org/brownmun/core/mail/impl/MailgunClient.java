package org.brownmun.core.mail.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Very simple client for the Mailgun API.
 */
public class MailgunClient
{
    private static final Logger log = LoggerFactory.getLogger(MailgunClient.class);

    private final RestTemplate restTemplate;

    public MailgunClient(MailgunProperties properties, RestTemplateBuilder builder)
    {
        this.restTemplate = builder.rootUri(properties.getBaseUri())
                .basicAuthorization(properties.getUsername(), properties.getApiKey()).build();
    }

    public void send(String from, String to, String replyTo, String subject, String html, String text)
            throws RestClientException
    {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("from", from);
        params.add("to", to);
        params.add("h:Reply-To", replyTo);
        params.add("subject", subject);
        params.add("html", html);
        params.add("text", text);

        log.debug("Sending email [{}] to {} via Mailgun", subject, to);

        MailgunMessageResponse response = restTemplate.postForObject("/messages", params, MailgunMessageResponse.class);
        log.debug("Message sent with ID {} (response: {})", response.getId(), response.getMessage());
    }

    private static class MailgunMessageResponse
    {
        private String message;
        private String id;

        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }
    }
}
