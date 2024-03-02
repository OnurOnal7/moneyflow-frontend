package ta4_1.MoneyFlow_Backend.Cards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Onur Onal
 */

public interface CardRepository extends JpaRepository<Card, UUID> {
    Optional<Card> findById(UUID id);

    @Transactional
    void deleteById(UUID id);
    
}
