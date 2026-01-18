package epam.report.emailsender.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class EmailMetrics {

    private final Counter sentEmails;

    public EmailMetrics(MeterRegistry registry) {
        this.sentEmails = Counter.builder("emails_sent_total")
                .description("Total sent emails")
                .register(registry);
    }

    public void increment() {
        sentEmails.increment();
    }
}
