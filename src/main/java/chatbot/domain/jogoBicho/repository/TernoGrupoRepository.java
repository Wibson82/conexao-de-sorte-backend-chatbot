package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.TernoGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TernoGrupoRepository extends JpaRepository<TernoGrupo, Long> {
    List<TernoGrupo> findByApostaId(Long apostaId);
}