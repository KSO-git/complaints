package com.complaints.complaints.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoLocationService {

    public String getCountryByIp(String ip) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://ipapi.co/" + ip + "/country_name/";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Unknown";
        }
    }
}