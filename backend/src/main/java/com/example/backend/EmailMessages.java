package com.example.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailMessages {

    @Value("${email.infoEmail}")
    private String infoEmail;

    public String NewEmailSubject() {
        return "Welcome to Learning cards!";
    }
    public String NewAccountBody() {
        return
                "Dear user,\n\n" +
                "Welcome aboard! We're thrilled to have you as a new member of Learning cards. Your registration is the first step towards an exciting journey with us.\n\n" +
                "If you have any questions or need assistance, don't hesitate to reach out to our support team at " + infoEmail + ". We're here to help!\n\n" +
                "Once again, welcome to Learning cards. We're thrilled to have you on board, and we can't wait to see all the amazing things you'll accomplish here.\n\n" +
                "Thank you,\n" +
                "Learning-cards Team";
    }

    public String RecoverPasswordUsingEmailsubject() {
        return "Action Required: Password Reset for Your Learning-cards Account";
    }

    public String RecoverPasswordUsingEmailbody(String passwordResetLink) {
        return "Dear user,\n\n" +
                "We received a request to reset the password for your Learning-cards account. If you did not make this request, please ignore this email.\n\n" +
                "To reset your password, click on the following link:\n" +
                passwordResetLink + "\n\n" +
                "This link is valid for 2 hours. After that, you will need to submit another password reset request.\n\n" +
                "If you have any questions or need further assistance, please contact our support team at " + infoEmail + ".\n\n" +
                "Thank you,\n" +
                "Learning-cards Team";
    }
    
    

}
