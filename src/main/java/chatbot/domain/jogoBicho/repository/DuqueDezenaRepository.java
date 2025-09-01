package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.DuqueDezena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DuqueDezenaRepository extends JpaRepository<DuqueDezena, Long> {
    List<DuqueDezena> findByApostaId(Long apostaId);
}