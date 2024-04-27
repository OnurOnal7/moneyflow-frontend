package ta4_1.MoneyFlow_Backend.Portfolio;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Expenses.Expenses;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for managing portfolios.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Purchases a stock for a user.
     *
     * @param id              The ID of the user whose expenses to update.
     * @param updatedExpenses The updated expenses object.
     * @return ResponseEntity with the updated expenses.
     */
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<Expenses> purchaseStock(@PathVariable UUID id, @RequestBody Expenses updatedExpenses) {
        return userRepository.findById(id)
                .map(user -> {
                    Expenses currentExpenses = user.getExpenses();
                    currentExpenses.setPersonal(currentExpenses.getPersonal() + updatedExpenses.getPersonal());
                    currentExpenses.setWork(currentExpenses.getWork() + updatedExpenses.getWork());
                    currentExpenses.setHome(currentExpenses.getHome() + updatedExpenses.getHome());
                    currentExpenses.setOther(currentExpenses.getOther() + updatedExpenses.getOther());

                    return ResponseEntity.ok(currentExpenses); // Return the updated expenses
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a portfolio for a user.
     *
     * @param userId    The ID of the user.
     * @param portfolio The portfolio object to be created.
     * @return ResponseEntity with the created portfolio.
     */
    @PostMapping("/{userId}")
    public ResponseEntity<?> createPortfolio(@PathVariable UUID userId, @RequestBody Portfolio portfolio) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        portfolio.setUser(user);
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);
        user.setPortfolio(savedPortfolio);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("portfolioId", portfolio.getId()));
    }

    /**
     * Get a portfolio of a user.
     *
     * @param id The ID of the user.
     * @return ResponseEntity with the found portfolio.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolioOfUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getPortfolio()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all portfolios.
     *
     * @return List of all portfolios.
     */
    @GetMapping
    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }




}
