package au.edu.rmit.sept.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import jakarta.mail.internet.MimeMessage;

@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/send-email")
    public ResponseEntity<Map<String, String>> sendEmail(
            @RequestParam("email") String email,
            @RequestParam("pdf") MultipartFile pdfFile) {

        Map<String, String> response = new HashMap<>();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("vetcare851@gmail.com");
            helper.setTo(email);
            helper.setSubject("Medical Record PDF");
            helper.setText(
                    "Attached to this email, you will find the medical records for your pet. The document includes important information regarding your pet’s health history, treatments, and any prescriptions that have been provided.\n"
                            + //
                            "\n" + //
                            "Please review the attached records carefully. If you have any questions or need further information, don’t hesitate to reach out. We are here to assist you.\n"
                            + //
                            "\n" + //
                            "Thank you for trusting us with your pet’s care.");

            // Attach the PDF file
            helper.addAttachment("medical-record.pdf", new ByteArrayResource(pdfFile.getBytes()));

            // Send the email

            // SimpleMailMessage message = new SimpleMailMessage();
            // message.setFrom("vetcare851@gmail.com");
            // message.setTo(email);
            // message.setSubject("Medical Record PDF");
            // message.setText("Attached is the medical record PDF.");

            mailSender.send(message);
            response.put("success", "true");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
