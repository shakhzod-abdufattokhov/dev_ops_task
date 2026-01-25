package epam.report.emailsender.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ApiHealthMetrics {

    private final Counter requestCounter;
    private final Counter errorCounter;
    private final Timer latencyTimer;
    private final AtomicInteger apiUp;

    public ApiHealthMetrics(MeterRegistry registry) {

        this.requestCounter = Counter.builder("api_requests_total")
                .description("Total API requests")
                .tag("api", "agriculture_get")
                .register(registry);

        this.errorCounter = Counter.builder("api_errors_total")
                .description("Total API errors")
                .tag("api", "agriculture_get")
                .register(registry);

        this.latencyTimer = Timer.builder("api_latency")
                .description("API latency")
                .tag("api", "agriculture_get")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        this.apiUp = registry.gauge(
                "api_up",
                Tags.of("api", "agriculture_get"),
                new AtomicInteger(1)
        );
    }

    public void recordRequest() {
        requestCounter.increment();
    }

    public void recordError() {
        errorCounter.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start();
    }

    public void stopTimer(Timer.Sample sample) {
        sample.stop(latencyTimer);
    }

    public void markUp() {
        apiUp.set(1);
    }

    public void markDown() {
        apiUp.set(0);
    }
}
