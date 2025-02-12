package de.v6hq.java.spring.hinitializer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.v6hq.java.spring.hinitializer.persistence.User;
import de.v6hq.java.spring.hinitializer.persistence.UserRepository;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId;

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String userNameFromProvider = oAuth2User.getName(); // or a specific attribute like 'id' from the provider
            UUID uuid = UUID.nameUUIDFromBytes(userNameFromProvider.getBytes());
            userId = uuid.toString();
            logger.info("Generated UUID from userName {}: {}", userNameFromProvider, userId);
        } else {
            userId = authentication.getName(); // Fallback to username if not OAuth2
            logger.info("User ID (not OAuth2): {}", userId);
        }

        // Persistence logic:
        User user = userRepository.findById(userId).orElse(null); // Try to find the user
        if (user == null) {
            user = new User();
            user.setId(userId);
            logger.info("Created new user with ID: {}", userId);
        }
        model.addAttribute("lastLoggedIn", user.getLastLoggedIn());
        user.setLastLoggedIn(LocalDateTime.now());
        model.addAttribute("currentLoggedIn", user.getLastLoggedIn());
        userRepository.save(user); // Save or update the user
        logger.info("User ID: {}", userId);

        return "dashboard"; // Gibt die index.html zurück
    }
}