package nl.rgs.kib.service;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import nl.rgs.kib.service.impl.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MailServiceImplTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private MailServiceImpl mailService;

    @Test
    public void testSendMail() throws MessagingException {
        String htmlContent = "<h1>Hello</h1>";
        String subject = "Test Subject";
        String sender = "sender@example.com";
        InternetAddress[] mailTo = {new InternetAddress("recipient@example.com")};
        InternetAddress[] cc = {new InternetAddress("cc@example.com")};
        Map<String, DataSource> attachments = new HashMap<>();

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailService.sendMail(htmlContent, subject, sender, mailTo, cc, attachments);

        verify(emailSender).send(mimeMessage);
    }
}