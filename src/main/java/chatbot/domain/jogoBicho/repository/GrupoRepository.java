package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByApostaId(Long apostaId);
}