package ta4_1.MoneyFlow_Backend.Cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    // Gets all cards of all users.
    @GetMapping("/cards")
    public List<List<Card>> getAllCards() {
        List<User> users = userRepository.findAll();
        List<List<Card>> allCards = new ArrayList<>();

        for (User u : users) {
            allCards.add(u.getCards());
        }

        return allCards;
    }

    // Gets all cards of a users.
    @GetMapping("/cards/userId/{id}")
    public List<Card> getAllCardsOfUser(@PathVariable UUID id) {
        return userRepository.findById(id).get().getCards();
    }

    // Gets a card.
    @GetMapping("/cards/cardId/{id}")
    public Card getCard(@PathVariable UUID id) { return cardRepository.findById(id).get(); }

    // Creates a card.
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
