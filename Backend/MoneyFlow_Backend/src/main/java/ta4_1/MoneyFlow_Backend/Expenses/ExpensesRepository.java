package ta4_1.MoneyFlow_Backend.Expenses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Kemal Yavuz
 */

public interface ExpensesRepository extends JpaRepository<Expenses, UUID> {
    Optional<Expenses> findById(UUID id);

    @Transactional
    void deleteById(UUID id);
}
