package epam.report.emailsender.controller;

import epam.report.emailsender.metrics.ApiHealthMetrics;
import epam.report.emailsender.model.AgricultureCompany;
import epam.report.emailsender.repository.AgricultureRepository;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agriculture")
@RequiredArgsConstructor
@Slf4j
public class AgricultureCompanyController {

    private final AgricultureRepository repository;
    private final ApiHealthMetrics metrics;

    @Timed(
            value = "agriculture_api_create_latency",
            description = "Latency of create agriculture company API",
            percentiles = {0.5, 0.95, 0.99}
    )
    @PostMapping
    public Long create(@RequestBody AgricultureCompany company) {
        AgricultureCompany saved = repository.save(company);

        log.info("Agriculture company created successfully, id={}", saved.getId());
        return saved.getId();
    }

    @Timed(
            value = "agriculture_api_get_latency",
            description = "Latency of get agriculture company API",
            percentiles = {0.5, 0.95, 0.99}
    )
    @GetMapping("/get/{id}")
    public AgricultureCompany get(@PathVariable Long id) {

        metrics.recordRequest();
        Timer.Sample sample = metrics.startTimer();

        try {
            AgricultureCompany company = repository.findById(id).orElseThrow();
            metrics.markUp();
            return company;

        } catch (Exception ex) {
            metrics.recordError();
            metrics.markDown();
            throw ex;

        } finally {
            metrics.stopTimer(sample);
        }
    }

}
