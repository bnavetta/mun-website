package org.brownmun.busun;

import com.google.common.collect.ImmutableMap;
import org.brownmun.mail.EmailDescriptor;
import org.brownmun.mail.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Testing stuff out
 */
@Component
public class TestRunner implements CommandLineRunner
{
	private static final Logger log = LoggerFactory.getLogger(TestRunner.class);
	
	@Autowired
	private MailSender mailSender;

	@Transactional
	@Override
	public void run(String... args) throws Exception
	{
		log.info("Running test logic");

		EmailDescriptor msg = new EmailDescriptor();
		msg.setFrom("test@busun.org");
		msg.setRecipients(ImmutableMap.of("ben.navetta@gmail.com", ImmutableMap.of(
		    "name", "Ben"
        )));
		msg.setReplyTo(Optional.of("technology@busun.org"));
		msg.setSubject("Testing");
		msg.setHtml("<h1>Hello, %recipient.name%</h1>\nThis is a test email.");

//		mailSender.send(msg);
	}
}
