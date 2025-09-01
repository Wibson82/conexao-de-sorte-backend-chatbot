package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.Milhar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilharRepository extends JpaRepository<Milhar, Long> {
    List<Milhar> findByApostaId(Long apostaId);
}