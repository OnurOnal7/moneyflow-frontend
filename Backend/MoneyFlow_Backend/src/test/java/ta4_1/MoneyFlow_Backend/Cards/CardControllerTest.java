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
        UUID userId = UUID.randomUUID(); // Assume this is the ID of the existing user.
        Card newCard = new Card("Discover", "1234567890123456", "01/30", "789");
        User user = new User(); // Simulated user, ensure it has valid properties for the test.
        user.setId(userId);

        // Simulate setting ID when saved.
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card savedCard = invocation.getArgument(0);
            savedCard.setId(UUID.randomUUID()); // Simulate the JPA setting the ID on save.
            return savedCard;
        });

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/cards/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Discover\",\"cardNumber\":\"1234567890123456\",\"expirationDate\":\"01/30\",\"cvv\":\"789\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty()); // Adjust to check the ID is not empty.

        verify(cardRepository).save(any(Card.class));
        verify(userRepository).findById(userId);
    }

    @Test
    void testUpdateCard() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        Card existingCard = new Card("Visa", "1111222233334444", "12/24", "123");
        existingCard.setId(cardId);

        User user = new User();
        user.setId(userId);
        user.addCard(existingCard); // Ensure this method correctly adds the card to the user's list

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/cards/id/{userId}/{cardId}", userId, cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Visa\",\"cardNumber\":\"9999888877776666\",\"expirationDate\":\"01/31\",\"cvv\":\"999\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Visa"))
                .andExpect(jsonPath("$.cardNumber").value("9999888877776666"))
                .andExpect(jsonPath("$.expirationDate").value("01/31"))
                .andExpect(jsonPath("$.cvv").value("999"));

        // Assuming you add a save operation in the controller
        verify(cardRepository, times(1)).save(existingCard);
        verify(userRepository, times(1)).findById(userId);
    }
}
