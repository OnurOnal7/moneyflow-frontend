package ta4_1.MoneyFlow_Backend.Cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.util.UUID;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;

    @Column(name = "cvv", nullable = false)
    private String cvv;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Card(){

    }

    public Card(String firstName, String lastName, String cardNumber, String expirationDate, String cvv){
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
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

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    public UUID getId() {
        return this.id;
    }

    public void setId() {
        this.id = UUID.randomUUID();
    }

    public String getCvv() { return this.cvv; }

    public void setCvv(String cvv) { this.cvv = cvv; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return id + " "
                + firstName + " "
                + lastName + " "
                + cardNumber + " "
                + expirationDate + " "
                + cvv;
    }
}
