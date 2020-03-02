package com.usmanadio.banka.testconfig;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.io.InputStream;

@Profile("test")
public class MockMailSender implements JavaMailSender {

    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(InputStream inputStream) throws MailException {
        return null;
    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {

    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {

    }

    @Override
    public void send(final MimeMessagePreparator mimeMessagePreparator) throws MailException {
//        final MimeMessage mimeMessage = createMimeMessage();
//        try {
//            mimeMessagePreparator.prepare(mimeMessage);
//            final String content = (String) mimeMessage.getContent();
//            final Properties javaMailProperties = getJavaMailProperties();
//            javaMailProperties.setProperty("mailContent", content);
//        } catch (final Exception e) {
//            throw new MailPreparationException(e);
//        }
    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage... simpleMailMessages) throws MailException {

    }
}
