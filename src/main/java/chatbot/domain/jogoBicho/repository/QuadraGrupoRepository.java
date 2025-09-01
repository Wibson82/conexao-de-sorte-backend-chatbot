package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.QuinaGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuadraGrupoRepository extends JpaRepository<QuinaGrupo, Long> {
    List<QuinaGrupo> findByApostaId(Long apostaId);
}