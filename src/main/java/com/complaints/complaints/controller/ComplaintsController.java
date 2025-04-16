package com.complaints.complaints.controller;

import com.complaints.complaints.model.Complaint;
import com.complaints.complaints.service.ComplaintsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaints")
public class ComplaintsController {

    private final ComplaintsService service;

    public ComplaintsController(ComplaintsService service) {
        this.service = service;
    }

    @PostMapping
    public Complaint addComplaint(@RequestBody Complaint Complaint, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return service.addComplaint(Complaint, ip);
    }

    @GetMapping
    public List<Complaint> getAll() {
        return service.getAll();
    }

    @GetMapping("/complaint/{id}")
    public Complaint getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Complaint editComplaint(@PathVariable Long id, @RequestBody String newDescription) {
        return service.editComplaint(id, newDescription);
    }
}
