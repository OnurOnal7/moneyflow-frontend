package ta4_1.MoneyFlow_Backend.Cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controller for Cards
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 *
 */
@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all cards for all users.
     *
     * @return A list of lists containing cards for each user.
     */
    @GetMapping("/cards")
    public List<List<Card>> getAllCards() {
        List<User> users = userRepository.findAll();
        List<List<Card>> allCards = new ArrayList<>();

        for (User u : users) {
            allCards.add(u.getCards());
        }

        return allCards;
    }

    /**
     * Retrieves all cards for a specific user.
     *
     * @param id The UUID of the user.
     * @return A list of cards for the specified user.
     */
    @GetMapping("/cards/userId/{id}")
    public ResponseEntity<List<Card>> getAllCardsOfUser(@PathVariable UUID id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getCards()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a specific card by its ID.
     *
     * @param id The UUID of the card.
     * @return The card with the specified ID.
     */
    @GetMapping("/cards/cardId/{id}")
    public Card getCard(@PathVariable UUID id) { return cardRepository.findById(id).get(); }

    /**
     * Creates a new card for a user.
     *
     * @param id   The UUID of the user.
     * @param card The card to be created.
     * @return The UUID of the newly created card.
     */
    @PostMapping("/cards/{id}")
    public UUID createCard(@PathVariable UUID id, @RequestBody Card card) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            card.setUser(user);
            cardRepository.save(card);
            user.addCard(card);
            userRepository.save(user);
            return card.getId();
        }

        return null;
    }
}