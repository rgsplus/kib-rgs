package nl.rgs.kib.service;

import jakarta.activation.DataSource;
import jakarta.mail.internet.InternetAddress;

import java.util.Map;

public interface MailService {

    void sendMail(final String htmlContent, final String subject, final String sender,
                  final InternetAddress[] mailTo, final InternetAddress[] cc, Map<String, DataSource> attachments) throws jakarta.mail.MessagingException;

}