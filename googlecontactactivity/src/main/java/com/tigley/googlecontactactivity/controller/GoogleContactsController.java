package com.tigley.googlecontactactivity.controller;

import com.tigley.googlecontactactivity.service.GoogleContactsService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class GoogleContactsController {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final GoogleContactsService googleContactsService;

    public GoogleContactsController(OAuth2AuthorizedClientService authorizedClientService,
                                    GoogleContactsService googleContactsService) {
        this.authorizedClientService = authorizedClientService;
        this.googleContactsService = googleContactsService;
    }

    @GetMapping("/contacts")
    public String getContacts(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        if (client == null) {
            model.addAttribute("contacts", Map.of("error", "Not authorized to access Google Contacts"));
            return "contacts";
        }

        String accessToken = client.getAccessToken().getTokenValue();

        // Fetch contacts from Google API
        Map<String, Object> contacts = googleContactsService.getContacts(accessToken);

        // Ensure contacts is always set
        if (contacts == null) {
            contacts = Map.of("error", "Failed to retrieve contacts");
        }

        model.addAttribute("contacts", contacts);

        return "contacts"; // Render contacts.html
    }
}
