package epam.report.emailsender.controller;

import epam.report.emailsender.metrics.AgricultureMetrics;
import epam.report.emailsender.model.AgricultureCompany;
import epam.report.emailsender.repository.AgricultureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agriculture")
@Slf4j
public class AgricultureCompanyController {

    private final AgricultureRepository repository;
    private final AgricultureMetrics metrics;

    @PostMapping
    public Long create(@RequestBody AgricultureCompany company) {
        long start = System.currentTimeMillis();

        AgricultureCompany saved = repository.save(company);
        metrics.incrementCreate();

        metrics.recordLatency(System.currentTimeMillis() - start);

        log.info("Agriculture company created with id={}", saved.getId());
        return saved.getId();
    }

    @GetMapping("/get/{id}")
    public AgricultureCompany get(@PathVariable Long id) {
        long start = System.currentTimeMillis();

        AgricultureCompany company = repository.findById(id).orElseThrow();
        metrics.incrementGet();

        metrics.recordLatency(System.currentTimeMillis() - start);

        log.info("Agriculture company fetched with id={}", id);
        return company;
    }
}
