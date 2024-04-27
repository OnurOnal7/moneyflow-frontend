package ta4_1.MoneyFlow_Backend.Portfolio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ta4_1.MoneyFlow_Backend.Users.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * Entity Representing a user investment portfolio.
 *
 * @author Onur Onal
 * @author Kemal Yavuz
 */

@Entity
@Table(name = "portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "portfolio_value")
    private double portfolioValue;

    @Column(name = "stock1_shares")
    private double stock1Shares;

    @Column(name = "stock2_shares")
    private double stock2Shares;

    @Column(name = "stock1_price")
    private double stock1Price;

    @Column(name = "stock2_price")
    private double stock2Price;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;  // The user associated with this portfolio.

    public Portfolio() {
    }

    public Portfolio(double portfolioValue, double stock1Shares, double stock2Shares, double stock1Price, double stock2Price) {
        this.portfolioValue = roundToTwoDecimalPlaces(portfolioValue);
        this.stock1Shares = stock1Shares;
        this.stock2Shares = stock2Shares;
        this.stock1Price = stock1Price;
        this.stock2Price = stock2Price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(double portfolioValue) {
        if (portfolioValue >= 0) {
            this.portfolioValue = roundToTwoDecimalPlaces(portfolioValue);
        }
    }

    public double getStock1Shares() {
        return stock1Shares;
    }

    public void setStock1Shares(double stock1Shares) {
        if (stock1Shares >= 0) {
            this.stock1Shares = stock1Shares;
        }
    }

    public double getStock2Shares() {
        return stock2Shares;
    }

    public void setStock2Shares(double stock2Shares) {
        if (stock2Shares >= 0) {
            this.stock2Shares = stock2Shares;
        }
    }

    public double getStock1Price() {
        return stock1Price;
    }

    public void setStock1Price(double stock1Price) {
        if (stock1Price >= 0) {
            this.stock1Price = stock1Price;
        }
    }

    public double getStock2Price() {
        return stock2Price;
    }

    public void setStock2Price(double stock2Price) {
        if (stock2Price >= 0) {
            this.stock2Price = stock2Price;
        }
    }

    private double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", portfolio value=" + portfolioValue +
                ", stock 1 shares=" + stock1Shares +
                ", stock 2 shares=" + stock2Shares +
                ", stock 1 price=" + stock1Price +
                ", stock 2 price=" + stock2Price + '}';
    }
}
