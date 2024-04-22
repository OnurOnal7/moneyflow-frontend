package ta4_1.MoneyFlow_Backend.Cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ta4_1.MoneyFlow_Backend.Users.User;
import ta4_1.MoneyFlow_Backend.Users.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(CardController.class)
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private UUID userId;

    @BeforeEach
    void setup() {
        user = new User();
        userId = UUID.randomUUID();
        user.setId(userId);

        // Setup mocks
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
    }

    @Test
    void testCreateCard() throws Exception {
        Card newCard = new Card("Discover", "1234567890123456", "01/30", "789");
        newCard.setUser(user);
        newCard.setId(); // Correctly simulating the ID setting as done by the JPA repository

        given(cardRepository.save(any(Card.class))).willReturn(newCard);

        mockMvc.perform(MockMvcRequestBuilders.post("/cards/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Discover\",\"cardNumber\":\"1234567890123456\",\"expirationDate\":\"01/30\",\"cvv\":\"789\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newCard.getId().toString()));

        verify(cardRepository).save(any(Card.class));
        verify(userRepository).findById(userId);
    }

    @Test
    void testUpdateCard() throws Exception {
        UUID cardId = UUID.randomUUID();
        Card existingCard = new Card("Visa", "1111222233334444", "12/24", "123");
        existingCard.setId(); // Correctly setting ID without parameters
        existingCard.setUser(user);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(cardRepository.findById(cardId)).willReturn(Optional.of(existingCard));

        mockMvc.perform(MockMvcRequestBuilders.put("/cards/id/{userId}/{cardId}", userId, cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Visa\",\"cardNumber\":\"9999888877776666\",\"expirationDate\":\"01/31\",\"cvv\":\"999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Visa"))
                .andExpect(jsonPath("$.cardNumber").value("9999888877776666"))
                .andExpect(jsonPath("$.expirationDate").value("01/31"))
                .andExpect(jsonPath("$.cvv").value("999"));

        verify(cardRepository).save(existingCard);
        verify(userRepository).findById(userId);
    }
}
