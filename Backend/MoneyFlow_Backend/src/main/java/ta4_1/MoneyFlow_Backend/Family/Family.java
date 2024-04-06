package ta4_1.MoneyFlow_Backend.Family;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Cards.Card;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.util.Set;
import java.util.UUID;

@Entity
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "family")
    @JsonManagedReference
    private Set<User> users;

    @OneToMany(mappedBy = "family")
    @JsonManagedReference
    private Set<Card> cards;


    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void addCard(Card card) {
        cards.add(card);
        card.setFamily(this);
    }

    public void removeCard(Card card) {
        cards.remove(card);
        card.setFamily(null);
    }
}
