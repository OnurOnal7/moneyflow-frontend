package ta4_1.MoneyFlow_Backend.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Onur Onal
 */

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    // Gets all users of any type.
    @GetMapping("/users/{userType}")
    public List<User> getUsersByType(@PathVariable String userType) {
        return userRepository.findByType(userType);
    }

    // Gets a user.
    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable UUID id) {
        return userRepository.findById(id);
    }

    // Gets all users.
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Sign up operation.
    @PostMapping("/signup")
    public String signup(@RequestBody User u) {
        u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
        userRepository.save(u);
        return "Success";
    }

    // Login operation.
    @PostMapping("/login/")
    public String login(@RequestParam String email, @RequestParam String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, user.getPassword())) {
                return "Welcome back, " + user.getFirstName() + "!";
            }
            else {
                return "Incorrect password, Access Denied";
            }
        }
        else {
            return "No such user";
        }
    }

    // Updates a user.
    @PutMapping("/users/{id}")
    public Optional<User> updateUser(@PathVariable UUID id, @RequestBody User u) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(u.getFirstName());
            user.setLastName(u.getLastName());
            user.setEmail(u.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));

            userRepository.save(user);
        }
        return userOptional;
    }

    /* Deletes a user.
    @DeleteMapping("/users/type/{userType}/{email}")
    public String deleteUser(@PathVariable String userType, @PathVariable String email) {
        HashMap<String, User> selectedUsers;

        if ("regular".equals(userType)) {
            selectedUsers = regularUserList;
        } else if ("premium".equals(userType)) {
            selectedUsers = premiumUserList;
        } else if ("guest".equals(userType)) {
            selectedUsers = guestUserList;
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }

        if (selectedUsers.containsKey(email)) {
            String response = selectedUsers.get(email).getFirstName();
            selectedUsers.remove(email);
            return "User " +  response + " has been successfully removed.";
        } else {
            return "User not found.";
        }
    }
     */
}

