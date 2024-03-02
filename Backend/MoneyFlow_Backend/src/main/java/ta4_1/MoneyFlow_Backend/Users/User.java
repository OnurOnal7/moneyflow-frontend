package ta4_1.MoneyFlow_Backend.Users;

import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Cards.Card;

import java.util.List;
import java.util.UUID;

/**
 * Provides the Definition/Structure for the user table
 *
 * @author Onur Onal
 */

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "type")
    private String type = "regular";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;

    public User(){

    }

    public User(String firstName, String lastName, String username, String password, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId() {
        this.id = UUID.randomUUID();
    }

    public String getType() { return this.type; }

    public void setType(String type) { this.type = type; }

    public List<Card> getCards() { return this.cards; }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        if (cards != null) {
            for (Card card : cards) {
                card.setUser(this);
            }
        }
    }

    @Override
    public String toString() {
        return id + " "
                + firstName + " "
                + lastName + " "
                + password + " "
                + email;
    }
}

