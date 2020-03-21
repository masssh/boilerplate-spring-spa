package masssh.boilerplate.spring.spa.api.service;

import lombok.RequiredArgsConstructor;
import masssh.boilerplate.spring.spa.property.ApplicationProperty.ApiProperty;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailService {
    private final MailSender mailSender;
    private final ApiProperty apiProperty;

    public void sendEmailVerification(final String email, final String verificationHash) {
        final SimpleMailMessage message = new SimpleMailMessage();
        final String verificationUrl = apiProperty.getEndpoint() + "/verify/email?q=" + verificationHash;
        message.setFrom("from@example.com");
        message.setTo(email);
        message.setSubject("Please confirm your email address");
        message.setText(verificationUrl);
        mailSender.send(message);
    }
}
