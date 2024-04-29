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

    @Column(name = "apple_shares")
    private double AAPLShares;

    @Column(name = "amazon_shares")
    private double AMZNShares;

    @Column(name = "bitcoin")
    private double BTCUSDTShares;

    @Column(name = "dogecoin")
    private double DOGEUSDTShares;

    @Column(name = "apple_price")
    private double AAPLPrice;

    @Column(name = "amazon_price")
    private double AMZNPrice;

    @Column(name = "bitcoin_price")
    private double BTCUSDTPrice;

    @Column(name = "dogecoin_price")
    private double DOGEUSDTPrice;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;  // The user associated with this portfolio.

    public Portfolio() {
    }

    public Portfolio(double portfolioValue, double AAPLShares, double AMZNShares, double BTCUSDTShares, double DOGEUSDTShares, double AAPLPrice, double AMZNPrice, double BTCUSDTPrice, double DOGEUSDTPrice) {
        this.portfolioValue = portfolioValue;
        this.AAPLShares = AAPLShares;
        this.AMZNShares = AMZNShares;
        this.BTCUSDTShares = BTCUSDTShares;
        this.DOGEUSDTShares = DOGEUSDTShares;
        this.AAPLPrice = AAPLPrice;
        this.AMZNPrice = AMZNPrice;
        this.BTCUSDTPrice = BTCUSDTPrice;
        this.DOGEUSDTPrice = DOGEUSDTPrice;
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

    public double getAppleShares() {
        return AAPLShares;
    }

    public double getAmazonShares() {
        return AMZNShares;
    }

    public double getBitcoin() {
        return BTCUSDTShares;
    }

    public double getDogecoin() {
        return DOGEUSDTShares;
    }

    public double getApplePrice() {
        return AAPLPrice;
    }

    public double getAmazonPrice() {
        return AMZNPrice;
    }

    public double getBitcoinPrice() {
        return BTCUSDTPrice;
    }

    public double getDogecoinPrice() {
        return DOGEUSDTPrice;
    }

    public void setAppleShares(double AAPLShares) {
        this.AAPLShares = AAPLShares;
    }

    public void setAmazonShares(double AMZNShares) {
        this.AMZNShares = AMZNShares;
    }

    public void setBitcoin(double BTCUSDTShares) {
        this.BTCUSDTShares = BTCUSDTShares;
    }

    public void setDogecoin(double DOGEUSDTShares) {
        this.DOGEUSDTShares = DOGEUSDTShares;
    }

    public void setApplePrice(double AAPLPrice) {
        this.AAPLPrice = AAPLPrice;
    }

    public void setAmazonPrice(double AMZNPrice) {
        this.AMZNPrice = AMZNPrice;
    }

    public void setBitcoinPrice(double BTCUSDTPrice) {
        this.BTCUSDTPrice = BTCUSDTPrice;
    }

    public void setDogecoinPrice(double DOGEUSDTPrice) {
        this.DOGEUSDTPrice = DOGEUSDTPrice;
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
                ", Apple Shares=" + AAPLShares +
                ", Amazon Shares=" + AMZNShares +
                ", Bitcoin=" + BTCUSDTShares +
                ", Dogecoin=" + DOGEUSDTShares +
                ", Apple Price=" + AAPLPrice +
                ", Amazon Price=" + AMZNPrice +
                ", Bitcoin Price=" + BTCUSDTPrice +
                ", Dogecoin Price=" + DOGEUSDTPrice +
                '}';
    }
}
