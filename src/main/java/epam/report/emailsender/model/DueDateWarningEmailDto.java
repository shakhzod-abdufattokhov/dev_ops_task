package epam.report.emailsender.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DueDateWarningEmailDto {

    private String firstName;
    private String lastName;
    private String email;

    private List<DueTaskInfo> tasks;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DueTaskInfo {
        private Long taskId;
        private String taskName;
        private LocalDateTime dueDate;
        private String priority;
        private String status;
        private String description;
    }
}

