package epam.report.emailsender.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AgricultureMetrics {

    private final Counter createCounter;
    private final Counter getCounter;
    private final Timer apiTimer;

    public AgricultureMetrics(MeterRegistry registry) {
        this.createCounter = Counter.builder("agriculture_api_create_total")
                .description("Total number of agriculture create requests")
                .register(registry);

        this.getCounter = Counter.builder("agriculture_api_get_total")
                .description("Total number of agriculture get requests")
                .register(registry);

        this.apiTimer = Timer.builder("agriculture_api_latency")
                .description("Latency of agriculture API endpoints")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);
    }

    public void incrementCreate() {
        createCounter.increment();
    }

    public void incrementGet() {
        getCounter.increment();
    }

    public void recordLatency(long durationMs) {
        apiTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }
}
