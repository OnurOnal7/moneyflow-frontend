package ta4_1.MoneyFlow_Backend.Portfolio;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.UUID;

/**
 * Entity Representing a user investment portfolio.
 *
 * @author Kemal Yavuz
 * @author Onur Onal
 */

@Entity
@Table(name = "portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "portfolio_value")
    private double portfolioValue;

    @Column(name = "stock1")
    private double stock1Shares;

    @Column(name = "stock2")
    private double stock2Shares;
}
