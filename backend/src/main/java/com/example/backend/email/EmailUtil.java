package com.example.backend.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

    @Value("${email.fromEmail}")
    private String fromEmail;

    @Value("${email.password}")
    private String password;

    @Value("${email.smtpHost}")
    private String smtpHost;

    @Value("${email.sslPort}")
    private String sslPort;

    @Value("${email.smtpPort}")
    private String smtpPort;

    @Value("${email.personalName}")
    private String personalName;


    // TODO add boolean if success.
    public void SendEmail(String toEmail, String subject, String body) {
        System.out.println("SSLEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost); // SMTP Host
        props.put("mail.smtp.socketFactory.port", sslPort); // SSL Port
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
        props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
        props.put("mail.smtp.port", smtpPort); // SMTP Port

        Authenticator auth = new Authenticator() {
            // override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getDefaultInstance(props, auth);
        System.out.println("Session created");

        try {
            MimeMessage msg = new MimeMessage(session);
            // set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromEmail, personalName));

            msg.setReplyTo(InternetAddress.parse(fromEmail, false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("Email Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
