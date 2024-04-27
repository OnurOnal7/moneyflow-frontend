package ta4_1.MoneyFlow_Backend.Goals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/goals")
public class GoalController {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userId}")
    public ResponseEntity<?> addGoal(@PathVariable UUID userId, @RequestBody Goal goal) {
        return userRepository.findById(userId).map(user -> {
            goal.setUser(user);
            goalRepository.save(goal);
            return ResponseEntity.ok().body(goal);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Goal>> getGoalsByUser(@PathVariable UUID userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }
        List<Goal> goals = goalRepository.findByUserId(userId);
        return ResponseEntity.ok(goals);
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<?> updateGoal(@PathVariable UUID goalId, @RequestBody Goal goalDetails) {
        return goalRepository.findById(goalId).map(goal -> {
            goal.setDescription(goalDetails.getDescription());
            goal.setAmount(goalDetails.getAmount());
            goal.setTimeframe(goalDetails.getTimeframe());
            goalRepository.save(goal);
            return ResponseEntity.ok().body(goal);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<?> deleteGoal(@PathVariable UUID goalId) {
        if (!goalRepository.existsById(goalId)) {
            return ResponseEntity.notFound().build();
        }
        goalRepository.deleteById(goalId);
        return ResponseEntity.ok().build();
    }
}
