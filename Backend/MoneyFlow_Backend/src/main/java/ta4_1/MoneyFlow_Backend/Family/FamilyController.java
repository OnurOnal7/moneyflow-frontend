package ta4_1.MoneyFlow_Backend.Family;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.UUID;

@RestController
@RequestMapping("/family")
public class FamilyController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @PostMapping("/addMember/{userId}")
    public User addFamilyMember(@PathVariable UUID userId, @RequestBody User familyMember) {
        User mainUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Family family = mainUser.getFamily();
        if (family == null) {
            family = new Family();
            familyRepository.save(family);
            mainUser.setFamily(family);
        }

        String uniqueEmail = "family_" + UUID.randomUUID().toString() + "@example.com";
        familyMember.setEmail(uniqueEmail);
        familyMember.setType("family");
        familyMember.setFamily(family);

        userRepository.save(familyMember);
        return familyMember;
    }
}