package nl.rgs.kib.service.impl;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import nl.rgs.kib.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendMail(@NotNull final String htmlContent, @NotNull final String subject, @NotNull final String sender,
                         @NotNull final InternetAddress[] mailTo, final InternetAddress[] cc, Map<String, DataSource> attachments) throws jakarta.mail.MessagingException {
        MimeMessage mimeMessage = this.createMimeMessage();
        MimeMessageHelper messageHelper = this.createHelper(mimeMessage, sender);
        Arrays.stream(mailTo).forEach(internetAddress -> {
            try {
                messageHelper.addTo(internetAddress);
            } catch (jakarta.mail.MessagingException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        });
        if (cc != null) {
            Arrays.stream(cc).forEach(internetAddress -> {
                try {
                    messageHelper.addCc(internetAddress);
                } catch (jakarta.mail.MessagingException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            });
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(htmlContent, true);
        if (attachments != null) {
            attachments.forEach((s, dataSource) -> {
                try {
                    messageHelper.addAttachment(s, dataSource);
                } catch (jakarta.mail.MessagingException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            });
        }

        log.error(String.format("Sending email with subject '%s' to  '%s'", subject,
                Arrays.stream(mailTo).map(InternetAddress::getAddress)
                        .collect(Collectors.joining(", "))));
        this.sendEmail(mimeMessage);
    }


    protected void sendEmail(MimeMessage mimeMessage) {
        this.emailSender.send(mimeMessage);
    }

    protected MimeMessage createMimeMessage() {
        return this.emailSender.createMimeMessage();
    }

    protected MimeMessageHelper createHelper(MimeMessage mimeMessage, String sender) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
        helper.setFrom("RGS+ <".concat(sender).concat(">"));
        return helper;
    }
}
