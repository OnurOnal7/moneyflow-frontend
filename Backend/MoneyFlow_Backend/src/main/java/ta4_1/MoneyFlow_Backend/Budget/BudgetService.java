package ta4_1.MoneyFlow_Backend.Budget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ta4_1.MoneyFlow_Backend.Expenses.Expenses;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BudgetService {

    private final UserRepository userRepository;

    @Autowired
    public BudgetService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Double> checkBudgetLimits(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Budget budget = user.getBudget();
            Expenses expenses = user.getExpenses();

            Map<String, Double> overages = new HashMap<>();
            if (expenses.getPersonal() > budget.getPersonalLimit()) {
                overages.put("Personal", expenses.getPersonal() - budget.getPersonalLimit());
            }
            if (expenses.getWork() > budget.getWorkLimit()) {
                overages.put("Work", expenses.getWork() - budget.getWorkLimit());
            }
            if (expenses.getHome() > budget.getHomeLimit()) {
                overages.put("Home", expenses.getHome() - budget.getHomeLimit());
            }
            if (expenses.getOther() > budget.getOtherLimit()) {
                overages.put("Other", expenses.getOther() - budget.getOtherLimit());
            }

            return overages;
        }
        return new HashMap<>();  // Return empty map if user or budget is not found
    }
}
