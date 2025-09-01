package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.QuinaDezena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuinaDezenaRepository extends JpaRepository<QuinaDezena, Long> {
    List<QuinaDezena> findByApostaId(Long apostaId);
}