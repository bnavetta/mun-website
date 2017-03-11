package org.brownmun.busun;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.brownmun.mail.EmailDescriptor;
import org.brownmun.mail.MailService;
import org.brownmun.model.*;
import org.brownmun.model.repo.CommitteeRepository;
import org.brownmun.model.repo.DelegateRepository;
import org.brownmun.model.repo.PositionRepository;
import org.brownmun.model.repo.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Testing stuff out
 */
@Slf4j
@Component
public class TestRunner implements CommandLineRunner
{
	@Autowired
	private MailService mailService;

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

//		mailService.send(msg);
	}
}
