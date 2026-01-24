package epam.report.emailsender.repository;

import epam.report.emailsender.model.AgricultureCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgricultureRepository extends JpaRepository<AgricultureCompany, Long> {
}
