package ta4_1.MoneyFlow_Backend.Users;

import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Cards.Card;
import ta4_1.MoneyFlow_Backend.Expenses.Expenses;

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

    @OneToOne(mappedBy = "user")
    private Expenses expenses;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true) // Change nullable to true
    private String email;

    @Column(name = "type")
    private String type;

    @Column(name = "income")
    private double income;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;

    public User(){

    }

    public User(String firstName, String lastName, String password, String email, double income){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.income = income;
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

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public List<Card> getCards() { return this.cards; }

    public void setCards(List<Card> cards) {
        this.cards.clear();
        if (cards != null) {
            for (Card card : cards) {
                setCard(card);
            }
        }
    }

    public void setCard(Card card) {
        if (cards != null) {
            this.cards.add(card);
            card.setUser(this);
        }
    }

    public Expenses getExpenses() {
        return expenses;
    }

    public void setExpenses(Expenses expenses) {
        this.expenses = expenses;
        if (expenses != null) {
            expenses.setUser(this); // Set the user in the Expenses entity
        }
    }

    public String generateFinancialReport() {
        double totalExpenses = expenses != null ? expenses.getTotalExpenses() : 0;
        double budget = income - totalExpenses;
        return "Income: " + income + "\nExpenses: " + totalExpenses + "\n" + firstName + ", You Have " + budget + "$ to spend on entertainment, investing, or other.";
    }

    @Override
    public String toString() {
        return id + " "
                + firstName + " "
                + lastName + " "
                + password + " "
                + email + " "
                + income;
    }
}

