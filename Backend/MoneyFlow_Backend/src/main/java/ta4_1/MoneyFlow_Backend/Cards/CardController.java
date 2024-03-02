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
    @GetMapping("/card")
    public List<List<Card>> getAllCards() {
        List<User> users = userRepository.findAll();
        List<List<Card>> allCards = new ArrayList<>();

        for (User u : users) {
            allCards.add(u.getCards());
        }

        return allCards;
    }

    // Gets all cards of a users.
    @GetMapping("/card/userId/{id}")
    public List<Card> getAllCardsOfUser(@PathVariable UUID id) {
        return userRepository.findById(id).get().getCards();
    }

    // Gets a card.
    @GetMapping("/card/cardId/{id}")
    public Card getCard(@PathVariable UUID id) { return cardRepository.findById(id).get(); }

    // Creates a card.
    @PostMapping("/card/{id}")
    public UUID createCard(@PathVariable UUID id, @RequestBody Card c) {
        Optional<User> userOptional = userRepository.findById(id);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setCard(c);
            return user.getId();
        }

        return null;
    }

}
