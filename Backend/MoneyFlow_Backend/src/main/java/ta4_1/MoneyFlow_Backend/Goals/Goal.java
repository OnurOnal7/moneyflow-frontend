package ta4_1.MoneyFlow_Backend.Goals;

import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;
import java.util.UUID;

@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "timeframe", nullable = false) // timeframe in months
    private Integer timeframe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Goal() {
    }

    public Goal(String description, Double amount, Integer timeframe) {
        this.description = description;
        this.amount = amount;
        this.timeframe = timeframe;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(Integer timeframe) {
        this.timeframe = timeframe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
