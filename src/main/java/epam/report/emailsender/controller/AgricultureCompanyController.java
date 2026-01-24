package epam.report.emailsender.controller;

import epam.report.emailsender.model.AgricultureCompany;
import epam.report.emailsender.repository.AgricultureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agriculture")
public class AgricultureCompanyController {
    private final AgricultureRepository repository;

    @PostMapping
    public Long create(@RequestBody AgricultureCompany company){
        AgricultureCompany save = repository.save(company);
        return save.getId();
    }

    @GetMapping("/get/{id}")
    public AgricultureCompany get(@PathVariable Long id){
        return repository.findById(id).orElseThrow();
    }
}
