package chatbot.domain.jogoBicho.repository;

import chatbot.domain.jogoBicho.entities.Aposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ApostaRepository extends JpaRepository<Aposta, Long> {
    Optional<Aposta> findByCodigoUnico(String codigoUnico);
    List<Aposta> findByDataAposta(LocalDate dataAposta);
}