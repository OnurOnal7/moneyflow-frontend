package ta4_1.MoneyFlow_Backend.Users;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * Controller for managing user-related operations in the MoneyFlow application.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 *
 */
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository; // Injects the UserRepository for database operations.

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // Injects the password encoder for hashing passwords.

    /**
     * Retrieves a list of users filtered by their type.
     *
     * @param userType the type of users to retrieve
     * @return a list of users with the specified type
     */
    @GetMapping("/users/type/{userType}")
    public List<User> getUsersByType(@PathVariable String userType) {
        return userRepository.findByType(userType);
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user to retrieve
     * @return an Optional containing the user if found, or an empty Optional otherwise
     */
    @GetMapping("/users/id/{id}")
    public Optional<User> getUser(@PathVariable UUID id) { return userRepository.findById(id); }

    /**
     * Retrieves a list of all users.
     *
     * @return  a list of all users
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Handles user signup by creating a new user with the provided information.
     *
     * @param u the user to be created
     * @return ID of the User
     */
    @PostMapping("/signup")
    public UUID signup(@RequestBody User u) {
        u.setType("regular");
        u.setPassword(passwordEncoder.encode(u.getPassword())); // Use the autowired encoder for password encoding
        userRepository.save(u);
        return u.getId();
    }

    /**
     * Handles guest login by creating a new user with the type "guest".
     *
     * @return a success message
     */
    @PostMapping("/login/type/guest")
    public UUID loginGuest() {
        User u = new User();
        u.setType("guest");
        u.setFirstName("Guest");
        u.setLastName("User");
        u.setPassword("N/A");
        String uniqueEmail = "guest_" + UUID.randomUUID().toString() + "@example.com";
        u.setEmail(uniqueEmail);
        u.setMonthlyIncome(0.0);
        u.setAnnualIncome(0.0);
        userRepository.save(u);
        return u.getId();
    }

    /**
     * Handles user login by verifying the provided email and password.
     *
     * @param email the email of the user attempting to log in
     * @param password  the password of the user attempting to log in
     * @return ID of the User
     * @throws ResponseStatusException if the user is not found or the password is incorrect
     */
    @PostMapping("/login")
    public UUID login(@RequestParam String email, @RequestParam String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user.getId();
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password, Access Denied");
        }
    }

    /**
     * Updates the information of an existing user.
     *
     * @param id the ID of the user to be updated
     * @param u the updated user information
     * @return Updated user if the update is successful
     */
    @PutMapping("/users/{id}")
    @Transactional
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User u) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(u.getFirstName());
                    user.setLastName(u.getLastName());
                    user.setEmail(u.getEmail());
                    user.setMonthlyIncome(u.getMonthlyIncome());
                    user.setAnnualIncome(u.getAnnualIncome());
                    user.setPassword(passwordEncoder.encode(u.getPassword()));
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/users/{id}/income")
    public ResponseEntity<User> updateIncome(@PathVariable UUID id, @RequestBody Map<String, Double> incomeMap) {
        if (!incomeMap.containsKey("income")) {
            return ResponseEntity.badRequest().build();
        }
        Double income = incomeMap.get("income");
        return userRepository.findById(id)
                .map(user -> {
                    user.setMonthlyIncome(income);
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Generates a financial report for a user.
     *
     * @param id the ID of the user for whom the report is to be generated
     * @return  a ResponseEntity containing the financial report or a not found status
     */
    @GetMapping("/{id}/monthlyIncome")
    public ResponseEntity<Double> getMonthlyIncome(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            double monthlyIncome = user.getMonthlyIncome();
            return ResponseEntity.ok(monthlyIncome);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Generates a financial report for a user.
     *
     * @param id the ID of the user for whom the report is to be generated
     * @return  a ResponseEntity containing the financial report or a not found status
     */
    @GetMapping("/{id}/annualIncome")
    public ResponseEntity<Double> getAnnualIncome(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            double annualIncome = user.getAnnualIncome();
            return ResponseEntity.ok(annualIncome);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upgradeType/{userId}")
    public ResponseEntity<String> upgradeType(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setType("premium");
                    userRepository.save(user);
                    return ResponseEntity.ok("User upgraded to premium");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/userType/{userId}")
    public ResponseEntity<String> getUserType(@PathVariable UUID userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(user.getType()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Generates a financial report for a user.
     *
     * @param id the ID of the user for whom the report is to be generated
     * @return  a ResponseEntity containing the financial report or a not found status
     */
    @GetMapping("/{id}/financial-report")
    public ResponseEntity<Double> generateFinancialReport(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            double budget = user.generateBudget();
            return ResponseEntity.ok(budget);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the ID of the user to be deleted
     * @return  a success message
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return ResponseEntity.ok("User deleted successfully");
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

/*
    /**
     * Retrieve the monthly income of the user.
     *
     * @param id the ID of the user from whom the monthly income is retrieved.
     * @return  a ResponseEntity of the user's monthly income.
     */ /*
    @GetMapping("/users/monthly/{id}")
    public ResponseEntity<Double> getMonthlyIncome(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            double income = user.getMonthlyIncome();
            return ResponseEntity.ok(income);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/
/*
    /**
     * Retrieve the annual income of the user.
     *
     * @param id the ID of the user from whom the annual income is retrieved.
     * @return  a ResponseEntity of the user's annual income.
     *//*
    @GetMapping("/users/annual/{id}")
    public ResponseEntity<Double> getAnnualIncome(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            double income = user.getAnnualIncome();
            return ResponseEntity.ok(income);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    */
}