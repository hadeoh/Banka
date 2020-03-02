package com.usmanadio.banka.testconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
@Profile("test")
public class MailConfig {

    @Bean
    public JavaMailSender mailSender() {
        return new MockMailSender();
    }
}
