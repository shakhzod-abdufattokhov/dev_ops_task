package epam.report.emailsender.controller;

import epam.report.emailsender.model.DueDateWarningEmailDto;
import epam.report.emailsender.model.User;
import epam.report.emailsender.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody User user) throws MessagingException {
        emailService.sendEmailForNewUser(user);
        return ResponseEntity.ok("Email sended");
    }



    @PostMapping("/warning")
    public ResponseEntity<String> sendEmailWarning(@RequestBody DueDateWarningEmailDto warningEmailDto) throws MessagingException {
        emailService.sendNotificationToUserForTask(warningEmailDto);
        return ResponseEntity.ok("Email sended");
    }
}
