package ta4_1.MoneyFlow_Backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
    @GetMapping("/")
    public String welcome() {
        return "Welcome to MoneyFlow Insight";
    }

    @GetMapping("/version") //Updated Version
    public String getVersion() {
        return "MoneyFlow Insight Version 4.0";
    }
}
