package org.brownmun.mail.mailgun;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.brownmun.mail.EmailDescriptor;
import org.brownmun.mail.MailException;
import org.brownmun.mail.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * {@link MailSender} that uses the Mailgun API
 */
public class MailgunMailSender implements MailSender
{
    private static final Logger log = LoggerFactory.getLogger(MailgunMailSender.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper jsonMapper;
    private final TaskExecutor executor;

    /**
     * Create a new Mailgun-based {@link MailSender} from the provided {@link RestTemplate}. Assumes
     * that the template is configured with the Mailgun API base URL and credentials.
     * @param restTemplate
     */
    public MailgunMailSender(RestTemplate restTemplate, ObjectMapper jsonMapper, TaskExecutor executor)
    {
        this.restTemplate = restTemplate;
        this.jsonMapper = jsonMapper;
        this.executor = executor;
    }

    @Override
    public void send(EmailDescriptor message) throws MailException
    {
        log.debug("Sending message {} through Mailgun", message);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("from", message.getFrom());
        params.put("to", Lists.newArrayList(message.getRecipients().keySet()));
        try
        {
            params.add("recipient-variables", jsonMapper.writeValueAsString(message.getRecipients()));
        }
        catch (JsonProcessingException e)
        {
            throw new MailException("Invalid recipient variables", e, message);
        }
        message.getReplyTo().ifPresent(replyTo -> params.add("h:Reply-To", replyTo));

        params.add("subject", message.getSubject());
        message.getText().ifPresent(text -> params.add("text", text));
        message.getHtml().ifPresent(html -> params.add("html", html));

        for (String tag : message.getTags())
        {
            params.add("o:tag", tag);
        }

        for (Resource inline : message.getInline())
        {
            params.add("inline", inline);
        }

        for (Resource attachment : message.getAttachments())
        {
            params.add("attachment", attachment);
        }

        executor.execute(() -> {


        try
        {
            MailgunMessageResponse response =
                restTemplate.postForObject("/messages", params, MailgunMessageResponse.class);
            log.debug("Sent message {} (response: {})", response.getId(), response.getMessage());
        }
        catch (RestClientException e)
        {
            log.error("Error sending message to Mailgun", e);
//            throw new MailException("Unable to submit message", e, message);
        }
        });
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
