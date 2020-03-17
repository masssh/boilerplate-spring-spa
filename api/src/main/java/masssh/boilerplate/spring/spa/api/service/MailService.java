package masssh.boilerplate.spring.spa.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailService {
    private final MailSender mailSender;

    public void sendMail() {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("from@example.com");
        message.setTo("to@example.com");
        message.setSubject("subject");
        message.setText("text");
        mailSender.send(message);
    }
}
