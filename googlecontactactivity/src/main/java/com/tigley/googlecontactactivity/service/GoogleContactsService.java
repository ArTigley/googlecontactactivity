package com.tigley.googlecontactactivity.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GoogleContactsService {
    private static final String GOOGLE_CONTACTS_API_URL =
            "https://people.googleapis.com/v1/people/me/connections?personFields=names,emailAddresses,phoneNumbers";

    public static Map<String, Object> getContacts(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            return restTemplate.exchange(GOOGLE_CONTACTS_API_URL, HttpMethod.GET, entity, Map.class).getBody();
        } catch (Exception e) {
            System.out.println("Error fetching contacts: " + e.getMessage());
            return Map.of("error", "Failed to fetch contacts");
        }
    }
}
