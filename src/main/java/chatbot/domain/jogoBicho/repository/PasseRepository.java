package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.Passe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasseRepository extends JpaRepository<Passe, Long> {
    List<Passe> findByApostaId(Long apostaId);
}