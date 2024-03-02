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
        Expenses savedExpenses = expensesRepository.save(expenses);
        user.setExpenses(savedExpenses); // Update the expenses in the User entity
        userRepository.save(user); // Save the updated User entity
        return ResponseEntity.ok(savedExpenses);
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

    @PostMapping("/add/{userId}/{expenseType}")
    public ResponseEntity<Expenses> addExtraExpenses(@PathVariable UUID userId, @PathVariable String expenseType, @RequestBody Double amount) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        Expenses expenses = user.getExpenses();
        if (expenses == null) {
            expenses = new Expenses();
            expenses.setUser(user);
        }
        switch (expenseType.toLowerCase()) {
            case "personal":
                expenses.setPersonal(expenses.getPersonal() + amount);
                break;
            case "work":
                expenses.setWork(expenses.getWork() + amount);
                break;
            case "home":
                expenses.setHome(expenses.getHome() + amount);
                break;
            case "other":
                expenses.setOther(expenses.getOther() + amount);
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }
        Expenses savedExpenses = expensesRepository.save(expenses);
        return ResponseEntity.ok(savedExpenses);
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