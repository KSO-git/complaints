package com.complaints.complaints.service;

import com.complaints.complaints.model.Complaint;
import com.complaints.complaints.repository.ComplaintsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintsService {

    private final ComplaintsRepository repository;
    private final GeoLocationService geoLocationService = new GeoLocationService();

    public ComplaintsService(ComplaintsRepository repository, GeoLocationService geoLocationService) {
        this.repository = repository;
    }

    public Complaint addComplaint(Complaint complaint, String ip) {
        return repository.findByProductIdAndCustomer(complaint.getProductId(), complaint.getCustomer())
                .map(existing -> {
                    existing.setCounter(existing.getCounter() + 1);
                    return repository.save(existing);
                })
                .orElseGet(() -> {
                    complaint.setDateOfCreation(LocalDateTime.now());
                    complaint.setCountry(geoLocationService.getCountryByIp(ip));
                    complaint.setCounter(1);
                    return repository.save(complaint);
                });
    }

    public List<Complaint> getAll() {
        return repository.findAll();
    }

    public Complaint getById(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cannot find Complaint with id:" + id));
    }

    public Complaint editComplaint(Long id, String newDescription) {
        Complaint Complaints = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Complaint with id:" + id));
        Complaints.setDescription(newDescription);
        return repository.save(Complaints);
    }
}
