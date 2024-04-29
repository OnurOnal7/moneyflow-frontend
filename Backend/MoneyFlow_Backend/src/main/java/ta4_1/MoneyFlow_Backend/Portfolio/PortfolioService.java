package ta4_1.MoneyFlow_Backend.Portfolio;

import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    /**
     * Updates the user's total portfolio value based on shares held and stock prices.
     *
     * @param userPortfolio The user's portfolio.
     * @return The updated portfolio value.
     */
    public double updatePortfolioValue(Portfolio userPortfolio) {
        return (userPortfolio.getAppleShares() * userPortfolio.getApplePrice()) +
               (userPortfolio.getAmazonShares() * userPortfolio.getAmazonPrice()) +
               (userPortfolio.getBitcoin() * userPortfolio.getBitcoinPrice()) +
               (userPortfolio.getDogecoin() * userPortfolio.getDogecoinPrice());
    }
}
