package ta4_1.MoneyFlow_Backend.Goals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoalController(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Goal> createGoal(@PathVariable UUID userId, @RequestBody Goal goal) {
        return userRepository.findById(userId).map(user -> {
            goal.setUser(user);
            return ResponseEntity.ok(goalRepository.save(goal));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Goal>> getGoalsByUser(@PathVariable UUID userId) {
        List<Goal> goals = goalRepository.findByUserId(userId);
        return ResponseEntity.ok(goals);
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<Goal> updateGoal(@PathVariable UUID goalId, @RequestBody Goal goalDetails) {
        return goalRepository.findById(goalId).map(goal -> {
            goal.setGoalString(goalDetails.getGoalString());
            return ResponseEntity.ok(goalRepository.save(goal));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID goalId) {
        return goalRepository.findById(goalId).map(goal -> {
            goalRepository.delete(goal);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}