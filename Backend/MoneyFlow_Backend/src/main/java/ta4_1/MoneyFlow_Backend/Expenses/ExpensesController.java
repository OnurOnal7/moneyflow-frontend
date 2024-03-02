package ta4_1.MoneyFlow_Backend.Expenses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;
import ta4_1.MoneyFlow_Backend.Users.User;

//import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Kemal Yavuz
 */

@RestController
@RequestMapping("/expenses")
public class ExpensesController {
    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userId}")
    public ResponseEntity<Expenses> createOrUpdateExpenses(@PathVariable UUID userId, @RequestBody Expenses expenses) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        expenses.setUser(user);
        return ResponseEntity.ok(expensesRepository.save(expenses));
    }

    @PostMapping
    public Expenses createExpenses(@RequestBody Expenses expenses) {
        //expenses.setDate(LocalDate.now()); // Set the date when creating
        return expensesRepository.save(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expenses> getExpensesById(@PathVariable UUID id) {
        Optional<Expenses> expenses = expensesRepository.findById(id);
        return expenses.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Expenses> getAllExpenses() {
        return expensesRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expenses> updateExpenses(@PathVariable UUID id, @RequestBody Expenses updatedExpenses) {
        return expensesRepository.findById(id)
                .map(expenses -> {
                    expenses.setPersonal(updatedExpenses.getPersonal());
                    expenses.setWork(updatedExpenses.getWork());
                    expenses.setHome(updatedExpenses.getHome());
                    expenses.setOther(updatedExpenses.getOther());
                    //expenses.setDate(LocalDate.now()); // Update the date
                    return ResponseEntity.ok(expensesRepository.save(expenses));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<Expenses> addExtraExpenses(@PathVariable UUID id, @RequestBody Expenses extraExpenses) {
        return expensesRepository.findById(id)
                .map(expenses -> {
                    expenses.setPersonal(expenses.getPersonal() + extraExpenses.getPersonal());
                    expenses.setWork(expenses.getWork() + extraExpenses.getWork());
                    expenses.setHome(expenses.getHome() + extraExpenses.getHome());
                    expenses.setOther(expenses.getOther() + extraExpenses.getOther());
                    // expenses.setDate(LocalDate.now()); // Update the date
                    return ResponseEntity.ok(expensesRepository.save(expenses));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpenses(@PathVariable UUID id) {
        return expensesRepository.findById(id)
                .map(expenses -> {
                    expensesRepository.delete(expenses);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}