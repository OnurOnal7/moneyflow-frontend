package ta4_1.MoneyFlow_Backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 *
 * What happens here is that the serverendpoint -- in this case it is
 * the /chat endpoint handler is registered with SPRING
 * so that requests to ws:// will be honored.
 */
@Configuration
@Profile("!test") // This makes sure the WebSocket config is not loaded during tests
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
