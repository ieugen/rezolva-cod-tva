package ro.ieugen.tva;

import lombok.extern.slf4j.Slf4j;
import ro.ieugen.tva.api.model.CompanyRecord;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class MailService {

    private final Map<String, Object> env;
    public static final String TVA_SMTP_HOST = "tva_smtp_host";
    public static final String TVA_SMTP_USERNAME = "tva_smtp_username";
    public static final String TVA_SMTP_PASS = "tva_smtp_pass";

    public MailService(Map<String, Object> env) {
        this.env = env;
    }

    void sendEmail(String destination, List<CompanyRecord> records) {

        Session session = createSessionWithAuthentication();
        String msgBody = Arrays.toString(records.toArray());

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress((String) env.get(MailService.TVA_SMTP_USERNAME), "Rezolva TVA"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(destination, destination));
            msg.setSubject("Companiile au fost verificate");

            msg.setText(msgBody);
            log.info("Sending email...");
            Transport.send(msg);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.info("Exception sending email", e);
            e.printStackTrace();
        }
    }

    private Session createSessionWithAuthentication() {

        final String smtpUserName = (String) env.get(TVA_SMTP_USERNAME);
        final String smtpPassword = (String) env.get(TVA_SMTP_PASS);

        return Session.getDefaultInstance(configureMailTransport(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUserName, smtpPassword);
            }
        });
    }

    private Properties configureMailTransport() {
        Properties props = new Properties();

        String smtpHost = (String) env.get(TVA_SMTP_HOST);

        log.info("Using mail config: {} ", env);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.debug", "true");
        props.put("mail.debug.auth", "true");


        return props;
    }
}
