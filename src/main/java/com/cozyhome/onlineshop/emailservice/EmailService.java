package com.cozyhome.onlineshop.emailservice;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cozyhome.onlineshop.dto.EmailMessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

	private final JavaMailSender mailSender;
	
	public void sendEmail(EmailMessageDto emailMessageDto) {		
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessageDto.getMailTo());
        message.setSubject(emailMessageDto.getSubject());
        message.setText(emailMessageDto.getText());
        log.info("[ON sendActivationEmail] :: The message has been sent to: " + emailMessageDto.getMailTo());
        mailSender.send(message);
    }
}
