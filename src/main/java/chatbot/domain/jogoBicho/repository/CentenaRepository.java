package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.Centena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CentenaRepository extends JpaRepository<Centena, Long> {
    List<Centena> findByApostaId(Long apostaId);
}