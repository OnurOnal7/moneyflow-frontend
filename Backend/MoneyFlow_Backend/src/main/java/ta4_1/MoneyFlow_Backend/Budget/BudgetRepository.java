package ta4_1.MoneyFlow_Backend.Budget;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID> {
}
