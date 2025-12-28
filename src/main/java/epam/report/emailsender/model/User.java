package epam.report.emailsender.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    private Long id;
    private String firstName;
    private String email;
    private String password;
}
