package ta4_1.MoneyFlow_Backend.Recommendations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.util.UUID;

/**
 * Entity representing a recommendation.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 *
 */
@Entity
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;    // Unique identifier for a recommendation

    @Column(name = "recommendation", nullable = false)
    private String recommendation;    // A recommendation

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // The user associated with this recommendation

    /**
     * Default constructor
     */
    public Recommendation() {

    }

    /**
     * Constructor with parameters.
     *
     * @param recommendation  The recommendation.
     */
    public Recommendation(String recommendation){
        this.recommendation = recommendation;
    }

    // Getters and setters

    public String getRecommendation() { return this.recommendation; }

    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public UUID getId() { return this.id; }

    public void setId(UUID id) { this.id = id; }

    public User getUser() { return this.user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return id + " "
                + recommendation;
    }
}
