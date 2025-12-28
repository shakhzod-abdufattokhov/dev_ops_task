package epam.report.emailsender.model;

import epam.report.emailsender.model.enums.TaskPriority;
import lombok.*;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {

    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private String taskStatus;
    private LocalTime endTime;
    private User reporter;
}
