package epam.report.emailsender.service;

import epam.report.emailsender.model.DueDateWarningEmailDto;
import epam.report.emailsender.model.Task;
import epam.report.emailsender.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Getter
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    private static final String FROM_EMAIL = "abdufattoxovshaxzod0404@gmail.com";

    public void sendNotificationToUserForTask(DueDateWarningEmailDto emailDto) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            Context context = new Context();
            context.setVariable("firstName", emailDto.getFirstName());
            context.setVariable("lastName", emailDto.getLastName());
            context.setVariable("tasks", emailDto.getTasks());


            String htmlContent = springTemplateEngine.process("due-date-warning-email", context);

            helper.setTo(emailDto.getEmail());
            helper.setSubject("🔔 Reminder: Upcoming Task Deadlines");
            helper.setText(htmlContent, true);
            helper.setFrom(FROM_EMAIL);
            message.setSubject("Task deadline reminder for "+ emailDto.getFirstName()+ " "+ emailDto.getLastName());

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendEmailForNewUser(User newUser) throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        message.setSubject("Welcome to our Platform, " + newUser.getFirstName() + "!");


        Context context = new Context();
        context.setVariable("email", newUser.getEmail());
        context.setVariable("name", newUser.getFirstName());
        context.setVariable("password", newUser.getPassword());

        String htmlContent = springTemplateEngine.process("new-user-email", context);

        helper.setText(htmlContent, true);
        helper.setTo(newUser.getEmail());
        helper.setSubject("Welcome to EduSystem, " + newUser.getFirstName() + "!");

        helper.setFrom(FROM_EMAIL);

        javaMailSender.send(message);
    }




    public void sendNotificationToNewUser(User newUser) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        mimeMessage.setSubject("Welcome to the Platform, " + newUser.getFirstName() + "!");

        Context context = new Context();
        context.setVariable("email", newUser.getEmail());
        context.setVariable("name", newUser.getFirstName());
        context.setVariable("password", newUser.getPassword());

        context.setVariable("loginUrl", "http://localhost:8080/api/v1/auth/login");

        String htmlContent = springTemplateEngine.process("new-user-email", context);

        helper.setText(htmlContent, true);
        helper.setTo(newUser.getEmail());
        helper.setSubject("Welcome to the Platform, " + newUser.getFirstName() + "!");
        helper.setFrom(FROM_EMAIL);

        javaMailSender.send(mimeMessage);
    }

    public void sendNotificationToAssigners(Task task, User assigner) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        mimeMessage.setSubject("New Task Assigned: " + task.getTitle());

        helper.setSubject("New Task Assigned: " + task.getTitle());
        helper.setFrom(FROM_EMAIL);
        helper.setTo(assigner.getEmail());

        Context context = new Context();
        context.setVariable("taskTitle", task.getTitle());
        String description = task.getDescription() != null && task.getDescription().length() > 40 ? task.getDescription().substring(0, 40) : task.getDescription();
        context.setVariable("taskDescription", description + "...");
        context.setVariable("taskPriority", task.getPriority());
        context.setVariable("taskStatus", task.getTaskStatus());
        context.setVariable("taskDeadline", task.getEndTime() == null ? null : task.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        context.setVariable("taskUrl", "http://localhost:8080/tasks/" + task.getId());

        context.setVariable("assignedByName", task.getReporter().getFirstName());
        context.setVariable("assignedByEmail", task.getReporter().getFirstName());
        context.setVariable("assigneeName", assigner.getFirstName());

        String htmlContent = springTemplateEngine.process("task-assigned-email", context);
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }


    @Async
    public void sendNotificationAboutToMentionComment(User commentOwner, Task task, List<User> mentionedUsers, String comment) throws MessagingException {
        for (User mentionedUser : mentionedUsers) {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");

            mimeMessage.setSubject("You Were Mentioned in a Comment on Task: " + task.getTitle());

            helper.setTo(mentionedUser.getEmail());
            helper.setSubject("You Were Mentioned in a Comment on Task: " + task.getTitle());
            helper.setFrom(FROM_EMAIL);

            Context context = new Context();

            context.setVariable("mentionedUserName", mentionedUser.getFirstName());
            context.setVariable("commentAuthorName", commentOwner.getFirstName());
            context.setVariable("commentText", comment);
            context.setVariable("taskTitle", task.getTitle());

            String description = task.getDescription() != null && task.getDescription().length() > 40 ? task.getDescription().substring(0, 40) : task.getDescription();

            String priorityClass;
            switch (task.getPriority()) {
                case LOW:
                    priorityClass = "priority-low";
                    break;
                case NORMAL:
                    priorityClass = "priority-normal";
                    break;
                case HIGH:
                    priorityClass = "priority-high";
                    break;
                case URGENT:
                    priorityClass = "priority-urgent";
                    break;
                default:
                    priorityClass = "priority-low";
            }

            context.setVariable("taskDescription", description);
            context.setVariable("taskPriority", priorityClass);
            context.setVariable("taskDeadline", task.getEndTime() == null ? null : task.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            context.setVariable("taskUrl", "http://localhost:8080/tasks/" + task.getId());

            String htmlContent = springTemplateEngine.process("mention-notification-email", context);
            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
        }
    }
}

