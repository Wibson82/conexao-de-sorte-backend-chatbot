package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.TernoDezena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TernoDezenaRepository extends JpaRepository<TernoDezena, Long> {
    List<TernoDezena> findByApostaId(Long apostaId);
}