package com.facilization.task_tracking.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTaskAssignmentEmail(String toEmail, String taskTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Te eshte caktuar nje task i ri");
        message.setText("Pershendetje,\n\nTe eshte caktuar task-u: \"" + taskTitle +
                "\".\n\nFaleminderit,\nTask Tracking");
        mailSender.send(message);
    }
}